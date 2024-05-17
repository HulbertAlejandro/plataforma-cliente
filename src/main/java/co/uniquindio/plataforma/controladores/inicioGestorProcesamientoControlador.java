package co.uniquindio.plataforma.controladores;

import co.uniquindio.plataforma.modelo.PlataformaCliente;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import co.uniquindio.plataforma.modelo.SocioPublicador;
import co.uniquindio.plataforma.modelo.Noticia;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.ArrayList;

public class inicioGestorProcesamientoControlador {

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private TableView <Noticia> tableView;

    @FXML
    private Button procesarNoticias;

    @FXML
    private Button salir;

    @FXML
    private TableColumn<Noticia, String> titulo;

    @FXML
    private TableColumn<Noticia, String> publicador;

    @FXML
    private TableColumn<Noticia, String> contenido;

    @FXML
    private TableColumn<Noticia, String> fecha;

    private final PlataformaCliente plataformaCliente = PlataformaCliente.getInstance();

    private ArrayList<SocioPublicador> listaSociosPublicadores = plataformaCliente.listarSociosPublicadores();

    public void initialize() {
        // Llenar el ComboBox con los nombres de los socios-publicadores
        for (SocioPublicador socio : listaSociosPublicadores) {
            comboBox.getItems().add(socio.getNombre());
        }

        // Manejar el evento de cambio de selección en el ComboBox
        comboBox.setOnAction(event -> {
            String nombreSocioSeleccionado = comboBox.getValue();
            SocioPublicador socioSeleccionado = null;
            // Buscar el socio publicador seleccionado en la lista
            for (SocioPublicador socio : listaSociosPublicadores) {
                if (socio.getNombre().equals(nombreSocioSeleccionado)) {
                    socioSeleccionado = socio;
                    break;
                }
            }
            // Llenar la tabla con las noticias del socio seleccionado
            if (socioSeleccionado != null) {
                ObservableList<Noticia> noticias = FXCollections.observableArrayList(socioSeleccionado.getNoticias());
                titulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
                publicador.setCellValueFactory(new PropertyValueFactory<>("autor"));
                contenido.setCellValueFactory(new PropertyValueFactory<>("contenido"));
                fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
                tableView.setItems(noticias);
            }
        });
    }

    public void atras(ActionEvent event) {
        plataformaCliente.loadStage("/ventanas/bienvenida.fxml", event);
    }

    @FXML
    private void convertirNoticias() {
        plataformaCliente.convertirNoticias();
        mostrarMensaje(Alert.AlertType.INFORMATION, "Noticias procesadas correctamente");
    }

    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
}
