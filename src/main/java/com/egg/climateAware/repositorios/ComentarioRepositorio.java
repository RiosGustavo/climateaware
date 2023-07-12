package com.egg.climateAware.repositorios;

import com.egg.climateAware.entidades.Comentario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepositorio extends JpaRepository<Comentario, String> {

    @Query("SELECT co FROM Comentario co WHERE co.campana.idCampana = :idCampana AND co.altaBaja = true ORDER BY co.fechaPublicacion DESC")
    public List<Comentario> comentariosPorCampana(@Param("idCampana") String idCampana);

}
