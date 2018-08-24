package tpinvop;

import java.util.LinkedList;
import java.util.List;


public class GeneradorCadena {

    private AdminBD dbAdmin;
    
    public void GeneradorCadena(AdminBD admin){
        dbAdmin=admin;
    }
    
    private Cadena getCadena(Linea linea){
        Cadena c=new Cadena(linea);
        int nroNodo=0;
        for(Nodo n: linea){
            c.setProbAprobar(nroNodo, dbAdmin.getPassPercentage(n));
            nroNodo++;
        }
        return c;
    }
    
    public List<Cadena> getCadenas(Carrera carrera){
        List<Linea> lineas=carrera.getLineas();
        List<Cadena> cadenas=new LinkedList<>();
        for(Linea l:lineas){
            Cadena c=getCadena(l);
            cadenas.add(c);
        }
        return cadenas;
    }

}
