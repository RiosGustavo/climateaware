
package com.egg.climateAware.repositorios;

import com.egg.climateAware.entidades.Campaña;
import com.egg.climateAware.entidades.Empresa;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CampañaRepositorio extends JpaRepository<Campaña, String>{
   
    @Query("SELECT ca FROM Campaña ca WHERE ca.idCampaña = :idCampaña")
    public Campaña buscarPorId (@Param("idCampaña") String idCampaña );
    
    @Query("SELECT ca FROM Campaña ca WHERE ca.titulo = :titulo")
    public Campaña buscarPorTitulo (@Param("titulo") String titulo );
    
    @Query("SELECT ca FROM Campaña ca WHERE ca.fechaAlta = :fechaAlta")
    public Campaña buscarPorFecha (@Param("fechaAlta") Date fechaAlta );
    
        
    @Query("SELECT em FROM Empresa em WHERE em.nombreEmpresa = :nombreEmpresa")
    public List<Campaña> buscarPorNombreEmpresa (@Param("nombreEmpresa") String nombreEmpresa);
    
    /// ACA ME HACE FALTA IMPORTAR LA ENTIDAD PUBLICACION
    /// revfizar cuadno acutlice el id de Publicación
     @Query("SELECT pu FROM Publicacion pu WHERE pu.id = :id")
    public List<Publicacion> buscarPorPublicacion (@Param("id") String id);
    
    
    
}
