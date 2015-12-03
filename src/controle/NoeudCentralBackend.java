/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import modele.*;

/**
 *
 * @author Gwenole Lecorve
 * @author David Guennec
 */
public class NoeudCentralBackend extends UnicastRemoteObject implements NoeudCentralRemoteInterface {

    protected String url;
    protected NoeudCentral noeudCentral;
    protected Annuaire abris;
    protected HashMap<AbriRemoteInterface,InfosAbriSC> infosAbrisSC;


    public NoeudCentralBackend(String _url) throws RemoteException, MalformedURLException {
        this.url = _url;
        noeudCentral = new NoeudCentral();
        abris = new Annuaire();
        this.infosAbrisSC = new HashMap<AbriRemoteInterface,InfosAbriSC>();
        Naming.rebind(url, (NoeudCentralRemoteInterface) this);
    }

    @Override
    public void finalize() throws RemoteException, NotBoundException, MalformedURLException, Throwable {
        try {
            Naming.unbind(url);
        } finally {
            super.finalize();
        }
    }

    public NoeudCentral getNoeudCentral() {
        return noeudCentral;
    }

    public Annuaire getAnnuaire() {
        return abris;
    }

    @Override
    public int getNbAbris(){
        System.out.println("GetNbAbris Début de la fonction");
        if(abris != null)
            return abris.getNbAbris();
        return 0;
    }

    @Override
    public synchronized void modifierAiguillage(String depuisUrl, ArrayList<String> versUrl) throws RemoteException, NoeudCentralException {
        
        System.out.print(url + ": \tReconfiguration du r�seau de " + depuisUrl + " vers ");
        Iterator<String> itr = versUrl.iterator();
        while (itr.hasNext()) {
            System.out.print(itr.next());
        }
        System.out.print("\n");
        
        noeudCentral.reconfigurerAiguillage(depuisUrl, versUrl);
    }

    @Override
    public synchronized void transmettre(Message message) throws RemoteException, AbriException, NoeudCentralException {
        try {
            noeudCentral.demarrerTransmission();
            
            System.out.println(url + ": \tTransmission du message \"" + message.toString() + "\"");
            
            ArrayList<String> abrisCible = noeudCentral.getVersUrl();
            Iterator<String> itr = abrisCible.iterator();
            
            while (itr.hasNext()) {
                AbriRemoteInterface c = abris.chercherUrl(itr.next());
                c.recevoirMessage(message);
            }
        } catch (RemoteException ex) {
            throw ex;
        } catch (AbriException ex) {
            throw ex;
        } finally {
            noeudCentral.stopperTransmission();
        }
    }

    @Override
    public synchronized void enregisterAbri(String urlAbriDistant) throws RemoteException, NotBoundException, MalformedURLException {
        System.out.println(url + ": \tEnregistrement de l'abri dans l'annuaire " + urlAbriDistant);
        AbriRemoteInterface abriDistant = (AbriRemoteInterface) Naming.lookup(urlAbriDistant);
        this.infosAbrisSC.put(abriDistant, new InfosAbriSC(abriDistant));
    }

    @Override
    public synchronized void supprimerAbri(String urlAbriDistant) throws RemoteException, NotBoundException, MalformedURLException {
        System.out.println(url + ": \tSuppression de l'abri de l'annuaire " + urlAbriDistant);
        AbriRemoteInterface abriDistant = (AbriRemoteInterface) Naming.lookup(urlAbriDistant);

    }

    public synchronized void MAJAbris(boolean suppression, boolean suppressionAbriSC, String urlAbriDistant) throws RemoteException, NotBoundException, MalformedURLException{
        AbriRemoteInterface abriDistant = (AbriRemoteInterface) Naming.lookup(urlAbriDistant);
        if(suppression){
            abris.retirerAbriDistant(urlAbriDistant);
            this.infosAbrisSC.remove(abriDistant);
        }

        MajNbAbris(suppression,suppressionAbriSC,abriDistant,urlAbriDistant);
    }

