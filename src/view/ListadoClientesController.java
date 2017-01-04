/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author ALCAOLIVA
 */
public class ListadoClientesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillClientes();
    }  
    
    /**
     * Llena el GridLayout con la tabla Clientes de la base de datos
     */
    private void fillClientes(){
        String query = null;
        Connection connection = null;        
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:/cuaderno_db.db", "", "");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                System.out.println(rs.getString("Nombre"));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ListadoClientesController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ListadoClientesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
}
