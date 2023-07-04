
package com.egg.climateAware.entidades;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity 
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)

public class Publicacion {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")

    
    private String idPublicacion;
    private String titulo;
    private String descripcion;    
    
    @Column(length=65535, columnDefinition="text")
    private String cuerpo;    
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    
     @OneToOne
    private Imagen imagen;
    private String video;
    private Boolean altaBaja;
    
    @OneToMany
    private List<Usuario> votos;
    

}
