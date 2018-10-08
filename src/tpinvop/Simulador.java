package tpinvop;

public class Simulador {
   
    //Hacer metodo de simulacion**************************************************
    
    public static int primerSimbolo(float v0[]) {
        float v0Acum[] = probAcumuladasVector(v0);
        double p = Math.random();
        for (int i = 0; i < v0Acum.length; i++) {
            if (p < v0Acum[i]) {
                return i;
            }
        }
        return -1;
    }
    
    public static Boolean converge(float ant, float act, float COTA) {
        return (Math.abs(ant - act) < COTA);
    }

    public static float[] probAcumuladasVector(float[] prob) {
        float probAcum[] = new float[prob.length];
        probAcum[0] = prob[0];
        for (int i = 1; i < prob.length; i++) {
            probAcum[i] = probAcum[i - 1] + prob[i];
        }
        return probAcum;
    }
    
    static public boolean ConvergeVector(float[] probActual, float[] probAnterior, float epsilon) {
        for (int i = 0; i < probActual.length; i++) {
            if (!(Math.abs(probActual[i] - probAnterior[i]) < epsilon)) {
                return false;
            }
        }
        return true;
    }

    static public float[][] probAcumuladasMatriz(float[][] prob) // leemos por columna, es decir la columna 1 da las probabilidades de pasar a otro estado dado el estado 1
    {
        float probTransicionEstadoAcum[][] = new float[prob.length][prob[0].length];
        //CopiarVector(prob[0], probTransicionEstadoAcum[0]);
        for (int i = 1; i < prob.length; i++) {
            for (int j = 0; j < prob[i].length; j++) {
                probTransicionEstadoAcum[i][j] = probTransicionEstadoAcum[i - 1][j] + prob[i][j];
            }
        }
        return probTransicionEstadoAcum;
    }
    
    
}
