package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Ciutat {
    private String nombre;
    private String provincia;
    private Float latitud;
    private Float longitud;

    public Ciutat(String nombre) {
        this.nombre = nombre;
    }

}
