package com.egg.climateAware.repositorios;

import com.egg.climateAware.entidades.Blogger;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BloggerRepositorio extends JpaRepository<Blogger, String> {

    @Query("SELECT blo FROM Blogger blo WHERE ((:termino IS NULL OR LOWER(blo.nombreApellido) LIKE %:termino%) OR "
            + "(:termino IS NULL OR blo.dni LIKE %:termino%) OR "
            + "(:termino IS NULL OR blo.email LIKE %:termino%) OR "
            + "(:termino IS NULL OR LOWER(blo.id) LIKE %:termino%))")
    List<Blogger> buscarBloggersPorTermino(@Param("termino") String termino);

}
