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
       Carrera IngSist=new Carrera("Ingeniero en Sistemas","recursos/Correlatividades.txt");
       
       AdminBD db = new AdminBD("cursadas","vista","alumnos");

       
       db.connectDatabase();
       db.createCoursedTable();
       db.createStudentsTable();
       db.loadCoursedCSV("./recursos/cursadas.csv");
       db.loadStudentsCSV("./recursos/alumnosbien.csv");
       db.filterCoursedData(206, "2011");
       db.filterStudentsData(206);

       db.generatePassTable(IngSist.getNodos(), null);
      
      Map<Integer, Cohorte> cohortes= new HashMap();
      GeneradorCadena genCadenas = new GeneradorCadena(db);
       
      for (int año=_AÑO_PRIMER_COHORTE;año<=_AÑO_ULTIMA_COHORTE;año++){
        db.generatePassTable(IngSist.getNodos(), año);
        System.out.println("Llamo a getCadenas");
        List<Cadena> cadenas= genCadenas.getCadenas(IngSist,año);
        Cohorte cohorte= new Cohorte(cadenas, año);
        cohortes.put(año, cohorte);
      }
       new VentanaResultados(cohortes.get(_AÑO_PRIMER_COHORTE).getCadenas()).setVisible(true);
       new VentanaSimulacion(cohortes.get(_AÑO_PRIMER_COHORTE),1).setVisible(true);
    }
}
