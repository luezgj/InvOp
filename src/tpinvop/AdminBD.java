package tpinvop;

import java.sql.*;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.postgresql.core.BaseConnection;
import org.postgresql.copy.CopyManager;

//********************************************************************
// AdminDB CLASS: Provides Database conectivity through JDBC API. 
// 
//********************************************************************

public class AdminBD {
    
    Connection con;
    String tableName;
    String viewName;

    public AdminBD(String tableName, String viewName) {
        this.tableName = tableName;
        this.viewName = viewName;
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
            // Conectamos con la base de datos
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/invopdb",
                    "invop", "12345");
 
            boolean valid = connection.isValid(50000);
            
            InfoMsgBox.infoBox(valid ? "TEST OK" : "TEST FAIL", "Mensaje");
        } catch (java.sql.SQLException sqle) {
                InfoMsgBox.errBox("COD "+sqle.getErrorCode()+
                                ") "+sqle.getMessage(), "Error de SQL");
        }
        
        return connection;
    } */
    
    
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
    
    public void createTable(){  // Creates a table with the specified name
        
        // Sql statement for creating the base table
        
        String sql = "CREATE TABLE IF NOT EXISTS "+ tableName  +
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
            InfoMsgBox.errBox("COD "+sqle.getErrorCode()+
                                ") "+sqle.getMessage(), "Error de SQL");
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
                    InfoMsgBox.errBox("COD "+sqle.getErrorCode()+
                                ") "+sqle.getMessage(), "Error de SQL");
                }
            }
            else
                InfoMsgBox.infoBox("La conexion con la base de datos ha sido cerrada, por favor reconectar."
                        ,"Atencion");
        }
        catch(SQLException sqle){
            InfoMsgBox.errBox("COD "+sqle.getErrorCode()+
                        ") "+sqle.getMessage(), "Error de DBMS");
        }
    }
    
    //Loads a CSV file into the DB
    public void loadCSV(String dirA){
        
        try{
            if(!con.isClosed()){
                
                String sql = "copy "+ tableName +" FROM stdin DELIMITER ',' CSV header";

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
                catch(SQLException sqle){
                    InfoMsgBox.errBox("COD "+sqle.getErrorCode()+
                                ") "+sqle.getMessage(), "Error de SQL");
                }
            
            }
        }
        catch(SQLException sqle){
        
            InfoMsgBox.errBox("COD "+sqle.getErrorCode()+
                        ") "+sqle.getMessage(), "Error de DBMS");
        }
        
    }
    
    //Deletes the not useful data from table
    public void filterData(int carrera, String plan){
        
        try{
            
            if(!con.isClosed()){
                String sql = "DELETE FROM "+ tableName +
                        "WHERE carrera!='" + carrera +"' or plan!='" + plan + 
                        "' or origen='Cursada Equivalente' or origen='Equivalencia'; ";

                try{
                    Statement stmt = con.createStatement();
                    stmt.execute(sql);
                    System.out.println("Filas filtradas.");
                }
                catch(SQLException sqle){
                    InfoMsgBox.errBox("COD "+sqle.getErrorCode()+
                                ") "+sqle.getMessage(), "Error de SQL");
                }
            }
            else
                InfoMsgBox.infoBox("La conexion con la base de datos ha sido cerrada, por favor reconectar."
                        ,"Atencion");
        }
        catch(SQLException sqle){
            InfoMsgBox.errBox("COD "+sqle.getErrorCode()+
                        ") "+sqle.getMessage(), "Error de DBMS");
        }
    }
    
    //Creates a SQL view from the table with pass and fail count from each year and subject
    private void createGeneralView(){
        try{

            if(!con.isClosed()){
                
                String sql= "CREATE TABLE "+ viewName +" AS SELECT coalesce(ap.año, des.año) as año, \n" +
"                    coalesce(ap.nombre, des.nombre) as nombre, \n" +
"                    coalesce(aprobados,0) as aprobados, \n" +
"                    coalesce(desaprobados,0) as desaprobados \n" +
"                    FROM\n" +
"                    (SELECT nombre, extract(year from fecha_regularidad) as año , count(*) as aprobados \n" +
"	                    FROM "+ tableName +" \n" +
"	                    WHERE resultado='A' or resultado='P' \n" +
"	                    GROUP BY nombre, año) AS ap \n" +
"                    FULL JOIN \n" +
"                    (SELECT nombre, extract(year from fecha_regularidad) as año , count(*) as desaprobados \n" +
"	                    FROM "+ tableName +" \n" +
"	                    WHERE resultado='R' or resultado='U' \n" +
"	                    GROUP BY nombre, año) AS des \n" +
"                    ON (ap.nombre=des.nombre and ap.año=des.año)";

                try{
                    Statement stmt = con.createStatement();
                    stmt.execute(sql);
                }
                catch(SQLException sqle){
                    InfoMsgBox.errBox("COD "+sqle.getErrorCode()+
                                ") "+sqle.getMessage(), "Error de SQL");
                }
            }
            else
                InfoMsgBox.infoBox("La conexion con la base de datos ha sido cerrada, por favor reconectar."
                        ,"Atencion");
        }
        catch(SQLException sqle){
            InfoMsgBox.errBox("COD "+sqle.getErrorCode()+
                        ") "+sqle.getMessage(), "Error de DBMS");
        }
    }
    
    //Updates the view created with data asociated to multiple subject node in lines
    private void createNodeView(List<Nodo> l){
        try{

            if(!con.isClosed()){

                //Create procedure to update view
                String sql = "CREATE OR REPLACE FUNCTION createNodeView(nodename character varying,subjectnames character varying[]) RETURNS void AS $$\n" +
"                BEGIN \n" +
"                INSERT INTO "+ viewName +
"                SELECT coalesce(t.año, a.año) as año, nodename as nombre , coalesce(a.aprobados, 0) as aprobados, coalesce(t.total-aprobados, t.total) as desaprobados\n" +
"                FROM\n" +
"                --Sacamos la cuenta de los que cursaron alguna materia\n" +
"                                (SELECT año,count(*) as total FROM\n" +
"                                        (SELECT extract(year from fecha_regularidad) as año, legajo\n" +
"                                        FROM "+ tableName +
"                                        WHERE nombre = ANY (subjectnames)\n" +
"                                        GROUP BY año, legajo) as ax1\n" +
"                                GROUP BY año) AS t\n" +
"                        FULL JOIN \n" +
"                                --Sacamos la cuenta de los que aprobaron todas las materias\n" +
"                                (SELECT año,count(*) as aprobados FROM\n" +
"                                        (SELECT extract(year from fecha_regularidad) as año, legajo\n" +
"                                        FROM "+ tableName +
"                                        WHERE nombre = ANY (subjectnames) and resultado='A'\n" +
"                                        GROUP BY año, legajo\n" +
"                                        HAVING count(*)=array_length(subjectnames, 1)) as ax2\n" +
"                                GROUP BY año) as a\n" +
"                        ON (t.año=a.año);\n" +
"                END;\n" +
"                $$ LANGUAGE plpgsql;";
                
                try{
                    Statement stmt = con.createStatement();
                    stmt.execute(sql);
                    System.out.println("Funcion creada");
                }
                catch(SQLException sqle){
                    InfoMsgBox.errBox("COD "+sqle.getErrorCode()+
                                ") "+sqle.getMessage(), "Error de SQL");
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
                        InfoMsgBox.errBox("COD " + sqle.getErrorCode()
                                + ") " + sqle.getMessage(), "Error de SQL");
                    }
                }
            }
            else
                InfoMsgBox.infoBox("La conexion con la base de datos ha sido cerrada, por favor reconectar."
                        ,"Atencion");
        }
        catch(SQLException sqle){
            InfoMsgBox.errBox("COD "+sqle.getErrorCode()+
                        ") "+sqle.getMessage(), "Error de DBMS");
        }
    }
    
    //public method for constructing the data view
    public void createView(List<Nodo> l){
        this.createGeneralView();
        this.createNodeView(l);
    }

    //Devolvemos sólo un porcentaje por año
    public float getPassPercentage(Nodo n){
        String sql="SELECT avg(aprobados) FROM "+ viewName +
        "WHERE nombre=" + n.getNombre();
        float passPercentage=0f;
        try {
            Statement stmt;
            stmt = con.createStatement();
            ResultSet rs= stmt.executeQuery(sql);
            rs.next();
            passPercentage=rs.getFloat(1);
        } catch (SQLException sqle) {
            InfoMsgBox.errBox("COD "+sqle.getErrorCode()+
                        ") "+sqle.getMessage(), "Error de SQL");
        }
        
        return passPercentage;
    }
    
    public boolean dbOK(){
        try{
            return !con.isClosed();
        }
        catch(SQLException sqle){
            InfoMsgBox.errBox("COD "+sqle.getErrorCode()+
                        ") "+sqle.getMessage(), "Error de DBMS");
        }
        
        return false;
    }
            
}