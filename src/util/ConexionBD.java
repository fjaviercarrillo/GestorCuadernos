/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.ListadoClientesController;

/**
 *
 * @author ALCAOLIVA
 */
public class ConexionBD {
    private Connection conexion;
    
    private String pathSk = "jdbc:sqlite:C:\\Users\\Sk\\Documents\\NetBeansProjects\\GestorCuadernos\\src\\cuadernoDB.db";
    private String pathAlcaoliva = "jdbc:sqlite:C:\\Users\\ALCAOLIVA\\Documents\\NetBeansProjects\\GestorCuadernos\\src\\cuadernoDB.db";
    
    public ConexionBD() {
        conexion = null;
        try {
            conexion = DriverManager.getConnection(pathSk);
        } catch (SQLException ex) {
            Logger.getLogger(ListadoClientesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void close() {
        try {
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet selectQuery(String query) {
        ResultSet result = null;
        try {
            PreparedStatement st = conexion.prepareStatement(query);
            result = st.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public boolean executeQuery(String query) {
        try {
            PreparedStatement statement = conexion.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
}
