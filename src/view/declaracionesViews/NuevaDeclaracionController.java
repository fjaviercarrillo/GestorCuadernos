/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.declaracionesViews;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
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
    private ArrayList<Parcela> listaParcelas;
    
    @FXML Label nombreCliente;
    @FXML GridPane grid;
    @FXML TextField totalSizeTextField;
    @FXML Label nombreFichero;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileDeclaracion = null;
        rowCounter = 1;
        listaParcelas = new ArrayList<>();
        totalSizeTextField.setText("0.0");
        addRow();
        
    }    
    
    /**
     * Crea los datos principales de la ventana
     * @param cliente Es el cliente al que se le asignará la declaración de cultivo
     * @param stage Es la escena que se ha creado
     * @param scene Es la ventana que contiene la escena
     */
    public void setData(Cliente cliente, Stage stage, Scene scene) {
        this.cliente = cliente;
        nombreCliente.setText(cliente.getNombre() + " " + cliente.getApellidos());
        this.stage = stage;
        this.sceneOwner = scene;
    }
    
    /**
     * Abre el cuadro de diálogo para escoger un archivo
     */
    @FXML private void openFileDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona una imagen");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Imágenes", "*.jpg"));
        fileDeclaracion = fileChooser.showOpenDialog(stage);
        nombreFichero.setText(fileDeclaracion.getName());
    }
     
    /**
     * Añade una nueva fila al GridPane
     */
    @FXML private void addRow() {
        String counterId = String.valueOf(rowCounter);

        // Creamos los campos de texto y el botón
        TextField textFieldId = new TextField();
        textFieldId.setId("id"+counterId);
        TextField textFieldSize = new TextField();
        textFieldSize.setId("size"+counterId);
        Button buttonAdd = new Button("Add");
        buttonAdd.setId("button"+counterId);
        
        // Añadimos los distintos listeners para los objetos de cada fila
        addRowListeners(textFieldId, textFieldSize, buttonAdd);
        
        // Añadimos los elementos creados al GridPane
        addToGridPane(textFieldId, textFieldSize, buttonAdd);
    }
    
    /**
     * Añade los diferentes listeners a los objetos de cada línea
     * @param textFieldId TextField con la id de la parcela
     * @param textFieldSize TextField con el tamaño de la parcela
     * @param buttonAdd Button para añadir nuevas líneas
     */
    private void addRowListeners(TextField textFieldId, TextField textFieldSize, Button buttonAdd) {
        buttonAdd.setOnAction((ActionEvent e) -> {
            addRow();
        });
        
        buttonAdd.setOnKeyPressed(button -> {
            KeyCode key = button.getCode();
            if (key == KeyCode.ENTER) {
                // Cuando se le de a intro se crea una nueva línea
                addRow();
                
                // Averiguamos el botón pulsado y sacamos los valores de los TextField de esa misma línea
                Button focusedNode = (Button) sceneOwner.getFocusOwner();
                String focusedRow = focusedNode.getId().substring(6);
                int idParcela = Integer.parseInt(((TextField) sceneOwner.lookup("#id"+focusedRow)).getText());
                double sizeParcela = Double.parseDouble(((TextField) sceneOwner.lookup("#size"+focusedRow)).getText());
                
                // Añadimos el valor de los TextField de la línea n a un array de parcelas
                listaParcelas.add(new Parcela(idParcela, sizeParcela));
                
                // Cambiamos el foco al primer TextField de la nueva fila
                int focused = Integer.parseInt(focusedRow);
                focusedRow = String.valueOf(focused + 1);
                TextField nextFocusedNode = (TextField) sceneOwner.lookup("#id"+focusedRow);
                nextFocusedNode.requestFocus();
                
                putTotalSize();
            }
        });

        // Añadimos el evento que mueve el foco al pulsar el tabulador
        textFieldId.setOnKeyPressed(textField -> {
            KeyCode key = textField.getCode();
            if (key == KeyCode.TAB || key == KeyCode.ENTER) {
                TextField focusedNode = (TextField) sceneOwner.getFocusOwner();
                String focusedId = focusedNode.getId().substring(2);
                TextField nextFocusedNode = (TextField) sceneOwner.lookup("#size"+focusedId);
                nextFocusedNode.requestFocus();
            }
        });

        textFieldSize.setOnKeyPressed(textField -> {
            KeyCode key = textField.getCode();
            if (key == KeyCode.TAB || key == KeyCode.ENTER) {
                TextField focusedNode = (TextField) sceneOwner.getFocusOwner();
                String focusedId = focusedNode.getId().substring(4);
                Button nextFocusedNode = (Button) sceneOwner.lookup("#button"+focusedId);
                nextFocusedNode.requestFocus();
            }
        });
    }
    
    /**
     * Añade los elementos al GridPane
     * @param textFieldId Un TextField con la id de la parcela
     * @param textFieldSize Un TextField con el tamaño de la parcela
     * @param buttonAdd Un Button para añadir nuevas líneas al GridPane
     */
    private void addToGridPane(TextField textFieldId, TextField textFieldSize, Button buttonAdd) {
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
    
    /**
     * Rellena el campo con el tamaño total de la declaración de cultivo
     */
    private void putTotalSize() {
        double totalSize = 0.0;
        for (int i=0; i<listaParcelas.size(); i++) {
            totalSize += listaParcelas.get(i).getSizeParcela().get();
        }
        totalSizeTextField.setText(String.valueOf(totalSize));
    }
    
    @FXML private void crearDeclaracion() {
        
    }
}
