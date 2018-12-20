package tpinvop;

/**
 *
 * @author lucho
 */
public class Cadena {
    private Linea l;
    private float[] prob;
    private float[][] matrizN;

    //Constructor
    public Cadena(Linea l) {
        this.l = l;
        prob= new float[l.getCantMaterias()];
        matrizN = new float[l.getCantMaterias()][l.getCantMaterias()];
    }
    
    //Set the pass probability to a node
    public boolean setProbAprobar(int nroNodo, float probabilidad){
        if(nroNodo<l.getCantMaterias()){
            prob[nroNodo]=probabilidad;
            return true;
        }
        return false;
    }

    //Returns the line associated to this markov chain
    public Linea getLinea() {
        return l;
    }
    
    //Returns the pass probability of a particular node
    public float getProbAprobar(int nroNodo){
        if(nroNodo<l.getCantMaterias()){
            return prob[nroNodo];
        }
        return -1;
    }
    
    //Returns the fail probability of a particular node
    public float getProbDesaprobar(int nroNodo){
        if(nroNodo<l.getCantMaterias()){
            return 1-prob[nroNodo];
        }
        return -1;
    }
    
    //Sets the markov chain with the probability array
    public void setMatriz(){
        for (int i=0; i < matrizN.length;i++)
            for (int j=0 ; j < matrizN.length; j++)
                matrizN[i][j] = 0;
        for (int i=0; i < matrizN.length ;i++){
            if (i == matrizN.length-1){
                matrizN[i][i] = 1-prob[i];
            } else{
                matrizN[i][i] = 1-prob[i];
                matrizN[i][i+1] = prob[i];
            }
        }
    }

    public float[][] getMatrizN() {
        return matrizN;
    }
    
    
}
