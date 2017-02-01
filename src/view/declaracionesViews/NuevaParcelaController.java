/*
 * TODO: 
    - Aceptar los cambios si pulsa aceptar (y existen cambios)
    - Cancelar: Cierra la ventana
 */
package view.declaracionesViews;

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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.Cliente;
import util.DeclaracionCultivo;

/**
 * FXML Controller class
 *
 * @author ALCAOLIVA
 */
public class NuevaParcelaController implements Initializable {

    @FXML TextField txtIdParcela;
    @FXML TextField txtSizeParcela;
    
    Connection connection;
    DeclaracionCultivo declaracionCultivo;
    Stage stage;
    Cliente cliente;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    /**
     * Pasa datos de la escena a el controlador
     * @param declaracionCultivo Es la declaración de cultivo que tendrá la parcela
     * @param stage Es la ventana dueña de la escena
     */
    public void setData(DeclaracionCultivo declaracionCultivo, Stage stage, Cliente cliente) {
        this.declaracionCultivo = declaracionCultivo;
        this.stage = stage;
        this.cliente = cliente;
    }
    
    /**
     * Acepta la nueva parcela
     */
    @FXML private void aceptar() {
        if (!txtSizeParcela.getText().isEmpty() && !txtIdParcela.getText().isEmpty()) {
            try {
                int idParcela = Integer.parseInt(txtIdParcela.getText());
                double sizeParcela = Double.parseDouble(txtSizeParcela.getText());
                connectToBD();
                String query = "INSERT INTO Parcelas (idParcela, idCliente, idDeclaracionCultivo, sizeDeclaracion) VALUES (?, ?, ?, ?);";
                
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, idParcela);
                preparedStatement.setInt(2, cliente.getIdCliente());
                preparedStatement.setInt(3, declaracionCultivo.getIdDeclaracionCultivo());
                preparedStatement.setDouble(4, sizeParcela);
                preparedStatement.executeUpdate();
                
                query = "UPDATE DeclaracionesCultivo SET totalSize = ? WHERE idDeclaracionCultivo = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setDouble(1, declaracionCultivo.getTotalSize() + sizeParcela);
                preparedStatement.setInt(2, declaracionCultivo.getIdDeclaracionCultivo());
                preparedStatement.executeUpdate();
                
                connection.close();
                closeWindow();
            } catch (SQLException ex) {
                Logger.getLogger(NuevaParcelaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
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
    
    /**
     * Cierra la ventana
     */
    @FXML private void closeWindow() {
        stage.close();
    }
    
    /**
     * Conecta a la base de datos
     */
    public void connectToBD() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:"+Main.rootPath+"cuadernoDB.db");
    }
    
}
