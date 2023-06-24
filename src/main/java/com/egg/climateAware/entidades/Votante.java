
package com.egg.climateAware.entidades;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)

public class Votante extends Usuario {

    private String nombreApellido;
    private String dni;
    private Integer voto;
    private String direccion;

    @OneToMany
    private List<Publicacion> publicaciones;

}
