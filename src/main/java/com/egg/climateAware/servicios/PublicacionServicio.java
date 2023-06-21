
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
    public void crearPublicacion(MultipartFile archivo, String cuerpo, String video) throws Exception {
        validar(cuerpo);
        Publicacion publicacion = new Publicacion();
        publicacion.setCuerpo(cuerpo);
        publicacion.setFechaAlta(new Date());
        Imagen imagen = imagenServicio.guardar(archivo);
        publicacion.setImagen(imagen);
        publicacion.setAltaBaja(true);
        publicacion.setVideo(video);
        publicacionRepositorio.save(publicacion);
    }

    @Transactional
    public void modificarPublicacion(MultipartFile archivo, String idPublicacion, String cuerpo, String video) throws Exception {

        validar(cuerpo);

        Optional<Publicacion> respuesta = publicacionRepositorio.findById(idPublicacion);

        if (respuesta.isPresent()) {
            Publicacion publicacion = respuesta.get();
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
        publicaciones = publicacionRepositorio.findAll();
        //intento solo traer las que no estan dadas de baja
        for (int j=0; j < publicaciones.size(); j++){
            if(!publicaciones.get(j).getAltaBaja()){
                publicaciones.remove(j);
            }
        }
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

    private void validar(String cuerpo) throws Exception {
        if (cuerpo.isEmpty() || cuerpo == null) {
            throw new Exception("El cuerpo no puede ser nulo o estar vacío");
        }
    }

}
