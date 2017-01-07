/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.sql.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import util.Cliente;
import util.ConexionBD;
import view.clientesViews.EditarClienteController;

/**
 * FXML Controller class
 *
 * @author ALCAOLIVA
 */
public class ListadoClientesController implements Initializable {
    
    @FXML private TableView tablaDatos;
    @FXML private TableColumn columnaNombre;
    @FXML private TableColumn columnaApellidos;
    @FXML private TableColumn columnaDNI;
    @FXML private TableColumn columnaDireccion;
    @FXML private TableColumn columnaCP;
    @FXML private TableColumn columnaLocalidad;
    @FXML private TableColumn columnaProvincia;
    @FXML private TableColumn columnaPais;
    @FXML private TableColumn columnaAsesor;
    
    private ObservableList<Cliente> data;
    ConexionBD connection;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        data = FXCollections.observableArrayList();
        fillClientes();
    }  
    
    /**
     * Crea una nueva ventana para introducir un usuario
     */
    @FXML private void createUser() {
        Stage secondStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        AnchorPane nuevoClienteLayout;
        try {
            loader.setLocation(getClass().getResource("/view/clientesViews/NuevoCliente.fxml"));
            nuevoClienteLayout = (AnchorPane) loader.load();
            secondStage.setScene(new Scene(nuevoClienteLayout));
            secondStage.setTitle("Nuevo cliente");
            secondStage.showAndWait();
            
            data.clear();
            fillClientes();
        } catch (IOException ex) {
            Logger.getLogger(ListadoClientesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML private void handleUpdateCliente() {
        Cliente selectedClient;
        selectedClient = (Cliente) tablaDatos.getSelectionModel().getSelectedItem();
        if (selectedClient!=null) {
            updateUserDialog(selectedClient);
        }
    }
    
    private void updateUserDialog(Cliente cliente) {
        try{
            // Carga el archivo fxml y crea un nuevo stage
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/clientesViews/EditarCliente.fxml"));
            AnchorPane updateClienteLayout = (AnchorPane) loader.load();
            
            // Crea la nueva ventana
            Stage secondStage = new Stage();
            secondStage.setTitle("Editar cliente");
            Scene scene = new Scene(updateClienteLayout);
            secondStage.setScene(scene);
            
            //Pasa el cliente al controlador
            EditarClienteController controller = loader.getController();
            controller.setData(cliente);
            secondStage.showAndWait();
            
            data.clear();
            fillClientes();
        } catch (IOException ex) {
            Logger.getLogger(ListadoClientesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Llena el GridLayout con la tabla Clientes de la base de datos
     */
    private void fillClientes(){
        // Conectamos a la base de datos para rellenar la tabla con los clientes existentes
        connection = new ConexionBD();
        ResultSet result = connection.selectQuery("SELECT * FROM Clientes");
        if (result!=null) {
            try {
                while (result.next()) {
                    String nombreCliente = result.getString("Nombre");
                    String apellidosCliente = result.getString("Apellidos");
                    String DNICliente = result.getString("DNI");
                    String direccionCliente = result.getString("Direccion");
                    int codigoPostal = result.getInt("CP");
                    String localidadCliente = result.getString("Localidad");
                    String provinciaCliente = result.getString("Provincia");
                    String paisCliente = result.getString("Pais");
                    int idCliente = result.getInt("id");
                    boolean necesitaAsesorCliente = (result.getInt("NecesitaAsesor") == 0)?false:true;
                    
                    // Añadimos una nueva fila a la tabla con un objeto cliente
                    data.add(new Cliente(
                            nombreCliente, apellidosCliente, DNICliente, direccionCliente, codigoPostal, localidadCliente, provinciaCliente,
                            paisCliente, necesitaAsesorCliente, idCliente));   
                    
                    // Añadimos un listener a cada fila para cuando hacemos doble click
                    tablaDatos.setRowFactory(tv -> {
                        TableRow<Cliente> row = new TableRow<>();
                        row.setOnMouseClicked(event -> {
                            if (event.getClickCount() == 2 && (!row.isEmpty())) {
                                updateUserDialog(row.getItem());
                            } else {
                                if (event.getClickCount() == 2 && (row.isEmpty())) {
                                    createUser();
                                }
                            }
                        });
                        return row;
                    });
                }
            } catch (SQLException ex) {
                Logger.getLogger(ListadoClientesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        configureTable();
        connection.close();
    }
    
    /**
     * Configura el TableView asignando el tipo de valor a las columnas
     */
    private void configureTable() {
        //tablaDatos.setEditable(true);
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnaDNI.setCellValueFactory(new PropertyValueFactory<>("DNI"));
        columnaDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        columnaCP.setCellValueFactory(new PropertyValueFactory<>("codigoPostal"));
        columnaLocalidad.setCellValueFactory(new PropertyValueFactory<>("localidad"));
        columnaProvincia.setCellValueFactory(new PropertyValueFactory<>("provincia"));
        columnaPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
        columnaAsesor.setCellValueFactory(
            new Callback<CellDataFeatures<Cliente, Boolean>, ObservableValue<Boolean>>(){
                @Override
                public ObservableValue<Boolean> call(CellDataFeatures<Cliente, Boolean> param) {
                    return param.getValue().necesitaAsesor();
                }
            });
        columnaAsesor.setCellFactory(CheckBoxTableCell.forTableColumn(columnaAsesor));
        tablaDatos.setItems(data);
        tablaDatos.setOnKeyPressed(tv -> {
            KeyCode key = tv.getCode();
            if (key.equals(KeyCode.ENTER)) {
                updateUserDialog((Cliente) tablaDatos.getSelectionModel().getSelectedItem());
            } else {
                if (key.equals(KeyCode.DELETE)) {
                    deleteCliente();
                }
            }
        });
    }
    
    @FXML private void deleteCliente() {
        Cliente client = (Cliente) tablaDatos.getSelectionModel().getSelectedItem();
        if (client != null) {
            if (confirmDialog(client.getNombre(), client.getApellidos())) {
                ConexionBD connection = new ConexionBD();
                String query = "DELETE FROM Clientes WHERE Id = " + client.getIdCliente();
                connection.executeQuery(query);
                connection.close();
                data.clear();
                fillClientes();
            }            
        }
    }

    private boolean confirmDialog(String nombre, String apellidos) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Borrar cliente");
        alert.setHeaderText("Borrar cliente");
        alert.setContentText("¿Está seguro de que quiere borrar al cliente " + nombre + " " + apellidos + "?");
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
}
