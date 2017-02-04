/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.dialog;

import application.Main;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Callback;
import util.Cliente;

/**
 * FXML Controller class
 *
 * @author ALCAOLIVA
 */
public class BusquedaClienteController implements Initializable {

    final int ROW_HEIGHT = 24;
    private ObservableList<Cliente> listaClientes;
    List<Cliente> lista;
    
    @FXML TextField textBusqueda;
    @FXML ListView listaResultados;
    
    Stage stage;
    Cliente cliente;
    Connection connection;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTextFieldKeyListener();
        setListViewKeyListener();
        connectToBD();
    }    
    
    /**
     * Añade los listeners para el cuadro de texto
     */
    private void setTextFieldKeyListener() {
        textBusqueda.setOnKeyPressed(textField -> {
            KeyCode key = textField.getCode();
            if (key == KeyCode.DOWN) {
                // Si la tecla pulsada es abajo selecciona el resultado inferior
                listaResultados.requestFocus();
                listaResultados.getSelectionModel().select(0);
                listaResultados.getFocusModel().focus(0);
            } else if (key == KeyCode.UP) {
                // Si la tecla pulsada es arriba selecciona el resultado anterior
                listaResultados.requestFocus();
                listaResultados.getSelectionModel().select(listaResultados.getItems().size()-1);
                listaResultados.getFocusModel().focus(listaResultados.getItems().size()-1);
            }
        });
    }
    
    // TODO: Arreglar este código para que al pulsar abajo estando en el último elemento del ListView ponga el foco en el primero
    private void setListViewKeyListener() {
        listaResultados.setOnKeyPressed(listView -> {
            KeyCode key = listView.getCode();
            if (key == KeyCode.ENTER) {
                cliente = (Cliente) listaResultados.getSelectionModel().getSelectedItem();
                stage.close();
            }
        });
    }
    
    /**
     * Busca los clientes de la base de datos que coincidan con lo escrito en el cuadro de texto
     */
    @FXML private void busquedaCliente() {
        listaResultados.getItems().clear();
        if (textBusqueda.getText().isEmpty()) {
            listaResultados.setOpacity(0.0);
        } else {
            listaResultados.setOpacity(100.0);
        }
        getListaClientesFromBD();
        listaClientes = FXCollections.observableList(lista);
        listaResultados.setItems(listaClientes);
        listaResultados.setCellFactory(
            new Callback<ListView<Cliente>, ListCell<Cliente>>() {

                @Override
                public ListCell<Cliente> call(ListView<Cliente> cliente) {

                        ListCell<Cliente> cell = new ListCell<Cliente>(){

                        @Override
                        protected void updateItem(Cliente c, boolean bln) {
                            super.updateItem(c, bln);
                            if (c != null) {
                                setText(c.getNombre() + " " + c.getApellidos());
                            }
                        }
                    };
                    return cell;
                }
            }
        );
        listaResultados.setPrefHeight(listaClientes.size() * ROW_HEIGHT + 2);
    }
    
    /**
     * Llena la lista de resultados de clientes con los datos de la BD
     */
    private void getListaClientesFromBD() {
        try {
            Statement statement = connection.createStatement();
            String text = textBusqueda.getText();
            String query = "SELECT Nombre, Apellidos, DNI, Direccion, CP, Localidad, Provincia, Pais, id, NecesitaAsesor, "
                    + "(Nombre||' '||Apellidos) as Cliente "
                    + "FROM Clientes WHERE Nombre||' '||Apellidos LIKE '%" + text + "%'";
            ResultSet result = statement.executeQuery(query);
            lista = new ArrayList<>();
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
                boolean necesitaAsesorCliente = (result.getInt("NecesitaAsesor") != 0);
                lista.add(new Cliente(nombreCliente, apellidosCliente, DNICliente, direccionCliente, codigoPostal,
                        localidadCliente, provinciaCliente, paisCliente, necesitaAsesorCliente, idCliente));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BusquedaClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setData(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * Pone el texto del cuadro de búsqueda en blanco
     */
    @FXML private void onClickBusqueda() {
        textBusqueda.setText("");
    }
    
    /**
     * Conecta a la base de datos
     */
    private void connectToBD() {
        connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:"+Main.rootPath+"cuadernoDB.db");
        } catch (SQLException ex) {
            Logger.getLogger(BusquedaClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
