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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
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
    private File fileDeclaracion;
    private int rowCounter;
    private Scene sceneOwner;
    
    @FXML Label nombreCliente;
    @FXML TextField idParcela1;
    @FXML TextField sizeParcela1;
    @FXML Button btnAddRow1;
    @FXML GridPane grid;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileDeclaracion = null;
        rowCounter = 2;
        
    }    
    
    public void setData(Cliente cliente, Stage stage, Scene scene) {
        this.cliente = cliente;
        nombreCliente.setText(cliente.getNombre() + " " + cliente.getApellidos());
        this.stage = stage;
        this.sceneOwner = scene;
    }
    
    @FXML private void openFileDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona una imagen");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Imágenes", "*.jpg"));
        fileDeclaracion = fileChooser.showOpenDialog(stage);
    }
     private void addListenerToTextField(TextField textField, boolean last) {
         if (last) {
             
         } 
     }
     
     @FXML private void addRow() {
         String counterId = String.valueOf(rowCounter);
         
         // Creamos los campos de texto y el botón
         TextField textFieldId = new TextField();
         textFieldId.setId("id"+counterId);
         TextField textFieldSize = new TextField();
         textFieldSize.setId("size"+counterId);
         Button buttonAdd = new Button("Add");
         buttonAdd.setId("button"+counterId);
         
         // Añadimos el evento que cree una nueva fila al pulsar el botón
         buttonAdd.setOnAction(new EventHandler<ActionEvent>() {
             @Override public void handle(ActionEvent e) {
                 addRow();
             }
         });
         
         // Añadimos el evento que mueve el foco al pulsar el tabulador
         textFieldId.setOnKeyPressed(textField -> {
             KeyCode key = textField.getCode();
             if (key == KeyCode.TAB) {
                 TextField focusedNode = (TextField) sceneOwner.getFocusOwner();
                 String focusedId = focusedNode.getId();
                 TextField nextFocusedNode = (TextField) sceneOwner.lookup("#size"+focusedId);
                 nextFocusedNode.requestFocus();
             }
         });
         
        textFieldSize.setOnKeyPressed(textField -> {
            KeyCode key = textField.getCode();
            if (key == KeyCode.TAB) {
                TextField focusedNode = (TextField) sceneOwner.getFocusOwner();
                String focusedId = focusedNode.getId();
                Button nextFocusedNode = (Button) sceneOwner.lookup("#button"+focusedId);
                nextFocusedNode.requestFocus();
            }
        });
         GridPane.setRowIndex(textFieldId, rowCounter);
         GridPane.setColumnIndex(textFieldId, 0);
         GridPane.setValignment(textFieldId, VPos.CENTER);
         GridPane.setHalignment(textFieldId, HPos.CENTER);
         
         GridPane.setRowIndex(textFieldSize, rowCounter);
         GridPane.setColumnIndex(textFieldSize, 1);
         GridPane.setValignment(textFieldSize, VPos.CENTER);
         GridPane.setHalignment(textFieldSize, HPos.CENTER);
         
         GridPane.setRowIndex(buttonAdd, rowCounter);
         GridPane.setColumnIndex(buttonAdd, 2);
         GridPane.setValignment(buttonAdd, VPos.CENTER);
         GridPane.setHalignment(buttonAdd, HPos.CENTER);
         grid.getChildren().addAll(textFieldId, textFieldSize, buttonAdd);
         rowCounter++;
     }
}
