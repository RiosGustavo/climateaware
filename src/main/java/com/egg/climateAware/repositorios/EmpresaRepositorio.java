
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
    
     @Query("SELECT ca FROM Campana ca WHERE ca.idCampana = :idCampana")
    public List<Campana> buscarPorCampana (@Param("idCampana") String idCampana  );

    
    
    
    
    
    
    
}
