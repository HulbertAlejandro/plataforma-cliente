package co.uniquindio.plataforma.controladores;

import co.uniquindio.plataforma.modelo.PlataformaCliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class inicioGestorEnvioControlador {

    @FXML
    private Button btnIniciar;

    @FXML
    private Button btnAtras;

    private final PlataformaCliente plataformaCliente = PlataformaCliente.getInstance();

    @FXML
    private void procesarNoticiasCsv() {
        plataformaCliente.procesarNoticiasCsv();
        mostrarMensaje(Alert.AlertType.INFORMATION, "Noticias procesadas correctamente");
    }

    public void atras(ActionEvent event) {
        plataformaCliente.loadStage("/ventanas/bienvenida.fxml", event);
    }


    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
}
