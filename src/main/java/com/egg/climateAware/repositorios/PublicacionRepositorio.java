package com.egg.climateAware.repositorios;


import com.egg.climateAware.entidades.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  PublicacionRepositorio extends JpaRepository<Publicacion, String> {
    
}
