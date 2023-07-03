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

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Votante buscarPorEmail(@Param("email") String email);

    @Query("SELECT vo FROM Votante vo WHERE vo.altaBaja = :true")
    public List<Votante> listadoVotantesActivos();
    
 
    @Query("SELECT publicaciones FROM Votante vo WHERE vo.id = :idVotante")
    public List<Publicacion> litadoPublicacionesVotante(@Param("idVotante") String idVotante);

    @Query("SELECT vo FROM Votante vo WHERE ((:termino IS NULL OR LOWER(vo.nombreApellido) LIKE %:termino%) OR "
            + "(:termino IS NULL OR vo.dni LIKE %:termino%) OR "
            + "(:termino IS NULL OR vo.email LIKE %:termino%) OR "
            + "(:termino IS NULL OR LOWER(vo.id) LIKE %:termino%))")
    List<Votante> buscarVotantesPorTermino(@Param("termino") String termino);

}
