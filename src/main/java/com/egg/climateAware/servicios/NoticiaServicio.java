
package com.egg.climateAware.servicios;

import com.egg.climateAware.entidades.Imagen;
import com.egg.climateAware.entidades.Noticia;
import com.egg.climateAware.repositorios.NoticiaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service 
public class NoticiaServicio {
    @Autowired
    private NoticiaRepositorio noticiaRepositorio;
    
    @Autowired
    private ImagenServicio imagenServicio;
    
    @Transactional
    public void crearNoticia(MultipartFile archivo, String titulo, String descripcion, String cuerpo, String video) throws Exception{
        validar(titulo,descripcion,cuerpo);
        Noticia noticia = new Noticia();
        noticia.setTitulo(titulo);
        noticia.setDescripcion(descripcion);
        noticia.setCuerpo(cuerpo);
        noticia.setFechaAlta(new Date());
        Imagen imagen = imagenServicio.guardar(archivo);
        noticia.setImagen(imagen);
        noticia.setAltaBaja(true);
        noticia.setVideo(video);
        noticiaRepositorio.save(noticia);
        
    }
   @Transactional
    public void modificarNoticia(MultipartFile archivo, String idNoticia, String titulo, String descripcion, String cuerpo, String video) throws Exception{
        validar(titulo,descripcion,cuerpo);
        Optional<Noticia> respuesta = noticiaRepositorio.findById(idNoticia);
        
        if (respuesta.isPresent()){
            Noticia noticia = respuesta.get();
            noticia.setTitulo(titulo);
            noticia.setDescripcion(descripcion);
            noticia.setCuerpo(cuerpo);
            noticia.setVideo(video);
            String idImagen = null;
            if (noticia.getImagen() != null) {
                idImagen = noticia.getImagen().getId();
            }
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);               
            noticia.setImagen(imagen);
            noticiaRepositorio.save(noticia);
        }
    }
   @Transactional
    public void eliminarNoticia(String idNoticia ) throws Exception { 
            noticiaRepositorio.deleteById(idNoticia);       
    }
    
    public Noticia getOne(String idNoticia){
        return noticiaRepositorio.getOne(idNoticia);
    }
    
    //        @Transactional(readOnly = true)
    public List<Noticia> listarNoticias(){
        List<Noticia> noticias = new ArrayList();
        noticias = noticiaRepositorio.listadoNoticiasActivas();
        return noticias;
    }
    
    @Transactional
    public void bajaNoticia(String idNoticia) throws Exception{
         Optional<Noticia> respuesta = noticiaRepositorio.findById(idNoticia);
        
        if (respuesta.isPresent()){
            Noticia noticia = respuesta.get();
            noticia.setAltaBaja(false);
            noticiaRepositorio.save(noticia);
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
