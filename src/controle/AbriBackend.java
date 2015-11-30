/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import modele.AbriException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import modele.Annuaire;
import modele.Abri;
import modele.Message;
import modele.Adresses;
import modele.NoeudCentralException;

/**
 *
 * @author Gwenole Lecorve
 * @author David Guennec
 */
public class AbriBackend extends UnicastRemoteObject implements AbriLocalInterface, AbriRemoteInterface {
    
    protected String url;
    protected String controleurUrl;
    protected String noeudCentralUrl;
    protected boolean estConnecte;
    protected Abri abri;
    final public NouveauControleur controleur;
    protected NoeudCentralRemoteInterface noeudCentral;
    
    protected Annuaire abrisDistants;    // Map faisant le lien entre les url et les interfaces RMI des abris distants
    protected ArrayList<String> copains; // Les urls des autres membres du groupe de l'abri courant // Pas dans l'annuaire -> imposer la gestion d'une liste locale aux abris pour les groupes
    
    protected Semaphore semaphore;
    
    public AbriBackend(String _url, Abri _abri) throws RemoteException, MalformedURLException {

        this.estConnecte = false;
        this.url = _url;
        this.controleurUrl = _url+"/controleur";
        this.noeudCentralUrl = "";
        
        this.abri = _abri;
        this.controleur = new NouveauControleur(controleurUrl, this, null);
        this.noeudCentral = null;
        
        this.abrisDistants = new Annuaire();
        this.copains = new ArrayList<String>();
        
        this.semaphore = new Semaphore(0, true);
    }
    
    /**
     * @throws AbriException
     * @throws RemoteException
     * @throws NotBoundException
     * @throws MalformedURLException
     * @throws Throwable
     */
    @Override
    public void finalize() throws AbriException, RemoteException, NotBoundException, MalformedURLException, Throwable {
        try {
            deconnecterAbri();
            Naming.unbind(url);
        } finally {
            super.finalize();
        }
    }

    @Override
    public String getUrl() {
        return url;
    }
    
    @Override
    public boolean estConnecte() {
        return abri.estConnecte();
    }

    @Override
    public Annuaire getAnnuaire() {
        return abrisDistants;
    }

