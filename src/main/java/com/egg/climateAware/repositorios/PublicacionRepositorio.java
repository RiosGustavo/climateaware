package com.egg.climateAware.repositorios;


import com.egg.climateAware.entidades.Publicacion;
//import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface  PublicacionRepositorio extends JpaRepository<Publicacion, String> {
    
    
            
    @Query("SELECT pu FROM Publicacion pu WHERE pu.altaBaja = true")
    public List<Publicacion> listadoPublicacionesActivas();
    
    @Query("SELECT pu FROM Publicacion pu WHERE pu.altaBaja = true")
    public List<Publicacion> publicacionesPorCampana();
    
     @Query("SELECT pu FROM Publicacion pu WHERE pu.titulo LIKE %:titulo%")
    public List<Publicacion> buscarPorTitulo (@Param("titulo") String titulo );
    
    @Query("SELECT pu FROM Publicacion pu WHERE pu.votante.id = :id")
    public List<Publicacion> publicacionesPorVotante(@Param("id") String id );
    
    
    @Query("SELECT pu FROM Publicacion pu WHERE pu.campana.idCampana = :idCampana")
    public List<Publicacion> buscarPorCampana (@Param("idCampana") String idCampana  );
    
    
    @Query("SELECT pu FROM Publicacion pu WHERE pu.campana.idCampana = :idCampana AND pu.altaBaja = true")
    public List<Publicacion> publicacionesActivasPorCampana (@Param("idCampana") String idCampana  );
    
    
    @Query(value = "SELECT * FROM Publicacion order by fecha_alta desc", nativeQuery = true)
    List<Publicacion> findAllOrderByfecha_altaDesc();
    
    @Query(value="select * from publicacion where votante_id=:id and campana_id_campana = :idCampana limit 1", nativeQuery = true)
    public Publicacion  buscarPorCampanaporUsuario (@Param("idCampana") String idCampana,@Param("id") String id  );
    
}
