package co.uniquindio.plataforma.socket;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC) // Esto asegura que el constructor sea público
public class Mensaje implements Serializable {
    private static final long serialVersionUID = 1L;
    private String tipo;
    private Object contenido;
}

