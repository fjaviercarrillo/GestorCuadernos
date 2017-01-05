/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.clientesViews;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ALCAOLIVA
 */
public class NuevoClienteController implements Initializable {
    // Variables para conexión con FXML
    @FXML private TextField textFieldNombre;
    @FXML private TextField textFieldApellidos;
    @FXML private TextField textFieldDNI;
    @FXML private TextField textFieldDireccion;
    @FXML private TextField textFieldCP;
    @FXML private TextField textFieldLocalidad;
    @FXML private TextField textFieldProvincia;
    @FXML private TextField textFieldPais;
    @FXML private CheckBox checkBoxAsesor;
    @FXML private Button closeButton;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    /**
     * Comprueba si los datos del cliente son correctos y lo ingresa en la BD
     */
    @FXML private void aceptar() {
        if (textFieldNombre.getText().compareTo("") == 0) {
            showAlert("El nombre no puede estar vacío");
        } else if (textFieldApellidos.getText().compareTo("") == 0) {
            showAlert("Los apellidos no pueden estar vacíos");
        } else if (textFieldDNI.getText().compareTo("") == 0) {
            showAlert("El DNI no puede estar vacío");
        } else if (!isDNICorrect(textFieldDNI.getText())) {
            showAlert("El formato del DNI no es correcto (debe ser del tipo 12345678A)");
        } else if (textFieldDireccion.getText().compareTo("") == 0) {
            showAlert("La dirección no puede estar vacía");
        } else if (textFieldCP.getText().compareTo("") == 0) {
            showAlert("El código postal no puede estar vacío");
        } else if (!isCPCorrect(textFieldCP.getText())) {
            showAlert("El código postal no es correcto (debe de ser númerico y no mayor de 5 dígitos");
        } else if (textFieldLocalidad.getText().compareTo("") == 0) {
            showAlert("La localidad no puede estar vacía");
        } else if (textFieldProvincia.getText().compareTo("") == 0) {
            showAlert("La provincia no puede estar vacía");
        } else if (textFieldPais.getText().compareTo("") == 0) {
            showAlert("El país no puede estar vacío");
        } else {
            addUserToBD();
        }
    }
    
    /**
     * Comprueba si el código postal es correcto
     * @param CP El código postal
     * @return true si es correcto, false si no lo es
     */
    private boolean isCPCorrect(String CP) {
        if (CP.length() < 5) {
            return false;
        } else {
            try {
                Integer.parseInt(CP);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Cierra la ventana
     */
    @FXML private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Añade el usuario a la base de datos
     */
    private void addUserToBD() {
        
    }
    
    /**
     * Comprueba si el DNI es correcto
     * @param DNI el DNI a comprobar
     * @return true si es correcto, falso si no lo es
     */
    private boolean isDNICorrect(String DNI) {
        if (DNI.length() != 9) {
            return false;
        } else {
            try {
                Integer.parseInt(DNI.substring(0, 7));
            } catch (NumberFormatException e) {
                return false;
            }
            return DNI.charAt(8) >= 'A' && (DNI.charAt(8) <= 'Z' || DNI.charAt(8) >= 'a') && DNI.charAt(8) <= 'z';
        }
    }
    
    /**
     * Muestra un mensaje de error personalizado
     * @param msg El mensaje de error
     */    
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error al crear el Cliente");
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
    
}
