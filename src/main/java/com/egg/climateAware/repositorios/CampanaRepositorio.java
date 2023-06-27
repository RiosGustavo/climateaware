
package com.egg.climateAware.repositorios;

import com.egg.climateAware.entidades.Campana;
import com.egg.climateAware.entidades.Empresa;
import com.egg.climateAware.entidades.Publicacion;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CampanaRepositorio extends JpaRepository<Campana, String>{
   
    @Query("SELECT ca FROM Campana ca WHERE ca.idCampana = :idCampana")
    public Campana buscarPorId (@Param("idCampana") String idCampana );
    
    @Query("SELECT ca FROM Campana ca WHERE ca.titulo = :titulo")
    public Campana buscarPorTitulo (@Param("titulo") String titulo );
    
    @Query("SELECT ca FROM Campana ca WHERE ca.fechaAlta = :fechaAlta")
    public Campana buscarPorFecha (@Param("fechaAlta") Date fechaAlta );
    
    
    @Query("SELECT ca FROM Campana ca WHERE ca.empresa.id = :id")
    public List<Campana> campanasPorEmpresa(@Param("id") String id );
 
    
    
    
    @Query("SELECT em FROM Empresa em WHERE em.nombreEmpresa = :nombreEmpresa")
    public List<Campana> buscarPorNombreEmpresa (@Param("nombreEmpresa") String nombreEmpresa);
    
   
     @Query("SELECT pu FROM Publicacion pu WHERE pu.idPublicacion = :idPublicacion")
    public List<Publicacion> buscarPorPublicacion (@Param("idPublicacion") String idPublicacion);
    
    
    
}
