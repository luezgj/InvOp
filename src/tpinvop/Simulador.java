package tpinvop;

public class Simulador {
   
	public static final int ROWDIM=10;
	public static final int COLDIM=10;
	
	List<Cadena> cadenas;
	int[ROWDIM][COLDIM] alumnosXNodo;
	int[ROWDIM][COLDIM] cantAprobados;
	int cantAlumnos;
	
	public Simulador(List<Cadena> cadenas, int cantAlumnos) {
		
		this.cadenas = cadenas;
		this.cantAlumnos = cantAlumnos;
		alumnosXNodo[0][0] = cantAlumnos;
	}
	
    //Hacer metodo de simulacion**************************************************
	public void simular(int nLinea) {
		
		inicMatrices();
		
		Cadena c = cadenas.get(Linea);
		int aprobados;
		
		for(int nodo = 0; nodo < DIMROW; nodo++)
			for(int año = i; año < DIMCOL; año++){
				
				aprobados = simularGrupo(c, nodo);
				cantAprobados[nodo][año] = aprobados;
				
				if(nodo+1 < DIMROW && año+1 < DIMCOL) {
					alumnosXNodo[nodo+1][año+1] += aprobados;
					alumnosXNodo[nodo][año+1] += cantAlumnos-aprobados;
				}
			}
		
	}
	
	private int simularGrupo(Cadena c, int nodo) {
		
		int cont = 0;
		
		for(int i = 1; i<= cantAlumnos; i++) {
			
			if(aprobado(c, nodo))
				cont++
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
    	
    	for(int nodo = 0; nodo < DIMROW; nodo++)
			for(int año = i; año < DIMCOL; año++){
				if(nodo == 0)
					alumnosXNodo[nodo][año] = cantAlumnos;
				else {
					alumnosXNodo[nodo][año] = 0;
					cantAprobados[nodo][año] = 0;
				}
			}
    }
    	
}