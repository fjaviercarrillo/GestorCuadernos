/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import application.Main;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import util.Aplicador;
import util.Asesor;
import util.Cliente;
import util.Equipo;

/**
 * FXML Controller class
 *
 * @author ALCAOLIVA
 */
public class OtrosDatosController implements Initializable {

    /**
     * Variables FXML
     */
    @FXML Label lblOrdenAplicador1;
    @FXML Label lblOrdenAplicador2;
    @FXML TextField txtNombreAplicador1;
    @FXML TextField txtNombreAplicador2;
    @FXML TextField txtNIFAplicador1;
    @FXML TextField txtNIFAplicador2;
    @FXML TextField txtROPOAplicador1;
    @FXML TextField txtROPOAplicador2;
    @FXML ToggleGroup tipoCarnet1;
    @FXML ToggleGroup tipoCarnet2;
    
    @FXML Label lblOrdenEquipo1;
    @FXML Label lblOrdenEquipo2;
    @FXML TextField txtDescripcionEquipo1;
    @FXML TextField txtDescripcionEquipo2;
    @FXML TextField txtROMAEquipo1;
    @FXML TextField txtROMAEquipo2;
    @FXML TextField txtAdquisicionEquipo1;
    @FXML TextField txtAdquisicionEquipo2;
    @FXML TextField txtInspeccionEquipo1;
    @FXML TextField txtInspeccionEquipo2;
    
    @FXML TextField txtNombreAsesor;
    @FXML TextField txtNIFAsesor;
    @FXML TextField txtIdentificacionAsesor;
    @FXML TextField txtTipoAsesor;
    
    /**
     * Fin variables FXML
     * 
     * Otras variables
     */
    
    int ordenAplicador1, ordenAplicador2, ordenEquipo1, ordenEquipo2;
    String nombreAplicador1, nombreAplicador2, NIFAplicador1, NIFAplicador2, ROPOAplicador1, ROPOAplicador2;
    String descripcionEquipo1, descripcionEquipo2, ROMAEquipo1, ROMAEquipo2, adquisicionEquipo1, adquisicionEquipo2, inspeccionEquipo1, inspeccionEquipo2;
    String nombreAsesor, NIFAsesor, identificacionAsesor, tipoAsesor;
    boolean carnetAplicador1, carnetAplicador2; // False si es básico, true si es cualificado
    RadioButton selectedCarnetAplicador1, selectedCarnetAplicador2;
    
    Aplicador aplicador1, aplicador2;
    Equipo equipo1, equipo2;
    Asesor asesor;
    Cliente cliente;
    
    Connection connection;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML public void accept() {
        /**
         * TODO:
         * - Añadir a la base de datos
         */
        // Comprobamos que el aplicador 1 no esté vacío
        if (!txtNombreAplicador1.getText().isEmpty() && !txtNIFAplicador1.getText().isEmpty() && 
                !txtROPOAplicador1.getText().isEmpty() && !txtDescripcionEquipo1.getText().isEmpty()){
            getAplicador1Data();
            getEquipo1Data();
            
            // Comprobamos si el aplicador 2 existe y cogemos los datos en dicho caso
            if (!txtNombreAplicador2.getText().isEmpty() && !txtNIFAplicador2.getText().isEmpty() && 
                    !txtROPOAplicador2.getText().isEmpty()) {
                getAplicador2Data();
            } else {
                aplicador2 = null;
            }
            
            //Comprobamos si existe la maquinaria 2
            if (!txtDescripcionEquipo2.getText().isEmpty()) {
                getEquipo2Data();
            } else {
                equipo2 = null;
            }

            //Comprobamos si hay asesor
            if (!txtNombreAsesor.getText().isEmpty() && !txtNIFAsesor.getText().isEmpty() && 
                    !txtIdentificacionAsesor.getText().isEmpty() && !txtTipoAsesor.getText().isEmpty()) {
                getAsesorData();
            }
            
            addDataToBD();
        } else {
            showAlert("Tienes que rellenar los datos de al menos un aplicador y una máquina");
        }
    }
    
    private void getAplicador1Data() {
        ordenAplicador1 = 1;
        nombreAplicador1 = txtNombreAplicador1.getText();
        NIFAplicador1 = txtNIFAplicador1.getText();
        ROPOAplicador1 = txtROPOAplicador1.getText();
        selectedCarnetAplicador1 = (RadioButton)tipoCarnet1.getSelectedToggle();
        carnetAplicador1 = !selectedCarnetAplicador1.getId().equals("basico1");
        aplicador1 = new Aplicador(ordenAplicador1, nombreAplicador1, NIFAplicador1, ROPOAplicador1, carnetAplicador1);
    }
    
    private void getAplicador2Data() {
        ordenAplicador2 = 2;
        nombreAplicador2 = txtNombreAplicador2.getText();
        NIFAplicador2 = txtNIFAplicador2.getText();
        ROPOAplicador2 = txtROPOAplicador2.getText();
        selectedCarnetAplicador2 = (RadioButton)tipoCarnet2.getSelectedToggle();
        carnetAplicador2 = !selectedCarnetAplicador2.getId().equals("basico2");
        aplicador2 = new Aplicador(ordenAplicador2, nombreAplicador2, NIFAplicador2, ROPOAplicador2, carnetAplicador2);
    }
    
    private void getEquipo1Data() {
        ordenEquipo1 = 1;
        descripcionEquipo1 = txtDescripcionEquipo1.getText();
        ROMAEquipo1 = txtROMAEquipo1.getText();
        adquisicionEquipo1 = txtAdquisicionEquipo1.getText();
        inspeccionEquipo1 = txtInspeccionEquipo1.getText();
        equipo1 = new Equipo(ordenEquipo1, descripcionEquipo1, ROMAEquipo1, adquisicionEquipo1, inspeccionEquipo1);
    }
    
    private void getEquipo2Data() {
        ordenEquipo2 = 2;
        descripcionEquipo2 = txtDescripcionEquipo2.getText();
        ROMAEquipo2 = txtROMAEquipo2.getText();
        adquisicionEquipo2 = txtAdquisicionEquipo2.getText();
        inspeccionEquipo2 = txtInspeccionEquipo2.getText();
        equipo2 = new Equipo(ordenEquipo2, descripcionEquipo2, ROMAEquipo2, adquisicionEquipo2, inspeccionEquipo2);
    }
    
    private void getAsesorData() {
        nombreAsesor = txtNombreAsesor.getText();
        NIFAsesor = txtNIFAsesor.getText();
        identificacionAsesor = txtIdentificacionAsesor.getText();
        tipoAsesor = txtTipoAsesor.getText();
        asesor = new Asesor(nombreAsesor, NIFAsesor, identificacionAsesor, tipoAsesor);
    }
    
    private void addDataToBD() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:"+Main.rootPath+"cuadernoDB.db");
        } catch (SQLException ex) {
            Logger.getLogger(OtrosDatosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void addAplicadorToBD(Aplicador aplicador) throws SQLException {
        // TODO Si el aplicador no existía, añadirlo a la BD, y si existía comparar por si ha habido cambios, si no ha habido cambios no hacer nada.
    }
    
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error al añadir los datos");
        alert.setContentText(msg);
        alert.showAndWait();
    } 
}
