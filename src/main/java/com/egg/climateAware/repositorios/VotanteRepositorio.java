
package com.egg.climateAware.repositorios;

import com.egg.climateAware.entidades.Votante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface VotanteRepositorio extends JpaRepository<Votante, String> {
    
}
