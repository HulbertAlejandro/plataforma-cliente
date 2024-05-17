package co.uniquindio.plataforma.modelo;

import co.uniquindio.plataforma.app.PlataformaApp1;
import co.uniquindio.plataforma.exceptions.AtributoVacioException;
import co.uniquindio.plataforma.exceptions.InformacionRepetidaException;
import co.uniquindio.plataforma.socket.Mensaje;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Builder;
import lombok.extern.java.Log;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@Log
public class PlataformaCliente {
    private static final String HOST = "localhost";
    private static final int PUERTO = 1000;

    private static PlataformaCliente plataformaCliente;

    public static PlataformaCliente getInstance() {
        if (plataformaCliente == null) {
            plataformaCliente = new PlataformaCliente();
        }

        return plataformaCliente;
    }

    public void loadStage(String url, Event event) {

        try {
            ((Node) (event.getSource())).getScene().getWindow().hide();

            Parent root = FXMLLoader.load(Objects.requireNonNull(PlataformaApp1.class.getResource(url)));
            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Plataforma de Noticias");
            newStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String registrarNoticia(String titulo, String contenido, String autor, LocalDate fecha) throws RuntimeException {

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)) {

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Noticia noticia = new Noticia(titulo, contenido, autor, fecha);

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject(Mensaje.builder()
                    .tipo("agregarNoticia").contenido(noticia).build());

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se retorna a lista de clientes
            return respuesta.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Noticia> listarNoticias(){
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(Mensaje.builder().tipo("listarNoticias").build());

            Object respuesta = in.readObject();

            if (respuesta instanceof ArrayList<?>) {
                @SuppressWarnings("unchecked")
                ArrayList<Noticia> list = (ArrayList<Noticia>) respuesta;
                return list;
            } else {
                throw new RuntimeException("La respuesta del servidor no es del tipo esperado");
            }

        } catch (EOFException eof) {
            // Manejar EOFException específicamente si es necesario
            log.severe("Se alcanzó el final del flujo de entrada inesperadamente.");
            throw new RuntimeException(eof);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String registrarCliente(long id, String nombre, String rutaArticulos, String rutaFotos) throws AtributoVacioException, InformacionRepetidaException {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            Cliente cliente = new Cliente(id, nombre, rutaArticulos, rutaFotos);

            // Enviamos un mensaje al servidor con los datos del cliente a registrar
            out.writeObject(Mensaje.builder()
                    .tipo("registrarCliente")
                    .contenido(cliente)
                    .build());

            // Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            // Cerramos los flujos de entrada y salida para liberar recursos
            in.close();
            out.close();

            // Retornamos la respuesta del servidor
            return respuesta.toString();
        } catch (Exception e) {
            // Capturamos excepciones y las relanzamos como RuntimeException
            throw new RuntimeException(e);
        }
    }


    public String registrarSocioPublicador(long id, String nombre, String rutaArticulos, ArrayList<Noticia> noticias) throws AtributoVacioException, InformacionRepetidaException {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            SocioPublicador socioPublicador = new SocioPublicador(id, nombre, rutaArticulos, noticias);

            // Enviamos un mensaje al servidor con los datos del cliente a registrar
            out.writeObject(Mensaje.builder()
                    .tipo("registrarSocioPublicador")
                    .contenido(socioPublicador)
                    .build());

            // Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            // Cerramos los flujos de entrada y salida para liberar recursos
            in.close();
            out.close();

            // Retornamos la respuesta del servidor
            return respuesta.toString();
        } catch (Exception e) {
            // Capturamos excepciones y las relanzamos como RuntimeException
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Cliente> listarClientes() {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(Mensaje.builder().tipo("listarClientes").build());

            Object respuesta = in.readObject();

            if (respuesta instanceof ArrayList<?>) {
                @SuppressWarnings("unchecked")
                ArrayList<Cliente> list = (ArrayList<Cliente>) respuesta;
                return list;
            } else {
                throw new RuntimeException("La respuesta del servidor no es del tipo esperado");
            }

        } catch (EOFException eof) {
            // Manejar EOFException específicamente si es necesario
            log.severe("Se alcanzó el final del flujo de entrada inesperadamente.");
            throw new RuntimeException(eof);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void cargarNoticiasEnClientes(List<Cliente> clientes, List<Noticia> noticias) {
        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("cargarNoticias").build() );

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se hace un casting de la respuesta Object a un ArrayList<Cliente>
            ArrayList<Cliente> list = (ArrayList<Cliente>) respuesta;

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ArrayList<SocioPublicador> listarSociosPublicadores() {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(Mensaje.builder().tipo("listarSocioPublicador").build());

            Object respuesta = in.readObject();

            if (respuesta instanceof ArrayList<?>) {
                @SuppressWarnings("unchecked")
                ArrayList<SocioPublicador> list = (ArrayList<SocioPublicador>) respuesta;
                return list;
            } else {
                throw new RuntimeException("La respuesta del servidor no es del tipo esperado");
            }

        } catch (EOFException eof) {
            // Manejar EOFException específicamente si es necesario
            log.severe("Se alcanzó el final del flujo de entrada inesperadamente.");
            throw new RuntimeException(eof);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean existeSocioPublicador(long id, String nombre) {
        try (Socket socket = new Socket(HOST, PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            SocioPublicador socioPublicador = new SocioPublicador(id, nombre, null, null);
            out.writeObject(Mensaje.builder()
                    .tipo("existeSocioPublicador")
                    .contenido(socioPublicador)
                    .build());

            Mensaje respuestaMensaje = (Mensaje) in.readObject();
            Object contenido = respuestaMensaje.getContenido();

            if (contenido instanceof Boolean) {
                return (Boolean) contenido; // Conversión segura a booleano
            } else {
                // Si la respuesta no es un booleano, maneja el error adecuadamente
                throw new RuntimeException("Respuesta inesperada del servidor al verificar existencia de SocioPublicador");
            }
        } catch (Exception e) {
            log.severe("Error al comunicarse con el servidor: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public SocioPublicador getSocioPublicador(){

        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("getSocioPublicador").build() );

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se hace un casting de la respuesta Object
            SocioPublicador socioPublicador = (SocioPublicador) respuesta;

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

            //Se retorna el cliente
            return socioPublicador;
        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void convertirNoticias() {
        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("convertirNoticias").build() );

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void procesarNoticiasCsv() {
        //Se intenta abrir una conexión a un servidor remoto usando un objeto Socket
        try (Socket socket = new Socket(HOST, PUERTO)){

            //Se crean flujos de datos de entrada y salida para comunicarse a través del socket
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Se envía un mensaje al servidor con los datos de la petición
            out.writeObject( Mensaje.builder()
                    .tipo("procesarNoticiasCsv").build() );

            //Obtenemos la respuesta del servidor
            Object respuesta = in.readObject();

            //Se cierran los flujos de entrada y de salida para liberar los recursos
            in.close();
            out.close();

        }catch (Exception e){
            log.severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
