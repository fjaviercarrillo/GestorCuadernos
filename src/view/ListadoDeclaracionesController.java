/*
 * TODO: 
    - Habilitar botones para editar y eliminar la declaración de cultivo
    - (Idea) Poner una vista en miniatura de la imagen de la declaración de cultivo
 */
package view;

import application.Main;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import util.Cliente;
import util.ConexionBD;
import util.DeclaracionCultivo;
import util.Parcela;
import view.declaracionesViews.EditarParcelaController;
import view.declaracionesViews.NuevaDeclaracionController;
import view.declaracionesViews.NuevaParcelaController;

/**
 * FXML Controller class
 *
 * @author Sk
 */
public class ListadoDeclaracionesController implements Initializable {

    final int ROW_HEIGHT = 24;
    
    @FXML private ListView listaResultados;
    @FXML private TextField textBusqueda;
    @FXML private AnchorPane paneDatosDeclaracion;
    @FXML private Label lblSuperficieTotal;
    @FXML private Label lblClienteDeclaracion;
    @FXML private TableView tablaParcelas;
    @FXML private TableColumn columnaIdParcela;
    @FXML private TableColumn columnaSizeParcela;
    @FXML private Button btnImage;
    
    private ObservableList<Cliente> listaClientes;
    List<Cliente> lista;
    private Cliente cliente;
    private DeclaracionCultivo declaracionCultivo;
    private ObservableList<Parcela> listadoParcelas;
    private Connection connection;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTextFieldKeyListener();
        setListViewKeyListener();
        connectToBD();
    }    
    
    /**
     * Añade los listeners para el cuadro de texto
     */
    private void setTextFieldKeyListener() {
        textBusqueda.setOnKeyPressed(textField -> {
            KeyCode key = textField.getCode();
            if (key == KeyCode.DOWN) {
                // Si la tecla pulsada es abajo selecciona el resultado inferior
                listaResultados.requestFocus();
                listaResultados.getSelectionModel().select(0);
                listaResultados.getFocusModel().focus(0);
            } else if (key == KeyCode.UP) {
                // Si la tecla pulsada es arriba selecciona el resultado anterior
                listaResultados.requestFocus();
                listaResultados.getSelectionModel().select(listaResultados.getItems().size()-1);
                listaResultados.getFocusModel().focus(listaResultados.getItems().size()-1);
            }
        });
    }
    
    // TODO: Arreglar este código para que al pulsar abajo estando en el último elemento del ListView ponga el foco en el primero
    private void setListViewKeyListener() {
        listaResultados.setOnKeyPressed(listView -> {
            KeyCode key = listView.getCode();
            if (key == KeyCode.ENTER) {
                cliente = (Cliente) listaResultados.getSelectionModel().getSelectedItem();
                if (comprobarDeclaracion()) {
                    fillDeclaracionData();
                } else {
                    dialogCreateDeclaracion();
                }
            }
        });
    }
    
    /**
     * Rellena la ventana con todos los datos de la declaración de cultivo del usuario seleccionado
     */
    private void fillDeclaracionData() {
        listadoParcelas = FXCollections.observableArrayList();
        try {
            getDataFromBD();
            lblClienteDeclaracion.setText(cliente.getNombre() + " " + cliente.getApellidos());
            lblSuperficieTotal.setText("Superficie total: " + String.valueOf(declaracionCultivo.getTotalSize()) + " ha");
            btnImage.setOnAction(bt -> {
                try {
                    Runtime.getRuntime().exec("powershell -c "+Main.rootPath + "img\\declaraciones\\" + declaracionCultivo.getImg1());
                } catch (IOException ex) {
                    Logger.getLogger(ListadoDeclaracionesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            addDataToTable();
            connection.close();
        } catch (SQLException ex) {
            dialogAlert("Ha habido un error al coger los datos de la declaración de cultivo de la base de datos", Main.DIALOG_ERROR);
        }
    }
    
    /**
     * Coge todos los datos de la base de datos
     * @throws SQLException Lanza una excepción si hay algún problema al coger algún dato de la BD
     */
    private void getDataFromBD() throws SQLException {
        getDeclaracionDataFromBD();
        getParcelasDataFromBD();
    }
    
    /**
     * Coge los datos de la declaración de cultivo
     * @throws SQLException Lanza una excepción si hay algún problema con la consulta
     */
    private void getDeclaracionDataFromBD() throws SQLException {
        int idDeclaracionCultivo, idCliente;
        boolean necesitaAsesor;
        String imgName1, imgName2;
        Double totalSize;
        
        ResultSet results = null;
        Statement statement = connection.createStatement();        
        String query = "SELECT * FROM DeclaracionesCultivo WHERE idCliente = "
                + "(SELECT id FROM Clientes WHERE DNI = '" + cliente.getDNI() + "');";
        results = statement.executeQuery(query);
        results.next();
        
        idDeclaracionCultivo = results.getInt("idDeclaracionCultivo");
        idCliente = results.getInt("idCliente");
        necesitaAsesor = results.getBoolean("necesitaAsesor");
        imgName1 = results.getString("imgName1");
        imgName2 = results.getString("imgName2");
        totalSize = results.getDouble("totalSize");
        declaracionCultivo = new DeclaracionCultivo(idDeclaracionCultivo, idCliente, totalSize, necesitaAsesor, imgName1, imgName2);
        results.close();
        statement.close();
    }
    
    /**
     * Coge los datos de las parcelas de la base de datos
     * @throws SQLException Lanza una excepción si hay algún problema con la consulta
     */
    private void getParcelasDataFromBD() throws SQLException {
        ResultSet results;
        Statement statement = connection.createStatement();
        int idParcela, keyParcela;
        double sizeParcela;
        String query = "SELECT * FROM Parcelas WHERE idDeclaracionCultivo = " + declaracionCultivo.getIdDeclaracionCultivo();
        results = statement.executeQuery(query);
        while (results.next()) {
            idParcela = results.getInt("idParcela");
            sizeParcela = results.getDouble("sizeDeclaracion");
            keyParcela = results.getInt("keyParcela");
            listadoParcelas.add(new Parcela(idParcela, sizeParcela, keyParcela));
        }
        results.close();
        statement.close();
    }
    
    /**
     * Añade los datos de las parcelas a la tabla
     */
    private void addDataToTable() {
        columnaIdParcela.setCellValueFactory(new PropertyValueFactory<>("idParcela"));
        columnaSizeParcela.setCellValueFactory(new PropertyValueFactory<>("sizeParcela"));
        tablaParcelas.setItems(listadoParcelas);
        tablaParcelas.setOnKeyPressed(tv -> {
            KeyCode key = tv.getCode();
            if (key == KeyCode.ENTER) {
                updateParcela();
            }
        });
    }
    
    /**
     * Crea la ventana para la creación de una nueva declaración de cultivo
     */
    private void dialogCreateDeclaracion() {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Nueva declaración de cultivo");
        dialog.setHeaderText("El cliente no tiene ninguna declaración de cultivo");
        dialog.setContentText("¿Desea crear una nueva declaración de cultivo para el cliente?");
        
        Optional<ButtonType> resultDialog = dialog.showAndWait();
        if (resultDialog.get() == ButtonType.OK) {
            try {
                // TODO Crear nueva ventana para crear una Declaración de cultivo
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/declaracionesViews/NuevaDeclaracion.fxml"));
                AnchorPane nuevaDeclaracionLayout = (AnchorPane) loader.load();
                
                Stage secondStage = new Stage();
                secondStage.setTitle("Nueva declaración");
                Scene scene = new Scene(nuevaDeclaracionLayout);
                secondStage.setScene(scene);
                
                NuevaDeclaracionController controller = loader.getController();
                controller.setData((Cliente) listaResultados.getSelectionModel().getSelectedItem(), secondStage, scene);
                
                secondStage.showAndWait();
                
                fillDeclaracionData();
            } catch (IOException ex) {
                Logger.getLogger(ListadoDeclaracionesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
    
    /**
     * Comprueba si ya existe una declaración de cultivo para el cliente seleccionado
     * @return true si existe, false en el caso contrario
     */
    private boolean comprobarDeclaracion() {
        String query = "SELECT * FROM DeclaracionesCultivo WHERE idCliente = " + cliente.getIdCliente();
        Statement statement;
        ResultSet results;
        boolean exists = true;
        try {
            statement = connection.createStatement();
            results = statement.executeQuery(query);
            if (!results.next()) {
                exists = false;
            }
            statement.close();
            results.close();
        } catch (SQLException ex) {
            Logger.getLogger(ListadoDeclaracionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exists;
    }
    
    /**
     * Llena la lista de resultados de clientes con los datos de la BD
     */
    private void getListaClientesFromBD() {
        try {
            Statement statement = connection.createStatement();
            String text = textBusqueda.getText();
            String query = "SELECT Nombre, Apellidos, DNI, Direccion, CP, Localidad, Provincia, Pais, id, NecesitaAsesor, "
                    + "(Nombre||' '||Apellidos) as Cliente "
                    + "FROM Clientes WHERE Nombre||' '||Apellidos LIKE '%" + text + "%'";
            ResultSet result = statement.executeQuery(query);
            lista = new ArrayList<>();
            while (result.next()) {
                String nombreCliente = result.getString("Nombre");
                String apellidosCliente = result.getString("Apellidos");
                String DNICliente = result.getString("DNI");
                String direccionCliente = result.getString("Direccion");
                int codigoPostal = result.getInt("CP");
                String localidadCliente = result.getString("Localidad");
                String provinciaCliente = result.getString("Provincia");
                String paisCliente = result.getString("Pais");
                int idCliente = result.getInt("id");
                boolean necesitaAsesorCliente = (result.getInt("NecesitaAsesor") != 0);
                lista.add(new Cliente(nombreCliente, apellidosCliente, DNICliente, direccionCliente, codigoPostal,
                        localidadCliente, provinciaCliente, paisCliente, necesitaAsesorCliente, idCliente));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ListadoDeclaracionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Busca los clientes de la base de datos que coincidan con lo escrito en el cuadro de texto
     */
    @FXML private void busquedaCliente() {
        listaResultados.getItems().clear();
        if (textBusqueda.getText().isEmpty()) {
            listaResultados.setOpacity(0.0);
        } else {
            listaResultados.setOpacity(100.0);
        }
        getListaClientesFromBD();
        listaClientes = FXCollections.observableList(lista);
        listaResultados.setItems(listaClientes);
        listaResultados.setCellFactory(
            new Callback<ListView<Cliente>, ListCell<Cliente>>() {

                @Override
                public ListCell<Cliente> call(ListView<Cliente> cliente) {

                        ListCell<Cliente> cell = new ListCell<Cliente>(){

                        @Override
                        protected void updateItem(Cliente c, boolean bln) {
                            super.updateItem(c, bln);
                            if (c != null) {
                                setText(c.getNombre() + " " + c.getApellidos());
                            }
                        }
                    };
                    return cell;
                }
            }
        );
        listaResultados.setPrefHeight(listaClientes.size() * ROW_HEIGHT + 2);
    }
    
    @FXML private void newParcela() {
        try {
            // Carga el archivo fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/declaracionesViews/NuevaParcela.fxml"));
            AnchorPane newParcelaLayout = (AnchorPane) loader.load();
            
            // Crea la nueva ventana
            Stage secondStage = new Stage();
            secondStage.setTitle("Nueva parcela");
            Scene scene = new Scene(newParcelaLayout);
            secondStage.setScene(scene);
            
            //Pasa el cliente al controlador
            NuevaParcelaController controller = loader.getController();
            controller.setData(declaracionCultivo, secondStage, cliente);
            secondStage.showAndWait();
            
            listadoParcelas.clear();
            fillDeclaracionData();
        } catch (IOException ex) {
            Logger.getLogger(ListadoDeclaracionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML private void updateParcela() {
        Parcela selectedParcela = (Parcela) tablaParcelas.getSelectionModel().getSelectedItem();
        if (selectedParcela != null) {
            try {
                // Carga el archivo fxml
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/declaracionesViews/EditarParcela.fxml"));
                AnchorPane updateClienteLayout = (AnchorPane) loader.load();

                // Crea la nueva ventana
                Stage secondStage = new Stage();
                secondStage.setTitle("Editar parcela");
                Scene scene = new Scene(updateClienteLayout);
                secondStage.setScene(scene);

                //Pasa el cliente al controlador
                EditarParcelaController controller = loader.getController();
                controller.setData(selectedParcela, declaracionCultivo, secondStage);
                secondStage.showAndWait();

                listadoParcelas.clear();
                fillDeclaracionData();
            } catch (IOException ex) {
                Logger.getLogger(ListadoDeclaracionesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Pone el texto del cuadro de búsqueda en blanco
     */
    @FXML private void onClickBusqueda() {
        textBusqueda.setText("");
    }
    
    /**
     * Conecta a la base de datos
     */
    private void connectToBD() {
        connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:"+Main.rootPath+"cuadernoDB.db");
        } catch (SQLException ex) {
            Logger.getLogger(NuevaDeclaracionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Muestra un mensaje en pantalla con el texto que le pasemos
     * @param msg Es el texto a mostrar
     */
    private void dialogAlert(String msg, int dialogType) {
        Alert alert;
        if (dialogType == Main.DIALOG_ERROR) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Éxito");
        }
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
