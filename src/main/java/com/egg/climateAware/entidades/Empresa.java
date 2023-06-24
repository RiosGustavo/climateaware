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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString

public class Empresa extends Usuario {

    private String nombreEmpresa;
    private String cuit;
    private String direccion;
    private String rubro;

    @OneToMany
    private List<Campaña> campañas;
}
