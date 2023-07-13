package com.egg.climateAware.servicios;

import com.egg.climateAware.entidades.Campana;
import com.egg.climateAware.entidades.Imagen;
import com.egg.climateAware.entidades.Publicacion;
import com.egg.climateAware.entidades.Usuario;
import com.egg.climateAware.entidades.Votante;
import com.egg.climateAware.repositorios.CampanaRepositorio;
import com.egg.climateAware.repositorios.PublicacionRepositorio;
import com.egg.climateAware.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PublicacionServicio {

    @Autowired
    private PublicacionRepositorio publicacionRepositorio;

    @Autowired
    private VotanteServicio votanteServicio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private CampanaRepositorio campanaRepositorio;

    @Transactional
    public void crearPublicacion(MultipartFile archivo, String titulo, String descripcion, String cuerpo, String youtubeUrl, String idVotante, String idCampana) throws Exception {
        validar(archivo, titulo, descripcion, cuerpo);
        validarNoRepiteUsuarioPublicacionCampana(idCampana, idVotante);
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idVotante);

        Optional<Campana> respuestaCampana = campanaRepositorio.findById(idCampana);

        Publicacion publicacion = new Publicacion();
        List<Usuario> votos = new ArrayList();
        publicacion.setTitulo(titulo);
        publicacion.setDescripcion(descripcion);
        publicacion.setCuerpo(cuerpo);
        publicacion.setFechaAlta(new Date());
        Imagen imagen = imagenServicio.guardar(archivo);
        publicacion.setImagen(imagen);
        publicacion.setAltaBaja(true);
        publicacion.setVotos(votos);
        if (youtubeUrl != null) {
            publicacion.setVideo(getEmbeddedYouTubeUrl(youtubeUrl));
        }

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            if (usuario.getRoles().toString().equalsIgnoreCase("VOT")) {
                Votante vo = (Votante) respuesta.get();
                publicacion.setVotante(vo);
            }
        }
        if (respuestaCampana.isPresent()) {
            publicacion.setCampana(respuestaCampana.get());
        }

        publicacionRepositorio.save(publicacion);
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
    public void modificarPublicacion(MultipartFile archivo, String idPublicacion, String titulo, String descripcion, String cuerpo, String youtubeUrl) throws Exception {

        validar(archivo, titulo, descripcion, cuerpo);

        Optional<Publicacion> respuesta = publicacionRepositorio.findById(idPublicacion);

        if (respuesta.isPresent()) {
            Publicacion publicacion = respuesta.get();
            publicacion.setTitulo(titulo);
            publicacion.setDescripcion(descripcion);
            publicacion.setCuerpo(cuerpo);
            if (youtubeUrl != null) {
                publicacion.setVideo(getEmbeddedYouTubeUrl(youtubeUrl));
            }

            String idImagen = null;
            if (archivo.getSize() > 0) {
                idImagen = publicacion.getImagen().getId();
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                publicacion.setImagen(imagen);
            }
            publicacionRepositorio.save(publicacion);
        }

    }

    @Transactional
    public void votar(String idPublicacion, String idUsuario) {
        Optional<Publicacion> respuesta = publicacionRepositorio.findById(idPublicacion);
        Votante votante = votanteServicio.getOne(idUsuario);
        if (respuesta.isPresent()) {
            Publicacion publicacion = respuesta.get();
            List votos = publicacion.getVotos();

            if (publicacion.getVotos().contains(votante)) {
                votos.remove(votante);
                publicacion.setVotos(votos);
            } else {
                votos.add(votante);
                publicacion.setVotos(votos);
            }
            publicacionRepositorio.save(publicacion);
        }
    }

    public Publicacion getOne(String idPublicacion) {
        return publicacionRepositorio.getOne(idPublicacion);
    }

    @Transactional(readOnly = true)
    public List<Publicacion> publicacionesPorCampana(String idCampana) {
        List<Publicacion> publicaciones = new ArrayList();

        publicaciones = publicacionRepositorio.buscarPorCampana(idCampana);

        return publicaciones;
    }

    @Transactional(readOnly = true)
    public List<Publicacion> publicacionesPorCampanaActivas(String idCampana) {
        List<Publicacion> publicaciones = new ArrayList();

        publicaciones = publicacionRepositorio.publicacionesActivasPorCampana(idCampana);

        return publicaciones;
    }

    @Transactional(readOnly = true)
    public List<Publicacion> publicacionesPorVotante(String idCampana) {
        List<Publicacion> publicaciones = new ArrayList();
        publicaciones = publicacionRepositorio.publicacionesPorVotante(idCampana);
        return publicaciones;
    }

    @Transactional(readOnly = true)
    public List<Publicacion> listarPublicacionesActivas() {
        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones = publicacionRepositorio.listadoPublicacionesActivas();
        return publicaciones;
    }

    @Transactional(readOnly = true)
    public List<Publicacion> listarPublicaciones() {
        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones = publicacionRepositorio.findAll();
        return publicaciones;
    }

    @Transactional(readOnly = true)
    public List<Publicacion> publicacionPorTitulo(String titulo) {
        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones = publicacionRepositorio.buscarPorTitulo(titulo);
        return publicaciones;
    }

    @Transactional(readOnly = true)
    public List<Publicacion> publicacionPorFecha() {
        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones = publicacionRepositorio.findAllOrderByfecha_altaDesc();
        return publicaciones;
    }
    
    public List<Publicacion>  search(String termino, String estado, String orden) {

        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones = publicacionRepositorio.search(termino, estado, orden);
        return publicaciones;
    }

    public List<Publicacion> search2(String estado, String orden) {

        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones = publicacionRepositorio.search2(estado, orden);
       return publicaciones;
    }

    @Transactional
    public void darDeBajaPublicacion(String idPublicacion) throws Exception {

        Optional<Publicacion> respuesta = publicacionRepositorio.findById(idPublicacion);

        if (respuesta.isPresent()) {
            Publicacion publicacion = respuesta.get();
            publicacion.setAltaBaja(false);
            publicacionRepositorio.save(publicacion);
        }

    }

    @Transactional
    public void darDeAltaPublicacion(String idPublicacion) throws Exception {

        Optional<Publicacion> respuesta = publicacionRepositorio.findById(idPublicacion);

        if (respuesta.isPresent()) {
            Publicacion publicacion = respuesta.get();
            publicacion.setAltaBaja(true);
            publicacionRepositorio.save(publicacion);
        }

    }

    private void validarNoRepiteUsuarioPublicacionCampana(String idCampana, String idVotante) throws Exception {
        Publicacion publicado = publicacionRepositorio.buscarPorCampanaporUsuario(idCampana, idVotante);
        if (publicado != null) {
            throw new Exception("UPS! Ya has genado una Publicación para esta campaña");
        }
    }

    private void validar(MultipartFile archivo, String titulo, String descripcion, String cuerpo) throws Exception {
        if (titulo.isEmpty() || titulo == null) {
            throw new Exception("El título no puede estar vacío.");
        }
        if (descripcion.isEmpty() || descripcion == null) {
            throw new Exception("La descripcion no puede estar vacía");
        }
        if (cuerpo.isEmpty() || cuerpo == null) {
            throw new Exception("El cuerpo no puede estar vacío");
        }
        if (archivo.getSize() > 10 * 1024 * 1024) { // 10 MB en bytes
            throw new Exception("El archivo es demasiado grande. Por favor, seleccione una imagen de menos de 10 MB");
        }
    }
}
