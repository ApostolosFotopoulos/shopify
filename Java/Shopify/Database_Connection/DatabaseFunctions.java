package Database_Connection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author justdemo
 */
public class DatabaseFunctions {
    
    public Connection conn;
    
    /* Try to connect with the database */
    public DatabaseFunctions(String host,String port,String databaseName,String user,String password){
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://"+host+":"+port+"/"+databaseName,user,password);
        } catch (SQLException e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        //System.out.println("Successfully Connected with "+databaseName);
    }
    
    /* SELECT Query at database */
    public ResultSet selectQuery(String queryStr) throws SQLException{
        Statement stmt= conn.createStatement();
        ResultSet rs = stmt.executeQuery(queryStr);
        return rs;
    }
    
    /* INSERT Query at database */
    /* UPDATE Query at database */
    /* DELETE Query at database */
    public void insertQuery(String queryStr) throws SQLException{
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(queryStr);
        stmt.close();
    }
}
