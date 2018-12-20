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
    boolean truncate;
    String generalTable;
    String studentsTable;
    String generalPassTable;
    Map<Integer,String> cohortPassTables= new HashMap<>();
    Map<Integer,String> cohortTables= new HashMap<>();
            
    public AdminBD(String tableName, String viewName, String studentsTable, boolean truncate) {
        this.generalTable = tableName;
        this.studentsTable = studentsTable;
        this.generalPassTable = viewName;
        this.truncate = truncate;           //This value is used for deleting all tables if its true
    }
    
    //Connect to database and gets the current tables, if truncate is true drops all
    public void connectDatabase() {
        try {
            try { 
                //Loads DB driver
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
            }
            
            //Sets the url of the database and starts the connection
            String urlDatabase =  "jdbc:postgresql://localhost:5432/invopdb"; 
            con = DriverManager.getConnection(urlDatabase,  "invop", "12345");
            System.out.println("Conectado");
            
            //If the variable truncate is false -> gets all tables from database
            if(!truncate){
                this.updateTableList();
                System.out.println("Cargada lista de tablas satisfactoriamente");
            }
            //If not it drops all tables
            else{
                this.dropTables();
            }
           
            } 
        catch (java.sql.SQLException sqle) {
            InfoMsgBox.errBox(sqle.getMessage(), "Error!");
        }
        
    }
    
    //Gets all data from csv files and puts in the database, then applies the filtering
    public void getData(String pathCoursed, String pathStudents, int cod, String plan){
        
        //Creates subjects and students table 
        createCoursedTable();
        createStudentsTable();
        //Put the csv data into the tables
        loadCoursedCSV(pathCoursed);
        loadStudentsCSV(pathStudents);
        //Filter all the unnecesary data from the tables
        filterCoursedData(cod, plan);
        filterStudentsData(cod);
    }
    
    public void createCoursedTable(){  // Creates a table with the specified name
        
        if(!existsTable(generalTable)){
            // Sql statement for creating the base table
            String sql = "CREATE TABLE IF NOT EXISTS "+ generalTable  +
                    "( nro_entrada bigint PRIMARY KEY, "
                    + "carrera varchar,"
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
        else
            System.out.println("La tabla ya existe");
    }
    
    public void createStudentsTable(){  // Creates a table with the specified name
        
        if(!existsTable(studentsTable)){
            // Sql statement for creating the base table
            String sql = "CREATE TABLE IF NOT EXISTS "+ studentsTable  +
                    "(carrera int,"
                    + "legajo int,"
                    + "fecha_ingreso date);"
                    + "ALTER TABLE " + studentsTable + " ADD CONSTRAINT PK_" + studentsTable
                    + " PRIMARY KEY (legajo, carrera)";

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
        else
            System.out.println("La tabla ya existe");
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
    
    //Method that gets from the DB the list of all tables and then puts into the table maps
    private void updateTableList(){
         List<String> l = this.getTableNames();

         for(String s : l)
            if(s.startsWith("cohorte"))
                this.cohortTables.put(Integer.parseInt(s.substring(7)), s);
            else if (s.startsWith("pass"))
                this.cohortPassTables.put(Integer.parseInt(s.substring(4)), s);
    }
    
    //Drop all tables in the database
    public void dropTables(){
        
        List<String> l = this.getTableNames();
        
        for(String s : l){
            dropTable(s);
        }
        
        this.cohortPassTables.clear();
        this.cohortTables.clear();
        
        System.out.println("Base de datos vaciada correctamente.");

    }
    
    //Deletes a table, if exists, from the database
    public void dropTable(String tableName){
        
        try{
            Statement st = con.createStatement();
            if(this.existsTable(tableName) /*&& this.existsTableName(tableName)*/){
                st.execute("DROP TABLE "+tableName);
                System.out.println("Borrada la tabla: " + tableName);
            }
        } catch(SQLException sqle){
            InfoMsgBox.errBox(sqle.getMessage(), "Error de SQL");
        }
    }
    
    //Obtains all table names in the current database
    private List<String> getTableNames(){
        try{
            //get DB metadata
            DatabaseMetaData m = con.getMetaData();
            //uses as filter the keyword TABLE
            String[] tipos= {"TABLE"};
            //getTables -> (wantedCatalog, wantedSchema, ?, filteringKeywords)
            ResultSet tables = m.getTables(con.getCatalog(), con.getSchema(), "%", tipos);
            LinkedList<String> result = new LinkedList();
            
            while(tables.next()){
                result.add(tables.getString(3));
            }
            
            return result;
            
        } catch(SQLException sqle){
            InfoMsgBox.errBox(sqle.getMessage(), "Error de DBMS");
        }
        
        return null;
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
    
    //Deletes the not useful data from the subjects table. Deletes if the row isn't from the wanted career and plan 
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
    
    //Deletes the not useful data from the students table. Eliminates all rows that doesn't belong to the career code
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
    
    //Creates a table for the desired cohort, getting the data from the general table.
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
//"                TRUNCATE " + passTableDestination + ";"+                        
"                INSERT INTO "+ passTableDestination +
"                SELECT coalesce(t.año, a.año) as año, nodename as nombre , coalesce(a.aprobados, 0) as aprobados, coalesce(t.total-aprobados, t.total) as desaprobados\n" +
"                FROM\n" +
"                --Sacamos la cuenta de los que cursaron alguna materia\n" +
"                   (SELECT año, count(*) as total FROM\n" +
"                       (SELECT extract(year from fecha_regularidad) as año, legajo\n" +
"                       FROM "+ tableUsed +
"                       WHERE nombre = ANY (subjectnames)\n" +
"                       GROUP BY año, legajo) as ax1\n" +
"                   GROUP BY año) AS t\n" +
"                   FULL JOIN \n" +
"                   --Sacamos la cuenta de los que aprobaron todas las materias\n" +
"                   (SELECT año, count(*) as aprobados FROM\n" +
"                       (SELECT MAX(año) as año, count(*) AS cantidad \n" +
"                       FROM\n" +
"                           (SELECT DISTINCT extract(year from fecha_regularidad) as año, legajo, nombre\n" +
"                           FROM "+ tableUsed + 
"                           WHERE nombre = ANY(subjectnames) and (resultado='A' or resultado = 'P')) as x\n" +
"                           GROUP BY legajo) as y\n" +
"                       WHERE cantidad = array_length(subjectnames, 1)\n" +
"                   GROUP BY año) as a\n" +
"                   ON (t.año=a.año);\n" +
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
                        //For each materia in a node
                        for(Materia m:n){
                            materias[i]=m.getNombre();
                            i++;
                        }
                        
                        //Forms a sql array and puts all node subjects, then executes the sql procedure declarated before
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

        if(!existsTable("pass"+añoCohorte)){
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
    }

    //Return pass percentage of a node considering only the cohort year=año // año== null dont consider cohort
    public float getPassPercentage(Nodo n, Integer añoCohorte){
        System.out.println("****Tamaño del mapa de tablas: "+ cohortPassTables.size());
        String tableUsed=getPassTable(añoCohorte);
        System.out.println("Saco porcentaje del nodo: "+n.getNombre()+"- año: "+añoCohorte+"- Tabla:"+tableUsed);
        
        //Esto esta bien????
        if(tableUsed==null){
            return -1;
        }
        
        //Gets from table aproved and total from each node
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
            //Executes the sql query
            ResultSet rs= stmt.executeQuery(sql);
            
            //for each row from the query results gets data and then obtains the percentage
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
            System.out.println("!!!!!!!!!!NOMBRE DE TABLA PASA CREADA "+añoCohorte);
            String passTable= new String("pass"+añoCohorte);
            cohortPassTables.put(añoCohorte, passTable);
            return passTable;
        }
    }
    
    //Returns true only if the name is in any of the structures of this object 
    private boolean existsTableName(String name){
        return(this.generalPassTable.equals(name) || this.generalTable.equals(name) || this.studentsTable.equals(name) ||
                this.cohortPassTables.containsValue(name) || this.cohortTables.containsValue(name));
    }
    
    //Returns true only of the table exists in the database
    private boolean existsTable(String name){
        List<String> l = this.getTableNames();
        
        for(String s : l)
            if(s.equals(name))
                return true;
        
        return false;
    }
 
    public void setTruncate(boolean value){
        this.truncate = value;
    }
}
