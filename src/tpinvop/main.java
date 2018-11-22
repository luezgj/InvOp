package tpinvop;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import tpinvop.Interfaz.VentanaResultados;
import tpinvop.Interfaz.VentanaSimulacion;

public class main {
    public static final int _AÑO_PRIMER_COHORTE=2011;
    public static final int _AÑO_ULTIMA_COHORTE=2017;
    public static void main(String[] args) {
       Carrera IngSist=new Carrera("recursos/Correlatividades.txt");
       
                GeneradorCadena genCadenas = new GeneradorCadena(null);
            
                new VentanaResultados(genCadenas,IngSist).setVisible(true);
       /*         
       AdminBD db = new AdminBD("cursadas","vista");
       
       db.connectDatabase();
       db.createTable();
       db.loadCSV("./recursos/cursadas.csv");
       db.filterData(206, "2011");
*//*
       
      Map<Integer, Cohorte> cohortes= new HashMap();
    //  GeneradorCadena genCadenas = new GeneradorCadena(null);
       
      for (int año=_AÑO_PRIMER_COHORTE;año<=_AÑO_ULTIMA_COHORTE;año++){
        List<Cadena> cadenas= genCadenas.getCadenas(IngSist,año);
        Cohorte cohorte= new Cohorte(cadenas, año);
        cohortes.put(año, cohorte);
      }
       //new VentanaResultados(cadenas).setVisible(true);
       new VentanaSimulacion(cohortes.get(_AÑO_PRIMER_COHORTE),1).setVisible(true);*/
    }
}
