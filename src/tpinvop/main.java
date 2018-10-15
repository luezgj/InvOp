package tpinvop;

import java.util.LinkedList;
import java.util.List;
import tpinvop.Interfaz.VentanaResultados;
import tpinvop.Interfaz.VentanaSimulacion;

public class main {
    
    public static void main(String[] args) {
       Carrera IngSist=new Carrera("Ingeniero en Sistemas","recursos/Correlatividades.txt");
       /*AdminBD db = new AdminBD("cursadas","vista");
       
       db.connectDatabase();
       db.createTable();
       db.loadCSV("./recursos/cursadas.csv");
       db.filterData(206, "2011");

       */
      GeneradorCadena genCadenas = new GeneradorCadena(null);
       
       List<Cadena> cadenas= genCadenas.getCadenas(IngSist);
       
       new VentanaResultados(cadenas).setVisible(true);
       new VentanaSimulacion(cadenas).setVisible(true);
    }
}
