package com.egg.climateAware.repositorios;

import com.egg.climateAware.entidades.Votante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface VotanteRepositorio extends JpaRepository<Votante, String> {

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Votante buscarPorEmail(@Param("email") String email);
}
