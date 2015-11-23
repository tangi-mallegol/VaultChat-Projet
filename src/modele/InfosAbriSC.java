package modele;

import java.util.ArrayList;

/**
 * Created by tangimallegol on 23/11/2015.
 */
public class InfosAbriSC {
    //Liste des demande de SC en attente (si on demande la SC, on les libère lorsqu'on a fini de l'utiliser
    ArrayList<Abri> lstAbriDemandeEnAttente;
    //Nombre de réponses pour la SC
    int nbReponses;
    //Nombre de réponses attendues
    int nbReponsesAttendues;
    //Est ce que l'abri demande la SC
    boolean demandeSC;

    public InfosAbriSC(){
        this.lstAbriDemandeEnAttente = new ArrayList<Abri>();
        this.nbReponses = 0;
        this.demandeSC = false;
    }

    public void DemandeSC(int nbAbris){
        this.demandeSC = true;
        this.nbReponses = 0;
        //On demandera N - 1 réponses pour récupérer la SC (moins notre abri)
        this.nbReponsesAttendues = nbAbris - 1;
    }

    public boolean AddReponse(){
        this.nbReponses = nbReponses + 1;
        if(this.nbReponses == this.nbReponsesAttendues)
            return true;
        return false;
    }

    public void LibèrerSC(){
        this.demandeSC = false;
    }


}
