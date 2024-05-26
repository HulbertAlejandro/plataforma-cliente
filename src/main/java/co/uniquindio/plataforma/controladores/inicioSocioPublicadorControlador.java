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
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
        fileChooser.setTitle("Seleccionar archivos");

        // Configurar extensiones permitidas
        FileChooser.ExtensionFilter extFilterAll = new FileChooser.ExtensionFilter("Todos los archivos soportados", "*.xml", "*.png", "*.jpg", "*.jpeg", "*.zip");
        FileChooser.ExtensionFilter extFilterXML = new FileChooser.ExtensionFilter("Archivos XML (*.xml)", "*.xml");
        FileChooser.ExtensionFilter extFilterImages = new FileChooser.ExtensionFilter("Imágenes (PNG, JPG)", "*.png", "*.jpg", "*.jpeg");
        FileChooser.ExtensionFilter extFilterZip = new FileChooser.ExtensionFilter("Archivos ZIP (*.zip)", "*.zip");

        fileChooser.getExtensionFilters().addAll(extFilterAll, extFilterXML, extFilterImages, extFilterZip);

        // Mostrar el diálogo para seleccionar múltiples archivos
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

        // Si se seleccionan archivos, procesar cada uno
        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                String extension = getFileExtension(file);

                if (extension.equalsIgnoreCase("xml")) {
                    copiarYRegistrarArchivoXML(file);
                } else if (extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
                    manejarImagen(file);
                } else if (extension.equalsIgnoreCase("zip")) {
                    manejarArchivoZip(file);
                }
            }
        } else {
            System.out.println("Ningún archivo seleccionado.");
        }
    }

    private void copiarYRegistrarArchivoXML(File archivoXML) {
        try {
            // Copiar el archivo XML a la carpeta del socio
            String directorioSocio = plataformaCliente.getSocioPublicador().getRutaArticulos() + File.separator + plataformaCliente.getSocioPublicador().getId();
            crearCarpeta(directorioSocio);
            File destino = new File(directorioSocio + File.separator + archivoXML.getName());
            Files.copy(archivoXML.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            mostrarMensaje(Alert.AlertType.INFORMATION, "Archivo XML subido con éxito: " + archivoXML.getName());

            // Leer la noticia desde el archivo XML y registrarla
            Noticia noticia = leerNoticiaDesdeXML(archivoXML.getPath());
            if (noticia != null) {
                String mensaje = plataformaCliente.registrarNoticia(noticia.getTitulo(), noticia.getContenido(), noticia.getAutor(), noticia.getFecha());
                mostrarMensaje(Alert.AlertType.INFORMATION, mensaje);
                llenarTabla();
            } else {
                mostrarMensaje(Alert.AlertType.ERROR, "El archivo XML no contiene una noticia válida.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje(Alert.AlertType.ERROR, "Error al subir el archivo XML: " + archivoXML.getName());
        }
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    private void manejarImagen(File imagen) {
        try {
            String directorioImagenes = plataformaCliente.getSocioPublicador().getRutaArticulos() + File.separator + plataformaCliente.getSocioPublicador().getId();
            crearCarpeta(directorioImagenes);
            Files.copy(imagen.toPath(), new File(directorioImagenes + File.separator + imagen.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            mostrarMensaje(Alert.AlertType.INFORMATION, "Imagen subida con éxito: " + imagen.getName());
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje(Alert.AlertType.ERROR, "Error al subir la imagen: " + imagen.getName());
        }
    }

    private void manejarArchivoZip(File archivoZip) {
        try {
            String directorioZip = plataformaCliente.getSocioPublicador().getRutaArticulos() + File.separator + plataformaCliente.getSocioPublicador().getId();
            crearCarpeta(directorioZip);
            Files.copy(archivoZip.toPath(), new File(directorioZip + File.separator + archivoZip.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            mostrarMensaje(Alert.AlertType.INFORMATION, "Archivo ZIP subido con éxito: " + archivoZip.getName());
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje(Alert.AlertType.ERROR, "Error al subir el archivo ZIP: " + archivoZip.getName());
        }
    }

    public void crearCarpeta(String ruta) {
        File carpeta = new File(ruta);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    private Noticia leerNoticiaDesdeXML(String rutaArchivo) {
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

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
}