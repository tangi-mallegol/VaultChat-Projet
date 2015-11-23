package controle;

import modele.InfosAbriSC;
import modele.NoeudCentral;

/**
 * Created by tangimallegol on 23/11/2015.
 */
public class NouveauControleur implements ControleurInterface {

    AbriLocalInterface abri;
    String url;
    NoeudCentralRemoteInterface NoeudCentral;
    InfosAbriSC infosAbri;

    public NouveauControleur(String url, AbriLocalInterface abri, NoeudCentralRemoteInterface NoeudCentral){
        this.url = url;
        this.abri = abri;
        this.NoeudCentral = NoeudCentral;
        this.infosAbri = new InfosAbriSC();
    }

    @Override
    public void demanderSectionCritique() {
        System.out.println(this.url + ": \tDemande de section critique enregistr�e");
        infosAbri.DemandeSC(this.NoeudCentral.getNbAbris());
        if(abri.estConnecte() && this.NoeudCentral != null){

        }
    }

    public void addNoeudCentral(NoeudCentralRemoteInterface noeudCentral){
        this.NoeudCentral = noeudCentral;
    }

    @Override
    public void signalerAutorisation() {
        System.out.println(this.url + ": \tSignalement de l'autorisation");

    }

    @Override
    public void quitterSectionCritique() {
        System.out.println(this.url + ": \tFin de section critique");


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
