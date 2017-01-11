/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.declaracionesViews;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import util.Cliente;
import util.Parcela;
import application.Main;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author Sk
 */
public class NuevaDeclaracionController implements Initializable {

    private Cliente cliente;
    private Stage stage;
    private File fileDeclaracion, fileDeclaracion2;
    private int rowCounter;
    private Scene sceneOwner;
    private ArrayList<Parcela> listaParcelas;
    private final String imgDeclaracionesPath = "\\img\\declaraciones";
    
    @FXML Label nombreCliente;
    @FXML GridPane grid;
    @FXML TextField totalSizeTextField;
    @FXML Label nombreFichero;
    @FXML Label labelImagen2;
    @FXML Label nombreFichero2;
    @FXML Button buttonFichero;
    @FXML Button buttonFichero2;
    @FXML Button buttonAddImage;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileDeclaracion = null;
        fileDeclaracion2 = null;
        rowCounter = 1;
        listaParcelas = new ArrayList<>();
        totalSizeTextField.setText("0.0");
        addRow();
        addButtonFileListeners();
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
    @FXML private void openFileDialog(boolean isFirst) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona una imagen");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Imágenes", "*.jpg"));
        if (isFirst) {
            fileDeclaracion = fileChooser.showOpenDialog(stage);
            nombreFichero.setText(fileDeclaracion.getName());
        } else {
            fileDeclaracion2 = fileChooser.showOpenDialog(stage);
            nombreFichero2.setText(fileDeclaracion2.getName());
        }
    }
    
    /**
     * Crea un listener para cada button
     */
    private void addButtonFileListeners() {
        buttonFichero.setOnAction((ActionEvent e) -> {
            openFileDialog(true);
        });
        buttonFichero2.setOnAction((ActionEvent e) -> {
            openFileDialog(false);
        });
    }
    
    /**
     * Muestra los controles para añadir una nueva imagen
     */
    @FXML private void addNewImage() {
        labelImagen2.setOpacity(100.0);
        nombreFichero2.setOpacity(100.0);
        buttonFichero2.setOpacity(100.0);
        buttonAddImage.setOpacity(0.0);
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
    
    /**
     * Aceptamos la declaración, copiamos las imágenes y agregamos datos a la BD
     */
    @FXML private void crearDeclaracion() {
        try {
            // Copiamos las imágenes a nuestro directorio
            copyImages(fileDeclaracion);
            if (fileDeclaracion2 != null) {
                copyImages(fileDeclaracion2);
            }
            
            // Creamos una lista de objetos Parcela con los datos introducidos por el usuario
            populateParcelaList();
        } catch (IOException ex) {
            dialogAlert("Ha habido un error al copiar la imagen");
        } catch (NullPointerException ex) {
            dialogAlert("Debes escoger una imagen para la declaración de cultivo");
        }
    }
    
    /**
     * Copia la imagen al directorio general de imágenes
     * @param file Es la imagen a copiar
     */
    private void copyImages(File file) throws IOException {
        if (file != null) {
            Path filePath = Paths.get(file.getAbsolutePath());
            Path newPath = Paths.get(Main.rootPath + imgDeclaracionesPath + "\\", file.getName());
            Files.copy(filePath, newPath, REPLACE_EXISTING, COPY_ATTRIBUTES, NOFOLLOW_LINKS);           
        } else {
            throw new NullPointerException();
        }
    }
    
    private void populateParcelaList() {
        for (int i=0; i<rowCounter; i++) {
            int idParcela;
            double sizeParcela;
            boolean exists = false;
            idParcela = Integer.parseInt(((TextField) sceneOwner.lookup("#id"+i)).getText());
            sizeParcela = Double.parseDouble(((TextField) sceneOwner.lookup("#size"+i)).getText());
            for (int j=0; j<listaParcelas.size(); i++) {
                Parcela parcelaTemp = listaParcelas.get(j);
                if (parcelaTemp.getIdParcela().get() == idParcela) {
                    parcelaTemp.addToSizeParcela(sizeParcela);
                    exists = true;
                }
            }
            if (!exists) {
                Parcela nuevaParcela;
                nuevaParcela = new Parcela(idParcela, sizeParcela);
                listaParcelas.add(nuevaParcela);
            }
        }
    }
    
    private void dialogAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
