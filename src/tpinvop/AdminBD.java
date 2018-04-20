package tpinvop;

import java.sql.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;

//********************************************************************
// AdminDB CLASS: Provides Database conectivity through JDBC API. 
// 
//********************************************************************

public class AdminBD {
    
    Connection con;
    
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
    } 
    
    public void createTable(String dbName){  // Creates a table with the specified name
        
        // Sql statement for creating the base table
        
        String sql = "CREATE TABLE cursadas " +
                "(carrera varchar,"
                + "plan varchar(4),"
                + "legajo int,"
                + "materia int,"
                + "fecha_regularidad date,"
                + "resultado char(1),"
                + "nota numeric(2,2),"
                + "origen varchar,"
                + "nombre varchar);";
        
        Statement stmt;
        
        // Executing SQL statement through DBMS 
        try{
            
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            
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

                String sql = "COPY persons(first_name,last_name,dob,email) \n" +
                        "FROM 'C:\\tmp\\persons.csv' DELIMITER ',' CSV HEADER;";

                try{
                    File f = new File(dirA);
                    FileInputStream is = new FileInputStream(f);
                    Statement stmt;

                    try{

                        stmt = con.createStatement();
                        stmt.execute(sql);
                    }
                    catch(SQLException sqle){

                        InfoMsgBox.errBox("COD "+sqle.getErrorCode()+
                                ") "+sqle.getMessage(), "Error de SQL");
                    }

                }
                catch(FileNotFoundException e){
                    InfoMsgBox.errBox("El archivo no se ha encontrado."+e.getMessage(), "Error");
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
            
                String sql = "DELETE FROM cursadas \n" +
                        "WHERE carrera!='" + carrera +"' or plan!='" + plan + 
                        "' or origen='Cursada Equivalente' or origen='Equivalencia'; ";

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
    
    //Creates a SQL view from the table with pass and fail count from each year and subject
    private void createGeneralView(String tableName, String viewName){
        try{

            if(!con.isClosed()){

                String sql = "CREATE VIEW "+viewName+"/n"+ 
                    "SELECT coalesce(ap.año, des.año) as año, "+
                    "coalesce(ap.nombre, des.nombre) as nombre, "+
                    "coalesce(aprobados,0) as aprobados, "+
                    "coalesce(desaprobados,0) as desaprobados /n"+
                    "FROM" + 
                    "(SELECT nombre, extract(year from fecha_regularidad) as año , count(*) as aprobados /n"+ 
                    "FROM "+ tableName+"/n"+
                    "WHERE resultado='A' or resultado='P' /n"+
                    "GROUP BY nombre, año) AS ap /n"+
                    "FULL JOIN /n"+ 
                    "(SELECT nombre, extract(year from fecha_regularidad) as año , count(*) as desaprobados /n"+
                    "FROM "+ tableName+"/n"+
                    "WHERE resultado='R' or resultado='U' /n"+
                    "GROUP BY nombre, año) AS des /n"+
                    "ON (ap.nombre=des.nombre and ap.año=des.año)";

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
    private void createNodeView(String viewName, List<Nodo> l){
        //********************************************************
        //                  CREAR PROCEDURE DE SQL
        //********************************************************
    }
    
    //public method for constructing the data view
    public void createView(String viewName, String tableName, List<Nodo> l){
        
        this.createGeneralView(tableName, viewName);
        this.createNodeView(viewName, l);
    }

    //returns the 
    public List getPassPercentage(Nodo n){
        
        
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
