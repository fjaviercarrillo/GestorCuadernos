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
import util.Cliente;
import util.ConexionBD;

/**
 * FXML Controller class
 *
 * @author ALCAOLIVA
 */
public class EditarClienteController implements Initializable {
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
    
    private String nombre;
    private String apellidos;
    private String DNI;
    private String direccion;
    private int CP;
    private String localidad;
    private String provincia;
    private String pais;
    private boolean necesitaAsesor;
    private int idCliente;
    
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
        if (isCPCorrect(textFieldCP.getText())) {
            getFieldData();
            if (nombre.compareTo("") == 0) {
                showAlert("El nombre no puede estar vacío");
            } else if (apellidos.compareTo("") == 0) {
                showAlert("Los apellidos no pueden estar vacíos");
            } else if (DNI.compareTo("") == 0) {
                showAlert("El DNI no puede estar vacío");
            } else if (!isDNICorrect(DNI)) {
                showAlert("El formato del DNI no es correcto (debe ser del tipo 12345678A)");
            } else if (direccion.compareTo("") == 0) {
                showAlert("La dirección no puede estar vacía");
            } else if (localidad.compareTo("") == 0) {
                showAlert("La localidad no puede estar vacía");
            } else if (provincia.compareTo("") == 0) {
                showAlert("La provincia no puede estar vacía");
            } else if (pais.compareTo("") == 0) {
                showAlert("El país no puede estar vacío");
            } else {
                updateUserInBD();
            }
        } else {
            showAlert("El código postal no es correcto (debe de ser numérico y no mayor de 5 dígitos");
        }
    }
    
    public void setData(Cliente cliente) {
        textFieldNombre.setText(cliente.getNombre());
        textFieldApellidos.setText(cliente.getApellidos());
        textFieldDNI.setText(cliente.getDNI());
        textFieldDireccion.setText(cliente.getDireccion());
        textFieldCP.setText(String.valueOf(cliente.getCodigoPostal()));
        textFieldLocalidad.setText(cliente.getLocalidad());
        textFieldProvincia.setText(cliente.getProvincia());
        textFieldPais.setText(cliente.getPais());
        checkBoxAsesor.setSelected(cliente.necesitaAsesor().get());
        this.idCliente = cliente.getIdCliente();
    }
    
    private void getFieldData() {
        nombre = textFieldNombre.getText();
        apellidos = textFieldApellidos.getText();
        DNI = textFieldDNI.getText();
        direccion = textFieldDireccion.getText();
        CP = Integer.parseInt(textFieldCP.getText());
        localidad = textFieldLocalidad.getText();
        provincia = textFieldProvincia.getText();
        pais = textFieldPais.getText();
        necesitaAsesor = checkBoxAsesor.isSelected();
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
    private void updateUserInBD() {
        ConexionBD connection = new ConexionBD();
        int intNecesitaAsesor = (necesitaAsesor) ? 1 : 0;
        String updateQuery = "UPDATE Clientes SET Nombre = '" + nombre + "', Apellidos = '" + apellidos + "', DNI = '" + DNI + "', "
                + "Direccion = '" + direccion + "', CP = " + CP + ", Localidad = '" + localidad + "', Provincia = '" + provincia + "', "
                + "Pais = '" + pais + "', NecesitaAsesor = " + intNecesitaAsesor + " WHERE Id = " + idCliente;
        if (connection.executeQuery(updateQuery)) {
            showAlert("Usuario actualizado con éxito");
        } else {
            showAlert("Ha habido un error al crear el usuario");
        }
        connection.close();
        closeWindow();
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