    @Override
    public void connecterAbri() throws AbriException, RemoteException, MalformedURLException, NotBoundException, InterruptedException, NoeudCentralException {
        System.out.println("connecterAbri()");
        // Enregistrer dans l'annuaire RMI
        //Enregistrement du contrôleur d'abord (pour accès à la SC)
        Naming.rebind(url, (AbriRemoteInterface) this);
        for (String name : Naming.list(Adresses.archetypeAdresseAbri())) {
            name = "rmi:" + name;
            if (!name.equals(url)) {
                Remote o = Naming.lookup(name);
                if (o instanceof NoeudCentralRemoteInterface) {
                    if (noeudCentral == null) {
                        this.noeudCentralUrl = name;
                        noeudCentral = (NoeudCentralRemoteInterface) o;
                        noeudCentral.enregisterAbri(url);
                        this.controleur.addNoeudCentral(this.noeudCentral);
                        controleur.demanderSectionCritique();
                        semaphore.acquire();
                        noeudCentral.MAJAbris(false,false,url);
                        controleur.quitterSectionCritique();
                    }
                    else {
                        throw new AbriException("Plusieurs noeuds centraux semblent exister.");
                    }
                }
            }
        }
        // Enregistrement de tous les autres abris
        // et notification a tous les autres abris
        for (String name : Naming.list(Adresses.archetypeAdresseAbri())) {
            name = "rmi:" + name;
            if (!name.equals(url)) {
                Remote o = Naming.lookup(name);
                if (o instanceof AbriRemoteInterface) {
                    // Enregistrement de l'abri courant
                    System.out.println(url + ": \tEnregistrement aupres de " + name);
                    try{
                        ((AbriRemoteInterface) o).enregistrerAbri(url, abri.donnerGroupe(), controleurUrl);
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    // Enregistrement d'un abri distant
                    AbriBackend.this.enregistrerAbri(name, ((AbriRemoteInterface) o).signalerGroupe(), (AbriRemoteInterface) o);
                }
            }
        }

        abri.connecter();
    }

    /**
     *
     * @throws AbriException
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws NotBoundException
     */
    @Override
    public void deconnecterAbri() throws AbriException, RemoteException, MalformedURLException, NotBoundException, InterruptedException, NoeudCentralException {
        controleur.demanderSectionCritique();
        semaphore.acquire();
        noeudCentral.MAJAbris(true,false,url);
        for (AbriRemoteInterface distant : abrisDistants.getAbrisDistants().values()) {
            try {
                distant.supprimerAbri(url, controleurUrl);
            } catch (RemoteException ex) {
                Logger.getLogger(AbriBackend.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // Abri
        abri.deconnecter();
        controleur.quitterSectionCritique();
        // noeudCentral
        noeudCentral.supprimerAbri(url);
        noeudCentralUrl = "";
        noeudCentral = null;
        
        // Autres abris
        abrisDistants.vider();

        // Annuaire RMI
        Naming.unbind(url);
    }

    /**
     *
     * @param message
     * @throws RemoteException
     * @throws AbriException
     */
    @Override
    public synchronized void recevoirMessage(modele.Message message) throws RemoteException, AbriException {
        if (!message.getUrlDestinataire().contains(url)) {
            throw new AbriException("Message recu par le mauvais destinataire (" + message.getUrlDestinataire().toString() +  " != " + url + ")");
        }
        System.out.println(url + ": \tMessage recu de " + message.getUrlEmetteur() + " \"" + message.getContenu() + "\"");
        abri.ajouterMessage(message);
    }

    @Override
    public void emettreMessage(String message) throws InterruptedException, RemoteException, AbriException, NoeudCentralException {
        if(this.estConnecte()){
            controleur.demanderSectionCritique();
            semaphore.acquire();
            System.out.println(url + ": \tEntree en section critique");
            System.out.println(url + " est dans le groupe " + abri.donnerGroupe());
            System.out.println(url + ": \tEmission vers " + copains.toString() + ": " + message);
            noeudCentral.modifierAiguillage(url, copains);
            noeudCentral.transmettre(new Message(url, copains, message));
            controleur.quitterSectionCritique();
            System.out.println(url + ": \tSortie de la section critique");
        }
    }

    @Override
    public void enregistrerAbri(String urlDistant, String groupe, AbriRemoteInterface distant) {
        abrisDistants.ajouterAbriDistant(urlDistant, distant);
        if (groupe.equals(abri.donnerGroupe()))
        {
            this.copains.add(urlDistant);
        }
        System.out.println(url + ": \tEnregistrement de l'abri " + urlDistant);
    }

    @Override
    public synchronized void enregistrerAbri(String urlAbriDistant, String groupe, String urlControleurDistant){
        try {
            AbriRemoteInterface o = (AbriRemoteInterface) Naming.lookup(urlAbriDistant);
            AbriBackend.this.enregistrerAbri(urlAbriDistant, groupe, o);
            enregistrerControleur(urlControleurDistant, groupe);
            o.enregistrerControleur(controleurUrl, groupe);
        } catch (NotBoundException ex) {
            Logger.getLogger(AbriBackend.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(AbriBackend.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(AbriBackend.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void supprimerAbri(String urlDistant) {
        System.out.println(url + ": \tOubli de l'abri " + urlDistant);
        abrisDistants.retirerAbriDistant(urlDistant);
        if (copains.contains(urlDistant))
        {
            copains.remove(urlDistant);
        }
    }
    
    @Override
    public synchronized void supprimerAbri(String urlAbriDistant, String urlControleurDistant) {
        try {
            AbriRemoteInterface o = (AbriRemoteInterface) Naming.lookup(urlAbriDistant);
            AbriBackend.this.supprimerAbri(urlAbriDistant);
            supprimerControleur(urlControleurDistant);
            o.supprimerControleur(controleurUrl);
        } catch (NotBoundException ex) {
            Logger.getLogger(AbriBackend.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(AbriBackend.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(AbriBackend.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void enregistrerControleur(String urlDistante, String groupe) {
        controleur.enregistrerControleur(urlDistante, groupe);
    }

    @Override
    public synchronized void supprimerControleur(String urlDistante) {
        controleur.supprimerControleur(urlDistante);
    }

    @Override
    public void recevoirAutorisation() {
        System.out.println("RecevoirAutorisation");
        semaphore.release();
    }
    
    @Override
    public void changerGroupe(String groupe)
    {
        abri.definirGroupe(groupe);
    }
    
    @Override
    public String signalerGroupe() throws RemoteException
    {
        return abri.donnerGroupe();
    }

    @Override
    public void Autorisation() throws RemoteException{
        System.out.println("Autorisation");
        recevoirAutorisation();
    }
}
