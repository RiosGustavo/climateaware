
package com.egg.climateAware.repositorios;

import com.egg.climateAware.entidades.Noticia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticiaRepositorio extends JpaRepository<Noticia, String> {

    @Query("SELECT no FROM Noticia no WHERE no.altaBaja = true")
    public List<Noticia> listadoNoticiasActivas();
    
    @Query(value = "select * from Noticia order by fecha_alta desc", nativeQuery = true)
    List<Noticia> findAllOrderByfecha_altaDesc();
    
 @Query("SELECT ca FROM Noticia ca WHERE ca.titulo LIKE %:titulo%")
    public List<Noticia> buscarPorTitulo (@Param("titulo") String titulo );
    
    @Query("SELECT no FROM Noticia no WHERE no.blogger.id = :id")
    public List<Noticia> noticiasPorBlogger(@Param("id") String id );
    
}
