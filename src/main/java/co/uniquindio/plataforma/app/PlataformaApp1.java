package co.uniquindio.plataforma.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PlataformaApp1 extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(PlataformaApp1.class.getResource("/ventanas/bienvenida.fxml"));
        //FXMLLoader loader1 = new FXMLLoader(getClass().getResource("inicioSocioPublicador.fxml"));

        Parent parent = loader.load();

        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Plataforma de Noticias");
        stage.show();
    }

    public static void main(String[] args) {
        launch(PlataformaApp1.class, args);
    }
}



