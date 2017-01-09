/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.declaracionesViews;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import util.Cliente;
import util.Parcela;

/**
 * FXML Controller class
 *
 * @author Sk
 */
public class NuevaDeclaracionController implements Initializable {

    private Cliente cliente;
    private Stage stage;
    File fileDeclaracion;
    private ObservableList<Parcela> datosDeclaraciones;
    
    @FXML Label nombreCliente;
    @FXML TableColumn columnaIdParcela;
    @FXML TableColumn columnaSizeParcela;
    @FXML TableView tablaDatos;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileDeclaracion = null;
        configureTable();
    }    
    
    public void setData(Cliente cliente, Stage stage) {
        this.cliente = cliente;
        nombreCliente.setText(cliente.getNombre() + " " + cliente.getApellidos());
        this.stage = stage;
    }
    
    @FXML public void openFileDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona una imagen");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Imágenes", "*.jpg"));
        fileDeclaracion = fileChooser.showOpenDialog(stage);
    }
    
    private void configureTable() {
        columnaIdParcela.setCellValueFactory(new PropertyValueFactory<>("idParcela"));
        columnaSizeParcela.setCellValueFactory(new PropertyValueFactory<>("sizeParcela"));
        tablaDatos.setItems(datosDeclaraciones);
    }
}
