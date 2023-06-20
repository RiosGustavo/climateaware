
package com.egg.climateAware.repositorios;

import com.egg.climateAware.entidades.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepositorio  extends JpaRepository<Administrador,String> {
}
