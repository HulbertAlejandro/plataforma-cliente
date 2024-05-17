package co.uniquindio.plataforma.controladores;

import co.uniquindio.plataforma.exceptions.AtributoVacioException;
import co.uniquindio.plataforma.exceptions.InformacionRepetidaException;
import co.uniquindio.plataforma.modelo.Cliente;
import co.uniquindio.plataforma.modelo.PlataformaCliente;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;

public class registroClienteControlador {

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtRutaArticulos;

    @FXML
    private TextField txtRutaFotos;

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnAtras;

    private final PlataformaCliente plataformaCliente = PlataformaCliente.getInstance();

    public void atras(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnAtras)) {
            plataformaCliente.loadStage("/ventanas/inicioAdmin.fxml", event);
        }
    }

    @FXML
    void registrarCliente() {
        long id = Long.parseLong(txtId.getText());
        String nombre = txtNombre.getText();
        String rutaArticulos = txtRutaArticulos.getText();
        String rutaFotos = txtRutaFotos.getText();

        try{
            String mensaje = plataformaCliente.registrarCliente(
                    id,
                    nombre,
                    rutaArticulos,
                    rutaFotos
            );

            mostrarMensaje(Alert.AlertType.INFORMATION, mensaje);
        } catch (AtributoVacioException | InformacionRepetidaException e){
            mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

}


