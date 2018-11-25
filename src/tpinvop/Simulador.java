package tpinvop;

import java.util.List;

public class Simulador {
   
    public static final int ROWDIM=10;
    public static final int COLDIM=10;

    List<Cadena> cadenas;
    int[][] alumnosXNodo=new int[ROWDIM][COLDIM];
    int[][] cantAprobados=new int[ROWDIM][COLDIM];
    int cantAlumnos;

    public Simulador(List<Cadena> cadenas, int cantAlumnos) {
            this.cadenas = cadenas;
            this.cantAlumnos = cantAlumnos;
    }
	

    public void simular(int nLinea) {
            System.out.println("simular llamado");
            inicMatrices();

            Cadena c = cadenas.get(nLinea);

            for(int nodo = 0; nodo < ROWDIM; nodo++)
                    for(int año = nodo; año < COLDIM; año++){
                            int aprobados = simularGrupo(c, nodo, alumnosXNodo[nodo][año]);
                            //Parece fallar tambien el orden en el que estan las materias, parece que el primer nodo es en realidad el que deberia ir ultimo
                            //Probabilidades al reves****
                            System.out.println("Nodo: "+nodo+" - Año: "+año+" - Prob: "+c.getProbAprobar(nodo)+" - Cant: "+aprobados);

                            cantAprobados[nodo][año] = aprobados;
                                
                            System.out.println("ACa estoy");
                            if((nodo+1 < ROWDIM) && (año+1 < COLDIM)) {
                                    alumnosXNodo[nodo+1][año+1] += aprobados;
                                    System.out.println("Mismo nodo, año siguiente: Antes: "+alumnosXNodo[nodo][año+1]);
                                    
                                    alumnosXNodo[nodo][año+1] += alumnosXNodo[nodo][año]-aprobados;
                                    int quedan=alumnosXNodo[nodo][año]-aprobados;
                                    System.out.println("se quedaron: "+ quedan);
                                    System.out.println("Despues: "+alumnosXNodo[nodo][año+1]);
                            }
                    }

    }

    //Añadir un limite referido a la cantidad de alumnos que posee el nodo que se esta utilizando
    private int simularGrupo(Cadena c, int nodo, int cantSim) {

            int cont = 0;

            for(int i = 1; i<= cantSim; i++) {

                    if(aprobado(c, nodo))
                            cont++;
            }

            return cont;
    }

    private boolean aprobado(Cadena c, int nodo) {
        
        double p = Math.random();
        
        if (p < c.getProbAprobar(nodo)) {
            return true;
        }
        
        return false;
    }
    
    public int getCantidad(int nodo,int año){
        return alumnosXNodo[nodo][año];
    }
    
    public int getAprobados(int nodo, int año) {
    	return cantAprobados[nodo][año];
    }
    
    public int getDesaprobados(int nodo, int año) {
    	return alumnosXNodo[nodo][año] - cantAprobados[nodo][año];
    }
    
    private void inicMatrices() {
    	
    	for(int nodo = 0; nodo < ROWDIM; nodo++)
			for(int año = nodo; año < COLDIM; año++){
				if(nodo == 0 && año==0)
					alumnosXNodo[nodo][año] = cantAlumnos;
				else {
					alumnosXNodo[nodo][año] = 0;
					cantAprobados[nodo][año] = 0;
				}
			}
    }
    	
}