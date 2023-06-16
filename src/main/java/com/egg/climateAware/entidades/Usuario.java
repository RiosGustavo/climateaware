package com.egg.climateAware.entidades;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString

public class Usuario {

    @Id
    protected String id;
    protected String email;
    protected String password;
    protected Boolean altaBaja;

}
