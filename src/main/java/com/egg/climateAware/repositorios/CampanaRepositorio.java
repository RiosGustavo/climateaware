
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
    
    @Query("SELECT ca FROM Campana ca WHERE ca.titulo LIKE %:titulo%")
    public List<Campana> buscarPorTitulo (@Param("titulo") String titulo );
    
    @Query(value = "select * from Campana order by fecha_alta desc", nativeQuery = true)
    List<Campana> findAllOrderByfecha_altaDesc();
    
    @Query("SELECT ca FROM Campana ca WHERE ca.fechaAlta = :fechaAlta")
    public List<Campana> buscarPorFecha (@Param("fechaAlta") Date fechaAlta );
    
     @Query("SELECT ca FROM Campana ca WHERE ca.altaBaja = true")
    public List<Campana> buscarPorEstado ();
    
    @Query("SELECT ca FROM Campana ca WHERE ca.empresa.id = :id")
    public List<Campana> campanasPorEmpresa(@Param("id") String id );

    @Query("SELECT em FROM Empresa em WHERE em.nombreEmpresa = :nombreEmpresa")
    public List<Campana> buscarPorNombreEmpresa (@Param("nombreEmpresa") String nombreEmpresa);
   
     List<Campana> findByAltaBajaTrue();
    
     
     
    @Query("SELECT ca FROM Campana ca WHERE (:termino IS NULL OR CONCAT(ca.titulo, ca.descripcion, ca.empresa.nombreEmpresa) LIKE %:termino%) "
            + "AND (:estado IS NULL OR ca.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE ca.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN ca.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN ca.fechaAlta END DESC")
    public  List<Campana> search(@Param("termino") String termino, @Param("estado") String estado, @Param("orden") String orden);

    @Query("SELECT ca FROM Campana ca WHERE (:estado IS NULL OR ca.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE ca.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN ca.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN ca.fechaAlta END DESC")
    public  List<Campana> search2(@Param("estado") String estado, @Param("orden") String orden);
     
     
     
}
