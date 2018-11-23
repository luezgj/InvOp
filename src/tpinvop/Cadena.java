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
    private float[][] matrizN;

    public Cadena(Linea l) {
        this.l = l;
        prob= new float[l.getCantMaterias()];
        matrizN = new float[l.getCantMaterias()][l.getCantMaterias()];
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
    
    public void setMatriz(){
        for (int i=0; i < matrizN.length;i++)
            for (int j=0 ; j < matrizN.length; j++)
                matrizN[i][j] = 0;
        for (int i=0; i < matrizN.length ;i++){
            matrizN[i][i] = 1-prob[i];
        }
    }
}
