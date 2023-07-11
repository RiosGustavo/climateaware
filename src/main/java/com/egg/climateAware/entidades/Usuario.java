package com.egg.climateAware.entidades;

import com.egg.climateAware.enumeraciones.Rol;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString

public class Usuario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")

    protected String id;
    protected String email;
    protected String password;

    @Enumerated(EnumType.STRING)
    protected Rol roles;
    
    @Temporal(TemporalType.DATE)
    protected Date fechaAlta;
    protected Boolean altaBaja;
    
    @OneToOne
    protected Imagen imagen;
    
    @Column(name = "reset_password_token")
    protected String resetPasswordToken;
    
}
