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
        validarRegistro(nombreApellido, dni, direccion, email, password, password2);

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
        Imagen imagen = imagenServicio.guardar(archivo);
        votante.setImagen(imagen);

        votanteRepositorio.save(votante);

    }

    @Transactional
    public void modificarVotante(MultipartFile archivo, String idVotante, String nombreApellido, String dni,
            Integer voto, String direccion, String email, String password, String password2, String idPublicacion)
            throws Exception {
        // validar(String nombreApellido, String dni,String direccion,String
        // email,String password, String password2 )

        validarModificar(nombreApellido, dni, direccion, email, password, password2);

        Optional<Votante> respuesta = votanteRepositorio.findById(idVotante);

        Optional<Publicacion> respuestaPublicacion = publicacionRepositorio.findById(idPublicacion);
        Publicacion publicacion = new Publicacion();
        if (respuestaPublicacion.isPresent()) {
            publicacion = respuestaPublicacion.get();
        }
        if (respuesta.isPresent()) {
            Votante votante = respuesta.get();

            /// propios
            votante.setNombreApellido(nombreApellido);
            votante.setDni(dni);
            votante.setDireccion(direccion);
            // votante.setPublicacion(publicacion);

            // heredados de Usuaio
            votante.setEmail(email);
            // votante.setAltaBaja(Boolean.FALSE);
            votante.setPassword(new BCryptPasswordEncoder().encode(password));
            votante.setRoles(Rol.VOT);
            Imagen imagen = imagenServicio.guardar(archivo);
            votante.setImagen(imagen);

            votanteRepositorio.save(votante);
        }

    }

    @Transactional
    public void bajaVotante(String idVotante) throws Exception {

        Optional<Votante> respuesta = votanteRepositorio.findById(idVotante);

        if (respuesta.isPresent()) {
            Votante votante = respuesta.get();
            votante.setAltaBaja(false);
            votanteRepositorio.save(votante);
        }

    }

    public Votante getOne(String idVotante) {
        return votanteRepositorio.getOne(idVotante);
    }

    public List<Votante> listarVotantes() {
        List<Votante> votantes = new ArrayList();
        votantes = votanteRepositorio.findAll();

        for (int j = 0; j < votantes.size(); j++) {
            if (!votantes.get(j).getAltaBaja()) {
                votantes.remove(j);
            }
        }
        return votantes;
    }

    private void validarRegistro(String nombreApellido, String dni, String direccion, String email, String password,
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
    }
    
    private void validarModificar(String nombreApellido, String dni, String direccion, String email, String password,
            String password2) throws Exception {

       
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

    }

}
