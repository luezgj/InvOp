package tpinvop;

import java.sql.*;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.postgresql.core.BaseConnection;
import org.postgresql.copy.CopyManager;

//********************************************************************
// AdminDB CLASS: Provides Database conectivity through JDBC API. 
// 
//********************************************************************

public class AdminBD {
    
    Connection con;
    String generalTable;
    String studentsTable;
    String generalPassTable;
    Map<Integer,String> cohortPassTables= new HashMap<>();
    Map<Integer,String> cohortTables= new HashMap<>();
            
    public AdminBD(String tableName, String viewName, String studentsTable) {
        this.generalTable = tableName;
        this.studentsTable = studentsTable;
        this.generalPassTable = viewName;
    }
    
    
    
    /*
    public Connection connectDatabase() {
        
        Connection connection = null;
        
        try {
            
            //***************GENERALIZAR LA CONEXION****************************
            
            // We register the PostgreSQL driver
            // Registramos el driver de PostgresSQL
            try { 
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                InfoMsgBox.errBox("Error al registrar el driver de PostgreSQL: " + ex.getMessage(),
                        "Error");
            }
            
            // Database connect
            // Create user:
            // sudo -u postgres psql postgres
            // create user invop with password '12345';
            // Conectamos con la base de datos
            this.con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/invopdb",
                    "invop", "12345");
            
        } catch (java.sql.SQLException sqle) {
                InfoMsgBox.errBox("COD "+sqle.getErrorCode()+
                                ") "+sqle.getMessage(), "Error de SQL");
        }
        
    }
    
    */
    public void connectDatabase() {
        try {
            try { 
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
            }
            String urlDatabase =  "jdbc:postgresql://localhost:5432/invopdb"; 
            con = DriverManager.getConnection(urlDatabase,  "invop", "12345");
            System.out.println("Conectado");
            } 
        catch (java.sql.SQLException sqle) {
            System.out.println("Error: " + sqle);
        }
    }
    
    public void createCoursedTable(){  // Creates a table with the specified name
        
        // Sql statement for creating the base table
        
        String sql = "CREATE TABLE IF NOT EXISTS "+ generalTable  +
                "(carrera varchar,"
                + "plan varchar(4),"
                + "legajo int,"
                + "materia int,"
                + "fecha_regularidad date,"
                + "resultado char(1),"
                + "nota numeric(4,2),"
                + "origen varchar,"
                + "nombre varchar);";
        
        Statement stmt;
        
        // Executing SQL statement through DBMS 
        try{
            
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("Tabla cursadas creada correctamente");
            
        }
        catch(SQLException sqle){
            InfoMsgBox.errBox(sqle.getMessage(), "Error de SQL");
            System.out.println("ERROR2");
        }
        
    }
    
    public void createStudentsTable(){  // Creates a table with the specified name
        
        // Sql statement for creating the base table
        
        String sql = "CREATE TABLE IF NOT EXISTS "+ studentsTable  +
                "(carrera int,"
                + "legajo int,"
                + "fecha_ingreso date);";
        
        Statement stmt;
        
        // Executing SQL statement through DBMS 
        try{
            
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("Tabla alumnos creada correctamente");
            
        }
        catch(SQLException sqle){
            InfoMsgBox.errBox(sqle.getMessage(), "Error de SQL");
            System.out.println("ERROR3");
        }
        
    }
    
    //Creates a database with the specified name
    public void createDB(String dbName){ 
        
        try{
            
            if(!con.isClosed()){
            
                String sql = "CREATE DATABASE " + dbName;

                try{
                    Statement stmt = con.createStatement();
                    stmt.execute(sql);
                }
                catch(SQLException sqle){
                    InfoMsgBox.errBox(sqle.getMessage(), "Error de SQL");
                }
            }
            else
                InfoMsgBox.infoBox("La conexion con la base de datos ha sido cerrada, por favor reconectar."
                        ,"Atencion");
        }
        catch(SQLException sqle){
            InfoMsgBox.errBox(sqle.getMessage(), "Error de DBMS");
        }
    }
    
