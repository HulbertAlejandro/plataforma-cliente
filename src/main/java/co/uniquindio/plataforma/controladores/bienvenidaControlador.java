package co.uniquindio.plataforma.controladores;

import co.uniquindio.plataforma.modelo.PlataformaCliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class bienvenidaControlador {
    @FXML
    private Button btnEnvio,btnProcesamiento,btnSocio,btnAdmin;
    private final PlataformaCliente plataformaCliente = PlataformaCliente.getInstance();
    public void irEnvio(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnEnvio)) {
            plataformaCliente.loadStage("/ventanas/inicioGestorEnvio.fxml", event);
        }
    }
    public void irAdmin(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnAdmin)) {
            plataformaCliente.loadStage("/ventanas/loginAdmin.fxml", event);
        }
    }
        public void irProcesamiento(ActionEvent event) {
            Object evt = event.getSource();
            if (evt.equals(btnProcesamiento)) {
                plataformaCliente.loadStage("/ventanas/inicioGestorProcesamiento.fxml", event);
            }
        }
    public void irSocio(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnSocio)) {
            plataformaCliente.loadStage("/ventanas/login4.fxml", event);
        }
    }
}