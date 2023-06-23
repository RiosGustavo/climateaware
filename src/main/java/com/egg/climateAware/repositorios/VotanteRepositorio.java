
package com.egg.climateAware.repositorios;

import com.egg.climateAware.entidades.Publicacion;
import com.egg.climateAware.entidades.Votante;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface VotanteRepositorio extends JpaRepository<Votante, String> {
    
    @Query("SELECT vo FROM Votante vo WHERE vo.altaBaja = :true")
    public List<Votante> listadoVotantesActivos();
    
    /*
    ////QUISIERA HACER UNA LISTA DE PUBLICACIONES DE UN VOTANTE... NO PUEDO :(
    //@Query("SELECT pu FROM Publicacion pu JOIN pu.Votante vo WHERE vo.id = :idVotante ")
   //@Query("SELECT vo.Publicaciones FROM Votante WHERE vo.id = :idVotante")
    @Query("SELECT pu FROM Publicacion pu  WHERE pu.idPublicacion IN (SELECT vo.Publicaciones.idPublicacion FROM Votante vo WHERE vo.id = :idVotante)")
    public List<Publicacion> litadoPublicacionesVotante(@Param("idVotante") String idVotante);
    */
    @Query("SELECT publicaciones FROM Votante vo WHERE vo.id = :idVotante")
    public List<Publicacion> litadoPublicacionesVotante(@Param("idVotante") String idVotante);
    
}