    public void MajNbAbris(boolean suppression, boolean suppressionAbriSC, AbriRemoteInterface abriDistant, String urlAbriDistant){
        LinkedList<InfosAbriSC> lstInfosAbriSC = new LinkedList<InfosAbriSC>(this.infosAbrisSC.values());
        for(InfosAbriSC info : lstInfosAbriSC){
            if(info.getAbri() == abriDistant)
                lstInfosAbriSC.remove(info);
        }
        if(suppression){
            //Prend en paramètre un booléen suppressionAbriSC qui indique si on supprime un abri qui était en SC ou non
            // Si oui, on décrémente seulement le nombre d'abris
            // Si non, on décrémente le nombre d'abris mais aussi le nombre d'autorisation (puisqu'il en a donnée une)
            for (InfosAbriSC infosAbriSC: lstInfosAbriSC) {
                if (infosAbriSC.isDemandeurSC()) {
                    infosAbriSC.setNbReponsesAttendues(infosAbriSC.getNbReponsesAttendues() - 1);
                    if(infosAbriSC.getNbReponses() == infosAbriSC.getNbReponsesAttendues()){
                        //On envoie l'autorisation à l'AbriRemoteInterface
                        System.out.println("AUTORISATION SC DUE A LIBERATION SC D'UN ABRI SUPPRIME");
                        try{
                            AbriRemoteInterface abri_ = infosAbriSC.getAbri();
                            abri_.Autorisation();
                        }catch(RemoteException e){
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }

            /*else{
                for (InfosAbriSC infosAbriSC: lstInfosAbriSC) {
                    if(infosAbriSC.isDemandeurSC()){
                        infosAbriSC.setNbReponsesAttendues(infosAbriSC.getNbReponsesAttendues() - 1);
                        infosAbriSC.setNbReponses(infosAbriSC.getNbReponses() - 1);
                    }
                }
            }*/
        }else{
            //Si on rajoute un abri, alors on incrémente le nombre d'abri et le nombre de réponses (si il fait une demande de SC il sera donc placé à la fin de la liste)
            abris.ajouterAbriDistant(urlAbriDistant, abriDistant);
            System.out.println(this.abris.getAbrisDistants().values().size());
            for (InfosAbriSC infosAbriSC: lstInfosAbriSC) {
                if(infosAbriSC.isDemandeurSC()){
                    infosAbriSC.setNbReponses(infosAbriSC.getNbReponses() + 1);
                    infosAbriSC.setNbReponsesAttendues(infosAbriSC.getNbReponsesAttendues() + 1);
                }
            }
        }

        //Si on rajoute un abri, on modifie le nombre d'abris dans les Processus demandeur et on leur donne l'autorisation de cet abri
    }

    public synchronized void demanderSC(AbriRemoteInterface abri){
        int nb_abri_non_demandeur_SC = 0;
        System.out.println(this.infosAbrisSC.values().size());
        InfosAbriSC info = this.infosAbrisSC.get(abri);
        if(!info.isDemandeurSC()){
            info.setNbReponsesAttendues(this.infosAbrisSC.values().size() - 1);
            info.setDemandeSC(true);
            for (InfosAbriSC infosAbriSC: this.infosAbrisSC.values()) {
                if (!infosAbriSC.isDemandeurSC()) {
                    nb_abri_non_demandeur_SC++;
                }
            }
            System.out.println(nb_abri_non_demandeur_SC);
            System.out.print(this.infosAbrisSC.values().size());
            info.setNbReponses(nb_abri_non_demandeur_SC);
            if(info.getNbReponses() == info.getNbReponsesAttendues()){
                //On envoie l'autorisation à l'AbriRemoteInterface
                System.out.println("AUTORISATION SC");
                try{
                    abri.Autorisation();
                }catch(RemoteException e){
                    System.out.println(e.getMessage());
                }
            }
        }

    }

    public synchronized void libererSC(AbriRemoteInterface abri){
        InfosAbriSC info = this.infosAbrisSC.get(abri);
        info.setDemandeSC(false);
        for (InfosAbriSC infosAbriSC: this.infosAbrisSC.values()) {
            if(infosAbriSC.isDemandeurSC()){
                infosAbriSC.setNbReponses(infosAbriSC.getNbReponses() + 1);
                if(infosAbriSC.getNbReponses() == infosAbriSC.getNbReponsesAttendues()){
                    //On envoie l'autorisation à l'AbriRemoteInterface
                    System.out.println("AUTORISATION SC DUE A LIBERATION SC");
                    try{
                        AbriRemoteInterface abri_ = infosAbriSC.getAbri();
                        abri_.Autorisation();
                    }catch(RemoteException e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}
