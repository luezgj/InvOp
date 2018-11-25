/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpinvop;

import java.util.List;

/**
 *
 * @author lucho
 */
public class GeneradorInformacion {
    
    Cadena cadena;
    
    public GeneradorInformacion(Cadena cadena){
        this.cadena = cadena;
    }
    
    public static float getPromedioCuatrimestre(int nroNodo){
        
        return 0;
    }
    
    public static float getTotalCuatrimestre(){
        //sumatoria fila 1
        return 0;
    }
    
    public static float getCriticidad(){
        
        return 0;
    }
    
    public static double tiempoEsperadoRama(Cadena cadena){
        float [][] matrizN = cadena.getMatrizN();
        restarMatrizConI(matrizN);
        float [][] matrizResultante = matrizInversa(matrizN);
        //mostrarMatriz(matrizN);
        
        float suma = 0;
        for (int i=0 ; i < matrizResultante.length ; i++){
         //   System.out. print(matrizResultante[0][i] + "  ");
         //   System.out.println("");
            System.out.println(matrizResultante[0][i]+"/"+cadena.getLinea().getDifCuatrimestre(i));
            suma += matrizResultante[0][i]/cadena.getLinea().getDifCuatrimestre(i);
        }
        //System.out.println("");
        //System.out.println(suma);
        return suma;
    }
    
    private static  void mostrarMatriz(float [][] matrizN){
        for (int i=0; i < matrizN.length;i++){
            for (int j=0; j < matrizN.length;j++)
                System.out.print(matrizN[i][j] + " ");
            System.out.println();
        }
    }
    
    private static void restarMatrizConI(float[][] matrizN) {
        for (int i=0; i < matrizN.length; i++){
            if (i == matrizN.length-1){
                matrizN[i][i] = 1 - matrizN[i][i];
            } else{
                matrizN[i][i] = 1-matrizN[i][i];
                matrizN[i][i+1] = -matrizN[i][i+1];
            }
        }
    }
    
    private static float[][] matrizInversa(float[][] matriz) {
        float det=1/determinante(matriz);
        float[][] nmatriz=matrizAdjunta(matriz);
        multiplicarMatriz(det,nmatriz);
        return nmatriz;
    }
    
    private static void multiplicarMatriz(float n, float[][] matriz) {
        for(int i=0;i<matriz.length;i++)
            for(int j=0;j<matriz.length;j++)
                matriz[i][j]*=n;
    }
    
    private static float[][] matrizAdjunta(float [][] matriz){
        return matrizTranspuesta(matrizCofactores(matriz));
    }
    
    private static float[][] matrizTranspuesta(float [][] matriz){
        float[][]nuevam=new float[matriz[0].length][matriz.length];
        for(int i=0; i<matriz.length; i++){
            for(int j=0; j<matriz.length; j++)
                nuevam[i][j]=matriz[j][i];
        }
        return nuevam;
    }
    
    private static float[][] matrizCofactores(float[][] matriz){
        float[][] nm=new float[matriz.length][matriz.length];
        for(int i=0;i<matriz.length;i++) {
            for(int j=0;j<matriz.length;j++) {
                float[][] det=new float[matriz.length-1][matriz.length-1];
                float detValor;
                for(int k=0;k<matriz.length;k++) {
                    if(k!=i) {
                        for(int l=0;l<matriz.length;l++) {
                            if(l!=j){
                                int indice1=k<i ? k : k-1 ;
                                int indice2=l<j ? l : l-1 ;
                                det[indice1][indice2]=matriz[k][l];
                            }
                        }
                    }
                }
                detValor=determinante(det);
                nm[i][j]=detValor * (float)Math.pow(-1, i+j+2);
            }
        }
        return nm;
    }
    
    private static float determinante(float[][] matriz){
        float det;
        if(matriz.length==2){
            det=(matriz[0][0]*matriz[1][1])-(matriz[1][0]*matriz[0][1]);
            return det;
        }
        float suma=0;
        for(int i=0; i<matriz.length; i++){
        float[][] nm=new float[matriz.length-1][matriz.length-1];
            for(int j=0; j<matriz.length; j++){
                if(j!=i){
                    for(int k=1; k<matriz.length; k++){
                        int indice=-1;
                        if(j<i)
                            indice=j;
                        else if(j>i)
                            indice=j-1;
                            nm[indice][k-1]=matriz[j][k];
                    }
                }
            }
            if(i%2==0)
                suma+=matriz[i][0] * determinante(nm);
            else
                suma-=matriz[i][0] * determinante(nm);
        }
        return suma;
    }
    
    
}
