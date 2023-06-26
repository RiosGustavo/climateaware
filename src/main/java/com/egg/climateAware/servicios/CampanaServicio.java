package com.egg.climateAware.servicios;

import com.egg.climateAware.entidades.Campana;
import com.egg.climateAware.entidades.Empresa;
import com.egg.climateAware.entidades.Imagen;
import com.egg.climateAware.entidades.Publicacion;
import com.egg.climateAware.repositorios.CampanaRepositorio;
import com.egg.climateAware.repositorios.EmpresaRepositorio;
import com.egg.climateAware.repositorios.PublicacionRepositorio;
import java.util.ArrayList;
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
    private ImagenServicio imagenServicio;

    
    @Transactional
    public void crearCampana(MultipartFile archivo, String titulo, String cuerpo, Date fechaAlta, String idPublicacion, String id) throws Exception {

        validar(titulo, cuerpo, fechaAlta, idPublicacion, id);

        
        Optional<Publicacion> respuestaPublicacion = publicacionRepositorio.findById(idPublicacion);
        Optional<Empresa> respuestaEmpresa = empresaRepositorio.findById(id);

        Publicacion publicacion = new Publicacion();
        Empresa empresa = new Empresa();

        if (respuestaPublicacion.isPresent()) {
            publicacion = respuestaPublicacion.get();
        }

        if (respuestaEmpresa.isPresent()) {
            empresa = respuestaEmpresa.get();
        }

        Campana campana = new Campana();

        campana.setTitulo(titulo);
        campana.setCuerpo(cuerpo);
        campana.setEmpresa(empresa);
        campana.setFechaAlta(new Date());
        campana.setPublicaciones(publicacion);
        campana.setEmpresa(empresa);
        campana.setAltaBaja(Boolean.TRUE);
        
        Imagen imagen = imagenServicio.guardar(archivo);
        campana.setImagen(imagen);

        campanaRepositorio.save(campana);

    }

    @Transactional
    public void actualizarCampana(MultipartFile archivo, String idCampana, String titulo,
            String cuerpo, Date fechaAlta, String idPublicacion, String id) throws Exception {

        Optional<Campana> respuesta = campanaRepositorio.findById(idCampana);

        if (idCampana == null || idCampana.isEmpty()) {
            throw new Exception("Debe ingrear el id de la Campana");

        }

        validar(titulo, cuerpo, fechaAlta, idPublicacion, id);

        Optional<Publicacion> respuestaPublicacion = publicacionRepositorio.findById(idPublicacion);
        Optional<Empresa> respuestaEmpresa = empresaRepositorio.findById(id);

        Publicacion publicacion = new Publicacion();
        Empresa empresa = new Empresa();

        if (respuestaPublicacion.isPresent()) {
            publicacion = respuestaPublicacion.get();
        }

        if (respuestaEmpresa.isPresent()) {
            empresa = respuestaEmpresa.get();
        }

        if (respuesta.isPresent()) {

            Campana campana = respuesta.get();

            campana.setTitulo(titulo);
            campana.setCuerpo(cuerpo);
            campana.setEmpresa(empresa);
            campana.setPublicaciones(publicacion);
            campana.setAltaBaja(Boolean.TRUE);

            String idImagen = null;
           
            if (empresa.getImagen() != null) {
                idImagen = empresa.getImagen().getId();
            }
           
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

            campana.setImagen(imagen);

            campanaRepositorio.save(campana);

        }

    }
    
     @Transactional(readOnly = true)
    public Campana getOne(String id) {
        return campanaRepositorio.getOne(id);
                
    }
    
    @Transactional(readOnly = true)
    public List<Campana> listarCampanas(){
        List<Campana> campanas = new ArrayList();
        
        campanas = campanaRepositorio.findAll();
        
        return campanas;
    }
    
    @Transactional
    public void darDeBajaCampana(String idCampana) throws Exception{
        Optional<Campana> respuesta = campanaRepositorio.findById(idCampana);
        
        if(idCampana == null || idCampana.isEmpty()){
            throw new Exception("Debe ingrear el id de la Campana");
        }
        
        if(respuesta.isPresent()){
            Campana campana = respuesta.get();
            campana.setAltaBaja(Boolean.FALSE);
            
            campanaRepositorio.save(campana);
            
            
        }
    }
    
    
    
    


    public void validar(String titulo, String cuerpo, Date fechaAlta, String idPublicacion, String id) throws Exception {

        if (titulo.isEmpty() || titulo == null) {
            throw new Exception("Debe ingrear un Titulo de la Campana");
        }

        if (cuerpo.isEmpty() || cuerpo == null) {
            throw new Exception("Debe ingrear un Cuerpo de la Campana");
        }

        if (idPublicacion.isEmpty() || idPublicacion == null) {
            throw new Exception("La publicacion no puede ser nulo o estar vacio");
        }

        if (fechaAlta == null) {
            throw new Exception("fecha puede ser nulo o estar vacio");

        }

        if (id.isEmpty() || id == null) {
            throw new Exception("La Empresa no puede ser nula o estar vacia");
        }

    }

}