    //Loads a CSV file into the DB
    public void loadCoursedCSV(String dirA){
        
        try{
            if(!con.isClosed()){
                
                String sql = "copy "+ generalTable +" FROM stdin DELIMITER ',' CSV header";

                BaseConnection pgcon = (BaseConnection)con;
                CopyManager mgr = new CopyManager(pgcon);
                
                try{
                    Reader in = new BufferedReader(new FileReader(new File(dirA)));
                    long rowsaffected  = mgr.copyIn(sql, in);

                    System.out.println("CSV cargado correctamente. Filas copiadas: " + rowsaffected);
                }
                catch(IOException io_exept){
                    InfoMsgBox.errBox("Error de entrada/salida: "+io_exept.getMessage(), "Error");
                }
            
            }
        }
        catch(SQLException sqle){
        
            InfoMsgBox.errBox(sqle.getMessage(), "Error de DBMS");
        }
        
    }
    
    //Loads a CSV file into the DB
    public void loadStudentsCSV(String dirA){
        
        try{
            if(!con.isClosed()){
                
                String sql = "copy "+ studentsTable +"(carrera,legajo,fecha_ingreso) FROM stdin DELIMITER ',' CSV header";

                BaseConnection pgcon = (BaseConnection)con;
                CopyManager mgr = new CopyManager(pgcon);
                
                try{
                    //String comando="awk -F, '{print $1, $2, $4}' "+ dirA;
                    //String comando="cut -d ',' -f 1,2,4 "+ dirA;
                    String comando="cat "+ dirA;
                    System.out.println(comando);
                    Process p = Runtime.getRuntime().exec(comando);
                    
                    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    
                    /*
                    String s = null;
                    System.out.println("LEYENDO LINEAS DE ALUMNOS");
                    while ((s=in.readLine())!=null)
                    {

                    System.out.println(s);
                    }*/
                    
                    long rowsaffected  = mgr.copyIn(sql,in);

                    System.out.println("CSV cargado correctamente. Filas copiadas: " + rowsaffected);
                }
                catch(Exception exept){
                    InfoMsgBox.errBox("Error : "+exept.getMessage(), "Error");
                }
                
            
            }
        }
        catch(SQLException sqle){
        
            InfoMsgBox.errBox(sqle.getMessage(), "Error de DBMS");
        }
        
    }
    
    //Deletes the not useful data from table
    public void filterCoursedData(int carrera, String plan){
        
        try{
            
            if(!con.isClosed()){
                String sql = "DELETE FROM "+ generalTable +
                        " WHERE carrera !='" + carrera +"' or plan != '" + plan + 
                        "' or origen='Cursada Equivalente' or origen='Equivalencia'; ";
                try{
                    Statement stmt = con.createStatement();
                    stmt.execute(sql);
                }
                catch(SQLException sqle){
                    InfoMsgBox.errBox(sqle.getMessage(), "Error de SQL");
                    System.out.println("ERROR4");
                }
            }
            else
                InfoMsgBox.infoBox("La conexion con la base de datos ha sido cerrada, por favor reconectar."
                        ,"Atencion");
        }
        catch(SQLException sqle){
            InfoMsgBox.errBox(sqle.getMessage(), "Error de DBMS");
        }
    }
    
    public void filterStudentsData(int carrera){
        
        try{
            
            if(!con.isClosed()){
                String sql = "DELETE FROM "+ studentsTable +
                        " WHERE carrera !='" + carrera +"';";
                try{
                    Statement stmt = con.createStatement();
                    stmt.execute(sql);
                }
                catch(SQLException sqle){
                    InfoMsgBox.errBox(sqle.getMessage(), "Error de SQL");
                    System.out.println("ERROR5");
                }
            }
            else
                InfoMsgBox.infoBox("La conexion con la base de datos ha sido cerrada, por favor reconectar."
                        ,"Atencion");
        }
        catch(SQLException sqle){
            InfoMsgBox.errBox(sqle.getMessage(), "Error de DBMS");
        }
    }
    
    private String filterCohort(Integer añocohorte){
        String nombreTabla=generalTable;
        if (añocohorte!=null && !cohortTables.containsKey(añocohorte)){
            nombreTabla="Cohorte"+añocohorte;
            
            String str = "CREATE TABLE " + nombreTabla + " AS\n"
                        +"SELECT c.carrera, plan, c.legajo, materia, fecha_regularidad, resultado, nota, origen, nombre\n"
                        +"FROM "+generalTable +" c INNER JOIN "+studentsTable+" a"
                        +"    ON(c.legajo = a.legajo)\n"
                        +"WHERE date_part('year',a.fecha_ingreso) = " + añocohorte;
            System.out.println(str);
            try{
                System.out.println("");
                Statement stmt = con.createStatement();
                stmt.execute(str);
                
            }
            catch(SQLException sqle){
                InfoMsgBox.errBox(sqle.getMessage(), "Error de DBMS");
                System.out.println("Error filtrando cohorte");
            }
            
            cohortTables.put(añocohorte, nombreTabla);
        }
        return nombreTabla;
    }
    
