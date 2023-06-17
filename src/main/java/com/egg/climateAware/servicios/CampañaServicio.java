package com.egg.climateAware.servicios;

import com.egg.climateAware.entidades.Campaña;
import com.egg.climateAware.entidades.Empresa;
import com.egg.climateAware.repositorios.CampañaRepositorio;
import com.egg.climateAware.repositorios.EmpresaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CampañaServicio {

    @Autowired
    private CampañaRepositorio campañaRepositorio;

    @Autowired
    private EmpresaRepositorio empresaRepositorio;

    //// FALTA IMPLEMENTAR LA ENTIDAD PUBLICACION
    @Autowired
    private PublicacionRepositorio publicacionRepositorio;

    //// falta implementar la entidad imagen
    @Autowired
    private ImagenServicio imagenServicio;

    //// idPublicacion hay que revisarlo cuidadosamente para ver como se creo en la entidad Publicacion
    /// recordar que id es el id de empresa heredado de usuario
    /// FALTA AGREGAR TAMBIEN EL throws MiExcepcion
    @Transactional
    public void crearCampaña(String titulo, String cuerpo, Date fechaAlta, String idPublicacion, String id) {

        validar(titulo, cuerpo, fechaAlta, idPublicacion, id);

        //// implementar entidad Publicacion
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

        Campaña campaña = new Campaña();

        campaña.setTitulo(titulo);
        campaña.setCuerpo(cuerpo);
        campaña.setEmpresa(empresa);
        campaña.setFechaAlta(new Date());
        campaña.setPublicaciones(publicacion);
        campaña.setEmpresa(empresa);
        campaña.setAltaBaja(Boolean.FALSE);
        ////// falta agregar la parete de la imagen 
        Imagen imagen = imagenServicio.guardar(archivo);
        campaña.setImagen(imagen);

        campañaRepositorio.save(campaña);

    }

    @Transactional
    public void actualizarCampaña(MultipartFile archivo, String idCampaña, String titulo,
            String cuerpo, Date fechaAlta, String idPublicacion, String id) {

        Optional<Campaña> respuesta = campañaRepositorio.findById(idCampaña);

        if (idCampaña == null || idCampaña.isEmpty()) {
            throw new MiExcepcion("Debe ingrear el id de la Campaña");

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

            Campaña campaña = respuesta.get();

            campaña.setTitulo(titulo);
            campaña.setCuerpo(cuerpo);
            campaña.setEmpresa(empresa);
            campaña.setPublicaciones(publicacion);
            campaña.setAltaBaja(Boolean.TRUE);

            String idImagen = null;
            /// falta implementar la entidad imagen 
            if (empresa.getImagen() != null) {
                idImagen = empresa.getImagen().getId();
            }
            //// falta implentar la entidad imagen
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

            campaña.setImagen(imagen);

            campañaRepositorio.save(campaña);

        }

    }
    
     @Transactional(readOnly = true)
    public Campaña getOne(String id) {
        return campañaRepositorio.getOne(id);
                
    }
    
    @Transactional(readOnly = true)
    public List<Campaña> listarCampañas(){
        List<Campaña> campañas = new ArrayList();
        
        campañas = campañaRepositorio.findAll();
        
        return campañas;
    }
    
    @Transactional
    public void darDeBajaCampaña(String idCampaña){
        Optional<Campaña> respuesta = campañaRepositorio.findById(idCampaña);
        
        if(idCampaña == null || idCampaña.isEmpty()){
            throw new MiExcepcion("Debe ingrear el id de la Campaña");
        }
        
        if(respuesta.isPresent()){
            Campaña campaña = respuesta.get();
            campaña.setAltaBaja(Boolean.FALSE);
            
            campañaRepositorio.save(campaña);
            
            
        }
    }
    
    
    
    

 ///// FALTA AGREGAR EL throws MiExcepcion
//// REVIZAR COMO ES EL id DE PUBLICACIN
    public void validar(String titulo, String cuerpo, Date fechaAlta, String idPublicacion, String id) {

        if (titulo.isEmpty() || titulo == null) {
            throw new MiExcepcion("Debe ingrear un Titulo de la Campaña");
        }

        if (cuerpo.isEmpty() || cuerpo == null) {
            throw new MiExcepcion("Debe ingrear un Cuerpo de la Campaña");
        }

        if (idpublicacion.isEmpty() || idPublicacion == null) {
            throw new MiException("La publicacion no puede ser nulo o estar vacio");
        }

        if (fechaAlta == null) {
            throw new MIExcepcion("fecha puede ser nulo o estar vacio");

        }

        if (id.isEmpty() || id == null) {
            throw new MiException("La Empresa no puede ser nula o estar vacia");
        }

    }

}
