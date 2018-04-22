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
            List<Float> porcentajesAp=dbAdmin.getPassPercentage(n);
            c.setProbAprobar(nroNodo, getPromedio(porcentajesAp));
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
    
    private static float getPromedio(List<Float> lista){
        float suma=0f;
        int count=0;
        for(Float f: lista){
            count++;
            suma+=f;
        }
        return (suma/(count==0?1:count));
    }

}
