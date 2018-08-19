package tpinvop;

import java.util.LinkedList;
import java.util.List;

public class main {
    
    public static void main(String[] args) {
       Carrera IngSist=new Carrera("Ingeniero en Sistemas","recursos/Correlatividades.txt");
       AdminBD javaPostgreSQLBasic = new AdminBD();
       
       javaPostgreSQLBasic.connectDatabase();
       //javaPostgreSQLBasic.createTable("noImportaXqNoSeUsa");
       //javaPostgreSQLBasic.loadCSV("./recursos/cursadas.csv");
       //javaPostgreSQLBasic.filterData(206, "2011");
       
       
       Nodo nodoTest= new Nodo();
       nodoTest.add(new Materia(0, "Ciencias de la Computación I", null));
       nodoTest.add(new Materia(1, "Introducción a la Programación II", null));
       nodoTest.add(new Materia(2, "Matemática Discreta", null));
       
       List<Nodo> listaTest= new LinkedList<>();
       listaTest.add(nodoTest);
       
       javaPostgreSQLBasic.createView("vistaNetB", "cursadas", listaTest);
    }
}
