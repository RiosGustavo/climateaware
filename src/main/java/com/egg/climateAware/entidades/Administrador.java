
package com.egg.climateAware.entidades;

import com.egg.climateAware.enumeraciones.Rol;
import java.util.Date;
import javax.persistence.Entity;


@Entity
public class Administrador extends Usuario {

    public Administrador() {
    }
    
    public Administrador(String id, String email, String password, Rol roles, Date fechaAlta, Boolean altaBaja, Imagen imagen) {
        super(id, email, password, roles, fechaAlta, altaBaja, imagen);
    }
    
}
