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
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.DeclaracionCultivo;
import util.Parcela;

/**
 * FXML Controller class
 *
 * @author ALCAOLIVA
 */
public class EditarParcelaController implements Initializable {

    @FXML TextField txtIdParcela;
    @FXML TextField txtSizeParcela;
    
    Parcela parcela;
    Connection connection;
    DeclaracionCultivo declaracionCultivo;
    Stage stage;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    /**
     * Recibe objetos desde la escena principal
     * @param parcela La parcela elegida para editar
     */
    public void setData(Parcela parcela, DeclaracionCultivo declaracionCultivo, Stage stage) {
        this.parcela = parcela;
        this.declaracionCultivo = declaracionCultivo;
        this.stage = stage;
        fillData();
    }
    
    public void fillData() {
        txtIdParcela.setText(String.valueOf(parcela.getIdParcela()));
        txtSizeParcela.setText(String.valueOf(parcela.getSizeParcela()));
    }
    
    @FXML private void aceptar() {
        int idParcela;
        double sizeParcela;
        if (!txtIdParcela.getText().isEmpty() && !txtSizeParcela.getText().isEmpty()) {
            idParcela = Integer.parseInt(txtIdParcela.getText());
            sizeParcela = Double.parseDouble(txtSizeParcela.getText());
            if ((idParcela != parcela.getIdParcela()) || sizeParcela != parcela.getSizeParcela()) {
                try {
                    connectToBD();
                    String query = "UPDATE Parcelas SET idParcela = ?, sizeDeclaracion = ? "
                            + "WHERE keyParcela = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    preparedStatement.setInt(1, idParcela);
                    preparedStatement.setDouble(2, sizeParcela);
                    preparedStatement.setInt(3, parcela.getKeyParcela());

                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    connection.close();
                    closeWindow();
                } catch (SQLException ex) {
                    showAlert("Ha habido un error con la base de datos");
                    Logger.getLogger(EditarParcelaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            showAlert("Los campos no pueden estar vacíos");
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
