package com.egg.climateAware.servicios;

import com.egg.climateAware.entidades.Imagen;
import com.egg.climateAware.entidades.Publicacion;
import com.egg.climateAware.entidades.Votante;
import com.egg.climateAware.enumeraciones.Rol;
import com.egg.climateAware.repositorios.PublicacionRepositorio;
import com.egg.climateAware.repositorios.VotanteRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VotanteServicio {

    @Autowired
    private VotanteRepositorio votanteRepositorio;

    @Autowired
    private PublicacionRepositorio publicacionRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void crearVotante(MultipartFile archivo, String nombreApellido, String dni, String direccion, String email,
            String password, String password2) throws Exception {
        validarRegistro(archivo, nombreApellido, dni, direccion, email, password, password2);

        Votante votante = new Votante();
        /// propios
        votante.setNombreApellido(nombreApellido);
        votante.setDni(dni);
        votante.setDireccion(direccion);

        // heredados de Usuaio
        votante.setEmail(email);
        votante.setFechaAlta(new Date());
        votante.setAltaBaja(true);
        votante.setPassword(new BCryptPasswordEncoder().encode(password));

        votante.setRoles(Rol.VOT);

        if (archivo.getSize() == 0) {
            Imagen imagen = imagenServicio.obtenerImagenPorDefecto();
            votante.setImagen(imagen);
        } else {
            Imagen imagen = imagenServicio.guardar(archivo);
            votante.setImagen(imagen);
        }

        votanteRepositorio.save(votante);

    }

   
    
    
    
    
    @Transactional
    public void modificarVotante(MultipartFile archivo, String idVotante, String nombreApellido, String direccion)
            throws Exception {

        validarModificar(archivo, nombreApellido, direccion);

        Optional<Votante> respuesta = votanteRepositorio.findById(idVotante);

        if (respuesta.isPresent()) {
            Votante votante = respuesta.get();

            votante.setNombreApellido(nombreApellido);
            votante.setDireccion(direccion);

            String idImagen = null;
            if (archivo.getSize() > 0) {
                idImagen = votante.getImagen().getId();
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                votante.setImagen(imagen);
            }
            votanteRepositorio.save(votante);
        }

    }

    @Transactional
    public void darDeBajaVotante(String id) throws Exception {

        Optional<Votante> respuesta = votanteRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Votante votante = respuesta.get();
            votante.setAltaBaja(false);

            votanteRepositorio.save(votante);
        }

    }

    @Transactional
    public void darDeAltaVotante(String id) throws Exception {

        Optional<Votante> respuesta = votanteRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Votante votante = respuesta.get();
            votante.setAltaBaja(true);

            votanteRepositorio.save(votante);
        }

    }

    public Votante getOne(String idVotante) {
        return votanteRepositorio.getOne(idVotante);
    }

    public List<Votante> listarVotantes() {
        List<Votante> votantes = new ArrayList();
        votantes = votanteRepositorio.findAll();
        return votantes;
    }

    public List<Votante> search(String termino, String estado,String orden) {

        List<Votante> votantes = new ArrayList();
        votantes = votanteRepositorio. search(termino, estado,orden);
        return votantes;
    }
    
    public List<Votante> search2(String estado,String orden) {

        List<Votante> votantes = new ArrayList();
        votantes = votanteRepositorio. search2(estado,orden);
        return votantes;
    }

    private void validarRegistro(MultipartFile archivo, String nombreApellido, String dni, String direccion, String email, String password,
            String password2) throws Exception {

        // Verificar si el email ya existe en la base de datos
        Votante usuarioExistente = votanteRepositorio.buscarPorEmail(email);
        if (usuarioExistente != null) {
            throw new Exception("El email ingresado ya está registrado");
        }

        if (nombreApellido.isEmpty() || nombreApellido == null) {
            throw new Exception("Debe ingresar el nombre y apellido");
        }

        if (dni.isEmpty() || dni == null || !dni.chars().allMatch(Character::isDigit)) {
            throw new Exception("Debe ingresar un DNI");
        }
        if (direccion.isEmpty() || direccion == null) {
            throw new Exception("Debe ingresar una dirección");
        }

        if (email.isEmpty() || email == null) {
            throw new Exception("Debe ingresar un email");
        }

        if (password == null || password.isEmpty()) {
            throw new Exception("La contraseña no puede estar vacía");
        }
        if (!password.equals(password2)) {

            throw new Exception("Las contraseñas no coinciden. Por favor introduzcalas correctamente");

        }
        if (password.length() < 8) {
            throw new Exception("La contraseña debe tener al menos 8 caracteres");
        }

        if (archivo.getSize() > 10 * 1024 * 1024) { // 10 MB en bytes
            throw new Exception("El archivo es demasiado grande. Por favor, seleccione una imagen de menos de 10 MB");
        }
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
