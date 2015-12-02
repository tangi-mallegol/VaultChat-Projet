package controle;

import modele.InfosAbriSC;
import modele.NoeudCentral;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * Created by tangimallegol on 23/11/2015.
 */
public class NouveauControleur implements ControleurInterface {

    protected AbriLocalInterface abri;
    protected String url;
    protected NoeudCentralRemoteInterface NoeudCentral;

    public NouveauControleur(String url, AbriLocalInterface abri, NoeudCentralRemoteInterface NoeudCentral){
        this.url = url;
        this.abri = abri;
        this.NoeudCentral = NoeudCentral;
    }

    @Override
    public void demanderSectionCritique() {
        try{
            System.out.println(NoeudCentral);
            int nb_abris = this.NoeudCentral.getNbAbris();
            this.NoeudCentral.demanderSC((AbriRemoteInterface)this.abri);
            //L'abri recevra un message recevoirAutorisation
        }
        catch(RemoteException e){
            System.out.println(e.getMessage());
        }
    }

    public void addNoeudCentral(NoeudCentralRemoteInterface noeudCentral){
        this.NoeudCentral = noeudCentral;
    }

    @Override
    public void signalerAutorisation() {
        abri.recevoirAutorisation();
    }

    @Override
    public void quitterSectionCritique() {
        System.out.println(this.url + ": \tFin de section critique");
        try{
            System.out.println(this.NoeudCentral);
            this.NoeudCentral.libererSC((AbriRemoteInterface) this.abri);
        }
        catch(RemoteException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void enregistrerControleur(String urlDistant, String groupe) {
        System.out.println(this.url + ": \tEnregistrement du controleur " + urlDistant);
    }

    @Override
    public void supprimerControleur(String urlDistant) {
        System.out.println(this.url + ": \tSuppression du controleur " + urlDistant);
    }
}
