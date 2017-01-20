/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author ALCAOLIVA
 */
public class Main extends Application {
    
    public static String rootPath;
    public static String pathSk = "C:\\Users\\Sk\\Documents\\NetBeansProjects\\GestorCuadernos\\src\\";
    public static String pathAlcaoliva = "C:\\Users\\ALCAOLIVA\\Documents\\NetBeansProjects\\GestorCuadernos\\src\\";
    
    private Stage mainStage;
    public VBox rootLayout;
    
    @FXML private AnchorPane bodyPane;
    
    
    /**
     * Se ejecuta al iniciar el apartado gráfico
     */
    public void initialize(){
        rootPath = pathSk;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        initRootLayout();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Start the root layout
     */
    private void initRootLayout(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Main.fxml"));
            rootLayout = (VBox) loader.load();
            Scene scene = new Scene(rootLayout);
            mainStage.setScene(scene);
            mainStage.setTitle("Gestor de cuadernos de explotación agrícola");
            mainStage.show();
            //mainStage.setMaximized(true);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    /**
     * Al hacer click en clientes cargamos la vista Clientes
     */
    @FXML public void onClienteClicked(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ListadoClientes.fxml"));
            AnchorPane listadoClientesLayout = (AnchorPane) loader.load();
            AnchorPane.setBottomAnchor(listadoClientesLayout, 0.0);
            AnchorPane.setLeftAnchor(listadoClientesLayout, 0.0);
            AnchorPane.setRightAnchor(listadoClientesLayout, 0.0);
            AnchorPane.setTopAnchor(listadoClientesLayout, 0.0);
            bodyPane.getChildren().clear();
            bodyPane.getChildren().add(listadoClientesLayout);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    @FXML public void onDeclaracionClicked() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ListadoDeclaraciones.fxml"));
            AnchorPane listadoDeclaracionesLayout = (AnchorPane) loader.load();
            AnchorPane.setBottomAnchor(listadoDeclaracionesLayout, 0.0);
            AnchorPane.setLeftAnchor(listadoDeclaracionesLayout, 0.0);
            AnchorPane.setRightAnchor(listadoDeclaracionesLayout, 0.0);
            AnchorPane.setTopAnchor(listadoDeclaracionesLayout, 0.0);
            bodyPane.getChildren().clear();
            bodyPane.getChildren().add(listadoDeclaracionesLayout);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
