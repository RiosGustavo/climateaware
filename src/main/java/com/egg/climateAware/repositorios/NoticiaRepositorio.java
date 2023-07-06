
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

    
    @Query("SELECT no FROM Noticia no WHERE no.blogger.id = :id")
    public List<Noticia> noticiasPorBlogger(@Param("id") String id );
    
}
