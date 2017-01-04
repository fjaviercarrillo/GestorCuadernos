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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import util.Cliente;

/**
 * FXML Controller class
 *
 * @author ALCAOLIVA
 */
public class ListadoClientesController implements Initializable {
    
    @FXML private TableView tablaDatos;
    @FXML private TableColumn columnaNombre;
    private TableColumn columnaApellidos;
    
    private ObservableList<Cliente> data;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        data = FXCollections.observableArrayList();
        fillClientes();
    }  
    
    /**
     * Llena el GridLayout con la tabla Clientes de la base de datos
     */
    private void fillClientes(){
        Connection conn = connect();
        ResultSet result = null;
        int row = 0;
        if (conn!=null){
            try {
                PreparedStatement st = conn.prepareStatement("SELECT * FROM Clientes");
                result = st.executeQuery();
                while (result.next()) {
                    data.add(new Cliente(result.getString("Nombre")));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ListadoClientesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            configureTable();
        }
    }
    
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:/cuadernoDB.db");
        } catch (SQLException ex) {
            Logger.getLogger(ListadoClientesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
    
    private void close(Connection conn){
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(ListadoClientesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void configureTable() {
        tablaDatos.setEditable(true);
        columnaNombre.setCellValueFactory(
            new PropertyValueFactory<Cliente, String>("nombre"));
        tablaDatos.setItems(data);
    }
}
