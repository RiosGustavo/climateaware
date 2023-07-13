package com.egg.climateAware.repositorios;

import com.egg.climateAware.entidades.Blogger;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BloggerRepositorio extends JpaRepository<Blogger, String> {

    @Query("SELECT vot FROM Blogger vot WHERE (:termino IS NULL OR CONCAT(vot.nombreApellido,vot.email ,vot.direccion, vot.dni, vot.id) LIKE %:termino%) "
            + "AND (:estado IS NULL OR vot.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE vot.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN vot.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN vot.fechaAlta END DESC")
    public List<Blogger> search(@Param("termino") String termino, @Param("estado") String estado, @Param("orden") String orden);

    @Query("SELECT vot FROM Blogger vot WHERE (:estado IS NULL OR vot.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE vot.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN vot.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN vot.fechaAlta END DESC")
    public List<Blogger> search2(@Param("estado") String estado, @Param("orden") String orden);

}
