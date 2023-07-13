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

    @Query("SELECT vot FROM Votante vot WHERE vot.email = :email")
    public Votante buscarPorEmail(@Param("email") String email);

    @Query("SELECT vot FROM Votante vot WHERE vot.altaBaja = :true")
    public List<Votante> listadoVotantesActivos();

    @Query("SELECT publicaciones FROM Votante vo WHERE vo.id = :idVotante")
    public List<Publicacion> litadoPublicacionesVotante(@Param("idVotante") String idVotante);

    @Query("SELECT vot FROM Votante vot WHERE (:termino IS NULL OR CONCAT(vot.nombreApellido,vot.email, vot.direccion, vot.dni, vot.id) LIKE %:termino%) "
            + "AND (:estado IS NULL OR vot.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE vot.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN vot.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN vot.fechaAlta END DESC")
    public List<Votante> search(@Param("termino") String termino, @Param("estado") String estado, @Param("orden") String orden);

    @Query("SELECT vot FROM Votante vot WHERE (:estado IS NULL OR vot.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE vot.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN vot.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN vot.fechaAlta END DESC")
    public List<Votante> search2(@Param("estado") String estado, @Param("orden") String orden);

}
