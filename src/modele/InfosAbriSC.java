package modele;

import controle.AbriRemoteInterface;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tangimallegol on 23/11/2015.
 */
public class InfosAbriSC implements Serializable{
    //Liste des demande de SC en attente (si on demande la SC, on les libère lorsqu'on a fini de l'utiliser
    //protected ArrayList<Abri> lstAbriDemandeEnAttente;
    //Nombre de réponses pour la SC
    protected AbriRemoteInterface abri;
    protected int nbReponses;
    //Nombre de réponses attendues
    protected int nbReponsesAttendues;
    //Est ce que l'abri demande la SC
    protected boolean demandeSC;

    public InfosAbriSC(AbriRemoteInterface abri){
        //this.lstAbriDemandeEnAttente = new ArrayList<Abri>();
        this.nbReponses = 0;
        this.demandeSC = false;
        this.nbReponsesAttendues = 0;
        this.abri = abri;
    }

    public void DemandeSC(int nbAbris){
        try{
            this.demandeSC = true;
            this.nbReponses = 0;
            //On demandera N - 1 réponses pour récupérer la SC (moins notre abri)
            this.nbReponsesAttendues = nbAbris - 1;
        }
        catch(Exception e){
            System.out.print(e.getMessage());
        }

    }

    public int getNbReponses(){
        return this.nbReponses;
    }

    public int getNbReponsesAttendues(){
        return this.nbReponsesAttendues;
    }

    public void setNbReponses(int nbReponses){
        this.nbReponses = nbReponses;
    }

    public void setNbReponsesAttendues(int nbReponsesAttendues){
        this.nbReponsesAttendues = nbReponsesAttendues;
    }

    public boolean canEnterSC(){
        return getNbReponses() == getNbReponsesAttendues();
    }

    public void LibèrerSC(){
        this.demandeSC = false;
    }

    public boolean isDemandeurSC(){ return this.demandeSC; }

    public void setDemandeSC(boolean demandeSC) {
        this.demandeSC = demandeSC;
    }

    public AbriRemoteInterface getAbri(){
        return this.abri;
    }
}
