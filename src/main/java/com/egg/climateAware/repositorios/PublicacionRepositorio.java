package com.egg.climateAware.repositorios;


import com.egg.climateAware.entidades.Publicacion;
//import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface  PublicacionRepositorio extends JpaRepository<Publicacion, String> {
    
    
            
     @Query("SELECT pu FROM Publicacion pu WHERE pu.altaBaja = :true")
    public List<Publicacion> listadoPublicacionesActivas();
    
}
