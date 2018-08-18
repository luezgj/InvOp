package tpinvop;

public class main {
    
    public static void main(String[] args) {
       Carrera IngSist=new Carrera("Ingeniero en Sistemas","recursos/Correlatividades.txt");
       AdminBD javaPostgreSQLBasic = new AdminBD();
       
       javaPostgreSQLBasic.connectDatabase();
       javaPostgreSQLBasic.createDB("noImportaXqNoSeUsa");
    }
}
