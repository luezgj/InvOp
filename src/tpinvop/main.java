package InvOp;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class main {
    
    
    public static void main(String[] args) {
       Carrera IngSist=new Carrera("Ingeniero en Sistemas","recursos/Correlatividades.txt");
       AdminBD javaPostgreSQLBasic = new AdminBD();
       javaPostgreSQLBasic.connectDatabase();
       //IngSist.mostrarLineas();
    }
}
