
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
    
    @Query("SELECT em FROM Empresa em WHERE em.nombreEmpresa = :nombreEmpresa")
    public Empresa buscarPorNombre (@Param("nombreEmpresa") String nombreEmpresa );
    
    @Query("SELECT em FROM Empresa em WHERE em.cuit = :cuit")
    public Empresa buscarPorCuit (@Param("cuit") String cuit );
    
     @Query("SELECT em FROM Empresa em WHERE em.direccion = :direccion")
    public Empresa buscarPorDireccion (@Param("direccion") String direccion );
    
     @Query("SELECT em FROM Empresa em WHERE em.rubro = :rubro")
    public Empresa buscarPorRubro (@Param("rubro") String rubro );
    
    
     @Query("SELECT em FROM Empresa em WHERE ((:termino IS NULL OR LOWER(em.nombreEmpresa) LIKE %:termino%) OR "
            + "(:termino IS NULL OR em.fechaAlta LIKE %:termino%) OR "
            + "(:termino IS NULL OR em.rubro LIKE %:termino%) OR "
            + "(:termino IS NULL OR em.cuit LIKE %:termino%) OR "
            + "(:termino IS NULL OR em.email LIKE %:termino%) OR "
            + "(:termino IS NULL OR LOWER(em.id) LIKE %:termino%))")
    List<Empresa> buscarEmpresasPorTermino(@Param("termino") String termino);
    
}
