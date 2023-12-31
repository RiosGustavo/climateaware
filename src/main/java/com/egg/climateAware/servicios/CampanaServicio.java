package com.egg.climateAware.servicios;

import com.egg.climateAware.entidades.Campana;
import com.egg.climateAware.entidades.Empresa;
import com.egg.climateAware.entidades.Imagen;
import com.egg.climateAware.entidades.Publicacion;
import com.egg.climateAware.entidades.Usuario;
import com.egg.climateAware.entidades.Votante;
import com.egg.climateAware.repositorios.CampanaRepositorio;
import com.egg.climateAware.repositorios.EmpresaRepositorio;
import com.egg.climateAware.repositorios.PublicacionRepositorio;
import com.egg.climateAware.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CampanaServicio {

    @Autowired
    private CampanaRepositorio campanaRepositorio;

    @Autowired
    private EmpresaRepositorio empresaRepositorio;

    @Autowired
    private PublicacionRepositorio publicacionRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    private VotanteServicio votanteServicio;

    @Transactional
    public void crearCampana(MultipartFile archivo, String titulo, String cuerpo, String descripcion, String id) throws Exception {

        validar(archivo, titulo, cuerpo, descripcion);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        List<Votante> votantes = new ArrayList();
        Campana campana = new Campana();
        campana.setTitulo(titulo);
        campana.setCuerpo(cuerpo);
        campana.setDescripcion(descripcion);
        campana.setFechaAlta(new Date());
        campana.setAltaBaja(true);
        Imagen imagen = imagenServicio.guardar(archivo);
        campana.setImagen(imagen);
        campana.setVotantes(votantes);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            if (usuario.getRoles().toString().equalsIgnoreCase("EMP")) {
                Empresa e = (Empresa) respuesta.get();
                campana.setEmpresa(e);
            }
        }

        campanaRepositorio.save(campana);

    }

    @Transactional
    public void actualizarCampana(MultipartFile archivo, String titulo, String cuerpo, String descripcion, String idCampana) throws Exception {

        validar(archivo, titulo, cuerpo, descripcion);

        Optional<Campana> respuesta = campanaRepositorio.findById(idCampana);

        if (respuesta.isPresent()) {

            Campana campana = respuesta.get();

            campana.setTitulo(titulo);
            campana.setDescripcion(descripcion);
            campana.setCuerpo(cuerpo);

            String idImagen = null;
            if (archivo.getSize() > 0) {
                idImagen = campana.getImagen().getId();
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                campana.setImagen(imagen);
            }
            campanaRepositorio.save(campana);

        }

    }

    @Transactional
    public void seguir(String idCampana, String idUsuario) {
        Optional<Campana> respuesta = campanaRepositorio.findById(idCampana);
        Votante votante = votanteServicio.getOne(idUsuario);
        if (respuesta.isPresent()) {
            Campana campana = respuesta.get();
            List votantes = campana.getVotantes();

            if (campana.getVotantes().contains(votante)) {
                votantes.remove(votante);
                campana.setVotantes(votantes);
            } else {
                votantes.add(votante);
                campana.setVotantes(votantes);
            }
            campanaRepositorio.save(campana);
        }
    }

    @Transactional(readOnly = true)
    public Campana getOne(String id) {
        return campanaRepositorio.getOne(id);

    }

    @Transactional(readOnly = true)
    public List<Campana> listarCampanas() {
        List<Campana> campanas = new ArrayList();

        campanas = (List<Campana>) campanaRepositorio.findAll();

        return campanas;
    }

    @Transactional(readOnly = true)
    public List<Campana> listarCampanasActivas() {
        List<Campana> campanas = new ArrayList();

        campanas = campanaRepositorio.buscarPorEstado();

        return campanas;
    }

    public List<Campana> search(String termino, String estado, String orden) {

        List<Campana> campanas = new ArrayList();
        campanas = campanaRepositorio.search(termino, estado, orden);
        return campanas;
    }

    public List<Campana> search2(String estado, String orden) {

        List<Campana> campanas = new ArrayList();
        campanas = campanaRepositorio.search2(estado, orden);
        return campanas;
    }

    public List<Campana> buscarCampanasPorTitulo(String titulo) {

        List<Campana> campanas = new ArrayList();
        campanas = campanaRepositorio.buscarPorTitulo(titulo);
        return campanas;
    }

    @Transactional
    public void darDeBajaCampana(String idCampana) throws Exception {
        Optional<Campana> respuesta = campanaRepositorio.findById(idCampana);

        if (respuesta.isPresent()) {
            Campana campana = respuesta.get();
            campana.setAltaBaja(false);

            campanaRepositorio.save(campana);

        }
    }

    @Transactional
    public void darDeAltaCampana(String idCampana) throws Exception {
        Optional<Campana> respuesta = campanaRepositorio.findById(idCampana);

        if (respuesta.isPresent()) {
            Campana campana = respuesta.get();
            campana.setAltaBaja(true);

            campanaRepositorio.save(campana);

        }
    }

    public List<Campana> campanasPorEmpresa(String id) {

        List<Campana> campanas = new ArrayList();

        campanas = campanaRepositorio.campanasPorEmpresa(id);
        return campanas;
    }

    public void validar(MultipartFile archivo, String titulo, String cuerpo, String descripcion) throws Exception {

        if (titulo.isEmpty() || titulo == null) {
            throw new Exception("El título de la campaña no puede estar vacío.");
        }

        if (cuerpo.isEmpty() || cuerpo == null) {
            throw new Exception("El cuerpo de la campaña no puede estar vacío.");
        }

        if (descripcion.isEmpty() || descripcion == null) {
            throw new Exception("La descripción de la campaña no puede estar vacío.");
        }

        if (archivo.getSize() > 10 * 1024 * 1024) { // 10 MB en bytes
            throw new Exception("El archivo es demasiado grande. Por favor, seleccione una imagen de menos de 10 MB");
        }

    }

    public void darDeBajaCampanasAntiguas() {
        List<Campana> campanasActivas = campanaRepositorio.findByAltaBajaTrue();
        Date fechaActual = new Date();

        for (Campana campana : campanasActivas) {
            Date fechaAlta = campana.getFechaAlta();
            long diferenciaTiempo = fechaActual.getTime() - fechaAlta.getTime();
            long diasPasados = diferenciaTiempo / (1000 * 60 * 60 * 24);

            if (diasPasados >= 30) {
                campana.setAltaBaja(false);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaAlta);
                calendar.add(Calendar.DAY_OF_MONTH, 30);

                Date fechaBaja = calendar.getTime();

                campana.setFechaBaja(fechaBaja);
                campanaRepositorio.save(campana);
            }
        }
    }
}
