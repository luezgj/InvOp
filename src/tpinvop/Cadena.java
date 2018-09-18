/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpinvop;

/**
 *
 * @author lucho
 */
public class Cadena {
    private Linea l;
    private float[] prob;

    public Cadena(Linea l) {
        this.l = l;
        prob= new float[l.getCantMaterias()];
    }
    
    public boolean setProbAprobar(int nroNodo, float probabilidad){
        if(nroNodo<l.getCantMaterias()){
            prob[nroNodo]=probabilidad;
            return true;
        }
        return false;
    }

    public Linea getLinea() {
        return l;
    }
    
    public float getProbAprobar(int nroNodo){
        if(nroNodo<l.getCantMaterias()){
            return prob[nroNodo];
        }
        return -1;
    }
    
    public float getProbDesaprobar(int nroNodo){
        if(nroNodo<l.getCantMaterias()){
            return 1-prob[nroNodo];
        }
        return -1;
    }
}
