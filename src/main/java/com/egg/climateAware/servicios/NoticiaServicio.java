package com.egg.climateAware.servicios;

import com.egg.climateAware.entidades.Blogger;
import com.egg.climateAware.entidades.Imagen;
import com.egg.climateAware.entidades.Noticia;
import com.egg.climateAware.entidades.Usuario;
import com.egg.climateAware.entidades.Votante;
import com.egg.climateAware.repositorios.NoticiaRepositorio;
import com.egg.climateAware.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void crearNoticia(MultipartFile archivo, String titulo, String descripcion, String cuerpo, String youtubeUrl, String idBlogger) throws Exception {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(idBlogger);

        validar(titulo, descripcion, cuerpo);
        Noticia noticia = new Noticia();
        noticia.setTitulo(titulo);
        noticia.setDescripcion(descripcion);
        noticia.setCuerpo(cuerpo);
        noticia.setFechaAlta(new Date());
        Imagen imagen = imagenServicio.guardar(archivo);
        noticia.setImagen(imagen);
        noticia.setAltaBaja(true);
        if (youtubeUrl != null) {
            noticia.setVideo(getEmbeddedYouTubeUrl(youtubeUrl));
        }

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            if (usuario.getRoles().toString().equalsIgnoreCase("BLO")) {
                Blogger blo = (Blogger) respuesta.get();
                noticia.setBlogger(blo);
            }
        }
        noticiaRepositorio.save(noticia);

    }

    public String getEmbeddedYouTubeUrl(String youtubeUrl) {
        // Patron para buscar el identificador del video en la URL

        Pattern pattern = Pattern.compile("(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*");

        // Buscar el identificador del video en la URL
        Matcher matcher = pattern.matcher(youtubeUrl);
        if (matcher.find()) {
            String videoId = matcher.group();

            // Construir la URL de YouTube con el identificador del video en formato embed
            return "https://www.youtube.com/embed/" + videoId;
        } else {
            // Si no se encuentra el identificador del video en la URL, devolvemos null
            return null;
        }
    }

    @Transactional
    public void modificarNoticia(MultipartFile archivo, String idNoticia, String titulo, String descripcion, String cuerpo, String youtubeUrl) throws Exception {
        validar(titulo, descripcion, cuerpo);
        Optional<Noticia> respuesta = noticiaRepositorio.findById(idNoticia);

        if (respuesta.isPresent()) {
            Noticia noticia = respuesta.get();
            noticia.setTitulo(titulo);
            noticia.setDescripcion(descripcion);
            noticia.setCuerpo(cuerpo);
            if (youtubeUrl != null) {
                noticia.setVideo(getEmbeddedYouTubeUrl(youtubeUrl));
            }
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
    public void eliminarNoticia(String idNoticia) throws Exception {
        noticiaRepositorio.deleteById(idNoticia);
    }

    public Noticia getOne(String idNoticia) {
        return noticiaRepositorio.getOne(idNoticia);
    }

    //        @Transactional(readOnly = true)
    public List<Noticia> listarNoticias() {
        List<Noticia> noticias = new ArrayList();
        noticias = noticiaRepositorio.listadoNoticiasActivas();
        return noticias;
    }
    
    @Transactional(readOnly = true)
    public List<Noticia> noticiasPorBlogger(String idBlogger) {
        List<Noticia> noticias = new ArrayList();
        noticias = noticiaRepositorio.noticiasPorBlogger(idBlogger);
        return noticias;
    }
    
    @Transactional
    public void bajaNoticia(String idNoticia) throws Exception {
        Optional<Noticia> respuesta = noticiaRepositorio.findById(idNoticia);

        if (respuesta.isPresent()) {
            Noticia noticia = respuesta.get();
            noticia.setAltaBaja(false);
            noticiaRepositorio.save(noticia);
        }
    }

    private void validar(String titulo, String descripcion, String cuerpo) throws Exception {
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
