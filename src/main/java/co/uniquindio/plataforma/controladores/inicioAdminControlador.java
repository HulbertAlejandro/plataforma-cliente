package co.uniquindio.plataforma.controladores;

import co.uniquindio.plataforma.modelo.PlataformaCliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class inicioAdminControlador {

    @FXML
    private Button btnRegistrarCliente;

    @FXML
    private Button btnRegistrarSocio;

    @FXML
    private Button btnSalir;

    private final PlataformaCliente plataformaCliente = PlataformaCliente.getInstance();

    public void registrarCliente(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnRegistrarCliente)) {
            plataformaCliente.loadStage("/ventanas/registroCliente.fxml", event);
        }
    }

    public void registrarSocio(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnRegistrarSocio)) {
            plataformaCliente.loadStage("/ventanas/registroSocio.fxml", event);
        }
    }

    public void salir(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnSalir)) {
            plataformaCliente.loadStage("/ventanas/bienvenida.fxml", event);
        }
    }

}
