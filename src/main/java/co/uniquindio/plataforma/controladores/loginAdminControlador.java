package co.uniquindio.plataforma.controladores;

import co.uniquindio.plataforma.modelo.PlataformaCliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class loginAdminControlador {
    @FXML
    private TextField usuario;
    @FXML
    private PasswordField btnContrasena;
    @FXML
    private Button btnAtras;

    private final PlataformaCliente plataformaCliente = PlataformaCliente.getInstance();
    public void iniciarSesion(ActionEvent event) {
        try {

            if (usuario.getText().equals("admin") && btnContrasena.getText().equals("admin")) {
                plataformaCliente.loadStage("/ventanas/inicioAdmin.fxml", event);
            }
            else{
                // Acceso incorrecto
                mostrarMensaje(Alert.AlertType.ERROR, "Credenciales incorrectas");
            }
        } catch (Exception e) {
            mostrarMensaje(Alert.AlertType.ERROR, "Error durante el inicio de sesión");
        }
    }
    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
    public void atras(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnAtras)) {
            plataformaCliente.loadStage("/ventanas/bienvenida.fxml", event);
        }

    }


}

