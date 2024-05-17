package co.uniquindio.plataforma.controladores;

import co.uniquindio.plataforma.modelo.Cliente;
import co.uniquindio.plataforma.modelo.Noticia;
import co.uniquindio.plataforma.modelo.PlataformaCliente;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class inicioSocioPublicadorControlador implements Initializable {

    @FXML
    private TableView<Noticia> noticiasTableView;
    @FXML
    private TableColumn<Noticia, String> titulo;
    @FXML
    private TableColumn<Noticia, String> publicador;
    @FXML
    private TableColumn<Noticia, String> contenido;
    @FXML
    private TableColumn<Noticia, String> fecha;
    @FXML
    private TextArea textArea;
    @FXML
    private Button btnAtras;
    @FXML
    private Button btnAnadido, btnXML;
    @FXML
    private TextField txtArea;

    private Stage stage;
    private String rutaPublicadores = "ruta/a/tus/publicadores";
    private final PlataformaCliente plataformaCliente = PlataformaCliente.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Llenar la tabla al abrir la ventana
        llenarTabla();
    }

    @FXML
    private void cargarXML() {
        // Crear un FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");

        // Mostrar el diálogo para seleccionar un archivo
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Si un archivo es seleccionado, cargar la noticia desde el archivo XML
        if (selectedFile != null) {
            Noticia noticia = leerNoticiaDesdeXML(selectedFile.getPath());
            if (noticia != null) {
                // Registrar la noticia en la plataforma
                String mensaje = plataformaCliente.registrarNoticia(noticia.getTitulo(), noticia.getContenido(), noticia.getAutor(), noticia.getFecha());
                mostrarMensaje(Alert.AlertType.INFORMATION, mensaje);

                // Actualizar la tabla después de registrar la noticia
                llenarTabla();
            } else {
                mostrarMensaje(Alert.AlertType.ERROR, "El archivo XML no contiene una noticia válida.");
            }
        } else {
            System.out.println("Ningún archivo seleccionado.");
        }
    }

    public Noticia leerNoticiaDesdeXML(String rutaArchivo) {
        try {
            File archivoXML = new File(rutaArchivo);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(archivoXML);

            doc.getDocumentElement().normalize();

            // Obtener elementos del documento XML según la estructura NITF
            Element head = (Element) doc.getElementsByTagName("head").item(0);
            Element titleElement = (Element) head.getElementsByTagName("title").item(0);
            Element docdata = (Element) head.getElementsByTagName("docdata").item(0);
            Element dateIssueElement = (Element) docdata.getElementsByTagName("date.issue").item(0);
            Element docCopyrightElement = (Element) docdata.getElementsByTagName("doc.copyright").item(0);

            Element body = (Element) doc.getElementsByTagName("body").item(0);
            Element bodyContentElement = (Element) body.getElementsByTagName("body.content").item(0);

            // Extraer datos de los elementos
            String titulo = titleElement.getTextContent();
            String fechaStr = dateIssueElement.getTextContent();
            String publicador = docCopyrightElement.getTextContent();
            String contenido = bodyContentElement.getTextContent();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            LocalDate fechaLocal = LocalDate.parse(fechaStr, formatter);

            // Crear y retornar la instancia de Noticia
            return new Noticia(titulo, contenido, publicador, fechaLocal);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void mostrarMensaje(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

    public void atras(ActionEvent event) {
        plataformaCliente.loadStage("/ventanas/bienvenida.fxml", event);
    }

    private void llenarTabla() {
        titulo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitulo()));
        publicador.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAutor()));
        contenido.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContenido()));
        fecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFecha().toString()));

        // Configurar el formato de la fecha en la tabla
        fecha.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getFecha();
            String formattedDate = (date != null) ? date.toString() : "Fecha no disponible";
            return new SimpleStringProperty(formattedDate);
        });

        noticiasTableView.setItems(FXCollections.observableArrayList(plataformaCliente.getSocioPublicador().getNoticias()));
    }

    @FXML
    private void cargarNoticiasEnClientes() {

        ArrayList<Noticia> noticias = plataformaCliente.listarNoticias(); // Obtener el ArrayList de noticias (por ejemplo)

        if (noticias != null && !noticias.isEmpty()) {
            ArrayList<Cliente> clientes = plataformaCliente.listarClientes(); // Obtener la lista de clientes (por ejemplo)

            if (clientes != null && !clientes.isEmpty()) {
                // Llamar al método para cargar las noticias en las carpetas de los clientes
                plataformaCliente.cargarNoticiasEnClientes(clientes, noticias);
            } else {
                System.out.println("No hay clientes disponibles para cargar las noticias.");
            }
        } else {
            System.out.println("El ArrayList de noticias está vacío o nulo.");
        }
    }
}