    //Creates a SQL table from the table with pass and fail count from each year and subject
    private void generatePassPerSubject(String tableUsed, String passTableDestination){
        try{

            if(!con.isClosed()){
                
                System.out.println("PassPerSubject: ");
                System.out.println("PassPerSubject: "+passTableDestination);
                System.out.println("TableUsed: "+tableUsed);
                
                String sql= "CREATE TABLE "+ passTableDestination +" AS SELECT coalesce(ap.año, des.año) as año, \n" +
"                    coalesce(ap.nombre, des.nombre) as nombre, \n" +
"                    coalesce(aprobados,0) as aprobados, \n" +
"                    coalesce(desaprobados,0) as desaprobados \n" +
"                    FROM\n" +
"                    (SELECT nombre, extract(year from fecha_regularidad) as año , count(*) as aprobados \n" +
"	                    FROM "+ tableUsed +" \n" +
"	                    WHERE resultado='A' or resultado='P' \n" +
"	                    GROUP BY nombre, año) AS ap \n" +
"                    FULL JOIN \n" +
"                    (SELECT nombre, extract(year from fecha_regularidad) as año , count(*) as desaprobados \n" +
"	                    FROM "+ tableUsed +" \n" +
"	                    WHERE resultado='R' or resultado='U' \n" +
"	                    GROUP BY nombre, año) AS des \n" +
"                    ON (ap.nombre=des.nombre and ap.año=des.año)";

                try{
                    Statement stmt = con.createStatement();
                    stmt.execute(sql);
                }
                catch(SQLException sqle){
                    InfoMsgBox.errBox(sqle.getMessage(), "Error de SQL");
                    System.out.println("ERROR1");
                }
            }
            else
                InfoMsgBox.infoBox("La conexion con la base de datos ha sido cerrada, por favor reconectar."
                        ,"Atencion");
        }
        catch(SQLException sqle){
            InfoMsgBox.errBox(sqle.getMessage(), "Error de DBMS");
        }
    }
    
    //Updates the table of pass created with data asociated to multiple subject node in lines
    private void generatePassPerNode(List<Nodo> l, String tableUsed, String passTableDestination){
        try{

            if(!con.isClosed()){

                //Create procedure to update view
                String sql = "CREATE OR REPLACE FUNCTION createNodeView(nodename character varying,subjectnames character varying[]) RETURNS void AS $$\n" +
"                BEGIN \n" +
"                INSERT INTO "+ passTableDestination +
"                SELECT coalesce(t.año, a.año) as año, nodename as nombre , coalesce(a.aprobados, 0) as aprobados, coalesce(t.total-aprobados, t.total) as desaprobados\n" +
"                FROM\n" +
"                --Sacamos la cuenta de los que cursaron alguna materia\n" +
"                                (SELECT año,count(*) as total FROM\n" +
"                                        (SELECT extract(year from fecha_regularidad) as año, legajo\n" +
"                                        FROM "+ tableUsed +
"                                        WHERE nombre = ANY (subjectnames)\n" +
"                                        GROUP BY año, legajo) as ax1\n" +
"                                GROUP BY año) AS t\n" +
"                        FULL JOIN \n" +
"                                --Sacamos la cuenta de los que aprobaron todas las materias\n" +
"                               (SELECT año, count(*) as aprobados FROM\n" +
"                                   (SELECT MAX(año) as año, count(*) AS cantidad FROM\n" +
"                                       (SELECT DISTINCT extract(year from fecha_regularidad) as año, legajo, nombre\n" +
"                                       FROM "+ tableUsed + 
"                                       WHERE nombre = ANY(subjectnames) and resultado='A') as x\n" +
"                                   GROUP BY legajo) as y\n" +
"                               WHERE cantidad = array_length(subjectnames, 1)\n" +
"                               GROUP BY año) as a\n" +
"                        ON (t.año=a.año);\n" +
"                END;\n" +
"                $$ LANGUAGE plpgsql;";
                
                try{
                    Statement stmt = con.createStatement();
                    stmt.execute(sql);
                    System.out.println("Funcion creada");
                }
                catch(SQLException sqle){
                    InfoMsgBox.errBox(sqle.getMessage(), "Error de SQL");
                    System.out.println("ERROR6");
                }
                
                
                //Execute procedure for each node
                for(Nodo n: l){
                    try {
                        String[] materias= new String[n.getCantMaterias()];
                        int i=0;
                        for(Materia m:n){
                            materias[i]=m.getNombre();
                            i++;
                        }
                        java.sql.Array arrayMaterias= con.createArrayOf("varchar", materias);
                        
                        sql="SELECT * FROM createNodeView (?, ?)";
                        PreparedStatement pstmt = con.prepareStatement(sql);
                        pstmt.setString(1,n.getNombre());
                        pstmt.setArray(2, arrayMaterias);
                        pstmt.execute();
                        System.out.print("Nodo calculado");
                    } catch (SQLException sqle) {
                        InfoMsgBox.errBox(sqle.getMessage(), "Error de SQL");
                        System.out.println("ERROR7");
                    }
                }
            }
            else
                InfoMsgBox.infoBox("La conexion con la base de datos ha sido cerrada, por favor reconectar."
                        ,"Atencion");
        }
        catch(SQLException sqle){
            InfoMsgBox.errBox(sqle.getMessage(), "Error de DBMS");
        }
    }
    
