package tpinvop;

import java.util.LinkedList;
import java.util.List;


public class GeneradorCadena {

    private AdminBD dbAdmin;
    
    public GeneradorCadena(AdminBD admin){
        dbAdmin=admin;
    }
    
    //Gets from the database the pass percentage for each node
    private Cadena getCadena(Linea linea, Integer año){
        Cadena c=new Cadena(linea);
        int nroNodo=0;
        for(Nodo n: linea){
            c.setProbAprobar(nroNodo, dbAdmin.getPassPercentage(n,año));
            nroNodo++;
        }
        return c;
    }
    
    //Returns all the markov chains from the career (All years)
    public List<Cadena> getCadenas(Carrera carrera){
        List<Linea> lineas=carrera.getLineas();
        List<Cadena> cadenas=new LinkedList<>();
        for(Linea l:lineas){
            Cadena c=getCadena(l, null);
            cadenas.add(c);
        }
        return cadenas;
    }
    
    //Returns all the markov chains from the career (One year only)
    public List<Cadena> getCadenas(Carrera carrera, Integer año){
        List<Linea> lineas=carrera.getLineas();
        List<Cadena> cadenas=new LinkedList<>();
        for(Linea l:lineas){
            Cadena c=getCadena(l, año);
            cadenas.add(c);
        }
        return cadenas;
    }

}
