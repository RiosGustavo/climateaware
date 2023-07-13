package com.egg.climateAware.repositorios;

import com.egg.climateAware.entidades.Campana;
import com.egg.climateAware.entidades.Empresa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepositorio extends JpaRepository<Empresa, String> {

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Empresa buscarPorEmail(@Param("email") String email);

    @Query("SELECT em FROM Empresa em WHERE (:termino IS NULL OR CONCAT(em.nombreEmpresa, em.direccion, em.rubro, em.cuit, em.email) LIKE %:termino%) "
            + "AND (:estado IS NULL OR em.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE em.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN em.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN em.fechaAlta END DESC")
    public List<Empresa> search(@Param("termino") String termino, @Param("estado") String estado, @Param("orden") String orden);

    @Query("SELECT em FROM Empresa em WHERE (:estado IS NULL OR em.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE em.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN em.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN em.fechaAlta END DESC")
    public List<Empresa> search2(@Param("estado") String estado, @Param("orden") String orden);

}
