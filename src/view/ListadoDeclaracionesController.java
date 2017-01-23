/*
 * TODO: 
    - Cambiar la forma en la que se conecta a la BD para los resultados del cuadro de texto
    - Llenar el TableView con los datos de las parcelas
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import util.Cliente;
import util.ConexionBD;
import util.DeclaracionCultivo;
import util.Parcela;
import view.declaracionesViews.NuevaDeclaracionController;

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
    
    private ObservableList<Cliente> listaClientes;
    List<Cliente> lista;
    private Cliente cliente;
    private DeclaracionCultivo declaracionCultivo;
    private ArrayList<Parcela> listadoParcelas;
    private Connection connection;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTextFieldKeyListener();
        setListViewKeyListener();
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
        cliente = (Cliente) listaResultados.getSelectionModel().getSelectedItem();
        listadoParcelas = new ArrayList<>();
        getDeclaracionDataFromBD();
        
    }
    
    /**
     * Llena las diferentes variables con los datos de la declaración de cultivo
     */
    private void getDeclaracionDataFromBD() {
        
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
        ConexionBD connection = new ConexionBD();
        String query = "SELECT * FROM DeclaracionesCultivo WHERE idCliente = " + cliente.getIdCliente();
        ResultSet results = connection.selectQuery(query);
        try {
            if (!results.next()) {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ListadoDeclaracionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    /**
     * Llena la lista de resultados de clientes con los datos de la BD
     */
    private void getListaClientesFromBD() {
        try {
            ConexionBD connection = new ConexionBD();
            String text = textBusqueda.getText();
            String query = "SELECT Nombre, Apellidos, DNI, Direccion, CP, Localidad, Provincia, Pais, id, NecesitaAsesor, "
                    + "(Nombre||' '||Apellidos) as Cliente "
                    + "FROM Clientes WHERE Nombre||' '||Apellidos LIKE '%" + text + "%'";
            ResultSet result = connection.selectQuery(query);
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
                boolean necesitaAsesorCliente = (result.getInt("NecesitaAsesor") == 0)?false:true;
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
    
    /**
     * Pone el texto del cuadro de búsqueda en blanco
     */
    @FXML private void onClickBusqueda() {
        textBusqueda.setText("");
    }
    
    private void connectToBD() {
        connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:"+Main.rootPath+"cuadernoDB.db");
        } catch (SQLException ex) {
            Logger.getLogger(NuevaDeclaracionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