    //public method for constructing the data view
    public void generatePassTable(List<Nodo> l, Integer añoCohorte){
        System.out.println("");
        System.out.println("generatePassTable: "+ añoCohorte);
        System.out.println("");
        System.out.println("LLAMO A FILTERCOHORT");
        String origen=this.filterCohort(añoCohorte);
        String destino=creationPassTable(añoCohorte);
        System.out.println("LLAMO A GENERATEPASSPERSUBJECT");
        this.generatePassPerSubject(origen,destino);
        System.out.println("LLAMO A GENERATEPASSPERNODE");
        System.out.println("cantidad nodos: "+l.size());
        this.generatePassPerNode(l,origen,destino);
    }

    //Return pass pergentage of a node considering only the cohort year=año // año== null dont consider cohort
    public float getPassPercentage(Nodo n, Integer añoCohorte){
        String tableUsed=getPassTable(añoCohorte);
        System.out.println("Saco porcentaje del nodo: "+n.getNombre()+"- año: "+añoCohorte+"- Tabla:"+tableUsed);
        if(tableUsed==null){
            return -1;
        }
        
        String sql="SELECT aprobados,aprobados+desaprobados as total FROM "+ tableUsed +  //Cambiar por media
        " WHERE nombre='" + n.getNombre()+"'";
        System.out.println(sql);
        List<Float> porcentajeAprobados= new LinkedList();
        List<Integer> totalPorAño= new LinkedList();
        int totalNodo=0;
        float passPercentage=0f;
        try {
            Statement stmt;
            stmt = con.createStatement();
            ResultSet rs= stmt.executeQuery(sql);
            while(rs.next()){
                Float aprobados=rs.getFloat(1);
                int total=rs.getInt(2);
                porcentajeAprobados.add(aprobados/total);
                totalPorAño.add(total);
                totalNodo+=total;
            }
            for(int i=0;i<porcentajeAprobados.size();i++){
                float ponderacionAño=(float)(totalPorAño.get(i))/totalNodo;
                passPercentage+=porcentajeAprobados.get(i)*ponderacionAño;
            }
        } catch (SQLException sqle) {
            System.out.println("ERROR8");
            InfoMsgBox.errBox(sqle.getMessage(), "Error de SQL");
        }
        System.out.println("Porcentaje: "+passPercentage);
        return passPercentage;
    }
    
    public boolean dbOK(){
        try{
            return !con.isClosed();
        }
        catch(SQLException sqle){
            InfoMsgBox.errBox(sqle.getMessage(), "Error de DBMS");
        }
        
        return false;
    }
    
    private String getPassTable(Integer añoCohorte){
        if (añoCohorte==null){
            return generalPassTable;
        } else {
            return cohortPassTables.get(añoCohorte);
        }
    }
    
    private String creationPassTable(Integer añoCohorte){
        if (añoCohorte==null){
            return generalPassTable;
        } else {
            String passTable= new String("pass"+añoCohorte);
            cohortPassTables.put(añoCohorte, passTable);
            return passTable;
        }
    }
    
}
