
package com.egg.climateAware.servicios;

import com.egg.climateAware.entidades.Imagen;
import com.egg.climateAware.entidades.Publicacion;
import com.egg.climateAware.repositorios.PublicacionRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class PublicacionServicio {

    @Autowired
    private PublicacionRepositorio publicacionRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void crearPublicacion(MultipartFile archivo, String titulo, String descripcion, String cuerpo, String video) throws Exception {
        validar(titulo,descripcion,cuerpo);
        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo(titulo);
        publicacion.setDescripcion(descripcion);
        publicacion.setCuerpo(cuerpo);
        //publicacion.setFechaAlta(new Date());
        publicacion.setFechaAlta(new Date());
        Imagen imagen = imagenServicio.guardar(archivo);
        publicacion.setImagen(imagen);
        publicacion.setAltaBaja(true);
        publicacion.setVideo(video);
        publicacionRepositorio.save(publicacion);
    }
//modificarPublicacion(archivo, id, titulo,descripcion,cuerpo, video);
    @Transactional
    public void modificarPublicacion(MultipartFile archivo, String idPublicacion,String titulo,String descripcion, String cuerpo, String video) throws Exception {

        validar(titulo,descripcion,cuerpo);

        Optional<Publicacion> respuesta = publicacionRepositorio.findById(idPublicacion);

        if (respuesta.isPresent()) {
            Publicacion publicacion = respuesta.get();
            publicacion.setTitulo(titulo);
            publicacion.setDescripcion(descripcion);
            publicacion.setCuerpo(cuerpo);
            publicacion.setVideo(video);
            String idImagen = null;
            if (publicacion.getImagen() != null) {
                idImagen = publicacion.getImagen().getId();
            }
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            publicacion.setImagen(imagen);
            publicacionRepositorio.save(publicacion);
        }

    }

    /*
   @Transactional
    public void eliminarPublicacion(String idPublicacion ) throws Exception { 
            publicacionRepositorio.deleteById(idPublicacion);       
    }
     */
    public Publicacion getOne(String idPublicacion) {
        return publicacionRepositorio.getOne(idPublicacion);
    }

    //@Transactional
    public List<Publicacion> listarPublicaciones() {
        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones = publicacionRepositorio.listadoPublicacionesActivas();        
        return publicaciones;
    }

    @Transactional
    public void bajaPublicacion(String idPublicacion) throws Exception {

        Optional<Publicacion> respuesta = publicacionRepositorio.findById(idPublicacion);

        if (respuesta.isPresent()) {
            Publicacion publicacion = respuesta.get();
            publicacion.setAltaBaja(false);
            publicacionRepositorio.save(publicacion);
        }

    }

    private void validar(String titulo, String descripcion,String cuerpo) throws Exception {
        if (titulo.isEmpty() || titulo == null) {
            throw new Exception("El Título no puede ser nulo o estar vacío");
        }
        if (descripcion.isEmpty() || descripcion == null) {
            throw new Exception("La Descripcion no puede ser nula o estar vacía");
        }
        if (cuerpo.isEmpty() || cuerpo == null) {
            throw new Exception("El cuerpo no puede ser nulo o estar vacío");
        }
    }

}
