package com.egg.climateAware.servicios;

import com.egg.climateAware.entidades.Blogger;
import com.egg.climateAware.entidades.Imagen;
import com.egg.climateAware.entidades.Votante;
import com.egg.climateAware.enumeraciones.Rol;
import com.egg.climateAware.repositorios.BloggerRepositorio;
import com.egg.climateAware.repositorios.VotanteRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BloggerServicio {

    @Autowired
    private BloggerRepositorio bloggerRepositorio;

    @Autowired
    private VotanteRepositorio votanteRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public Blogger crearBlogger(String id) {
        Votante votante = votanteRepositorio.findById(id).get();
        Blogger blo = new Blogger();

        blo.setNombreApellido(votante.getNombreApellido());
        blo.setDni(votante.getDni());
        blo.setDireccion(votante.getDireccion());
        blo.setEmail(votante.getEmail());
        blo.setFechaAlta(new Date());
        blo.setAltaBaja(true);
        blo.setPassword(votante.getPassword());
        blo.setRoles(Rol.BLO);
        blo.setImagen(votante.getImagen());
        

        bloggerRepositorio.save(blo);
        votanteRepositorio.deleteById(id);
        return blo;
    }
    
    @Transactional
    public Votante bloggerAvotante(String id){
        
        Blogger blogger = bloggerRepositorio.findById(id).get();
        Votante vo = new Votante();
        
        vo.setNombreApellido( blogger.getNombreApellido());
        vo.setDni( blogger.getDni());
        vo.setDireccion( blogger.getDireccion());
        vo.setEmail(blogger.getEmail());
        vo.setFechaAlta(new Date());
        vo.setAltaBaja(true);
        vo.setPassword(blogger.getPassword());
        vo.setRoles(Rol.VOT);
        vo.setImagen(blogger.getImagen());
        
        
        
        votanteRepositorio.save(vo);
        bloggerRepositorio.deleteById(id);
        return vo;
       
    }
    
    
    

    @Transactional
    public void modificarBlogger(MultipartFile archivo, String id, String nombreApellido, String direccion)
            throws Exception {

        validarModificar(archivo, nombreApellido, direccion);
        Optional<Blogger> respuesta = bloggerRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Blogger blogger = respuesta.get();

            blogger.setNombreApellido(nombreApellido);
            blogger.setDireccion(direccion);

            String idImagen = null;
            if (archivo.getSize() > 0) {
                idImagen = blogger.getImagen().getId();
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                blogger.setImagen(imagen);
            }
            bloggerRepositorio.save(blogger);
        }
    }

    @Transactional
    public void darDeBajaBlogger(String id) throws Exception {

        Optional<Blogger> respuesta = bloggerRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Blogger blogger = respuesta.get();
            blogger.setAltaBaja(false);

            bloggerRepositorio.save(blogger);
        }
    }

    @Transactional
    public void darDeAltaBlogger(String id) throws Exception {

        Optional<Blogger> respuesta = bloggerRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Blogger blogger = respuesta.get();
            blogger.setAltaBaja(true);

            bloggerRepositorio.save(blogger);
        }
    }

    public Blogger getOne(String id) {
        return bloggerRepositorio.getOne(id);
    }

    public List<Blogger> buscarBloggersPorTermino(String termino) {

        List<Blogger> bloggers = new ArrayList();
        bloggers = bloggerRepositorio.buscarBloggersPorTermino(termino);
        return bloggers;
    }

    public List<Blogger> listarBloggers() {
        List<Blogger> bloggers = new ArrayList();
        bloggers = bloggerRepositorio.findAll();
        return bloggers;
    }

    private void validarModificar(MultipartFile archivo, String nombreApellido, String direccion) throws Exception {

        if (nombreApellido.isEmpty() || nombreApellido == null) {
            throw new Exception("El nombre y apellido no puede estar vacío.");
        }

        if (direccion.isEmpty() || direccion == null) {
            throw new Exception("La dirección no puede estar vacía.");
        }

        if (archivo.getSize() > 10 * 1024 * 1024) { // 10 MB en bytes
            throw new Exception("El archivo es demasiado grande. Por favor, seleccione una imagen de menos de 10 MB");
        }
    }
}
