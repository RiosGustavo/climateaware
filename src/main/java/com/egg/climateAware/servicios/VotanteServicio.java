package com.egg.climateAware.servicios;

import com.egg.climateAware.entidades.Imagen;
import com.egg.climateAware.entidades.Publicacion;
import com.egg.climateAware.entidades.Votante;
import com.egg.climateAware.enumeraciones.Rol;
import com.egg.climateAware.repositorios.PublicacionRepositorio;
import com.egg.climateAware.repositorios.VotanteRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void crearVotante(MultipartFile archivo, String nombreApellido, String dni, Integer voto, String direccion, String email, String password, String password2, String idPublicacion) throws Exception {
        validar(nombreApellido, dni, direccion, email, password, password2);

        Optional<Publicacion> respuestaPublicacion = publicacionRepositorio.findById(idPublicacion);
        Publicacion publicacion = new Publicacion();
        if (respuestaPublicacion.isPresent()) {
            publicacion = respuestaPublicacion.get();
        }

        Votante votante = new Votante();
        ///propios
        votante.setNombreApellido(nombreApellido);
        votante.setDni(dni);
        votante.setDireccion(direccion);
        votante.setPublicacion(publicacion);

        //heredados de Usuaio
        votante.setEmail(email);
        votante.setAltaBaja(Boolean.FALSE);
        votante.setPassword(password);
        votante.setRoles(Rol.VOT);
        Imagen imagen = imagenServicio.guardar(archivo);
        votante.setImagen(imagen);

        votanteRepositorio.save(votante);

    }

    @Transactional
    public void modificarVotante(MultipartFile archivo, String idVotante, String nombreApellido, String dni, Integer voto, String direccion, String email, String password, String password2, String idPublicacion) throws Exception {
        //validar(String nombreApellido, String dni,String direccion,String email,String password, String password2 )

        validar(nombreApellido, dni, direccion, email, password, password2);

        Optional<Votante> respuesta = votanteRepositorio.findById(idVotante);

        Optional<Publicacion> respuestaPublicacion = publicacionRepositorio.findById(idPublicacion);
        Publicacion publicacion = new Publicacion();
        if (respuestaPublicacion.isPresent()) {
            publicacion = respuestaPublicacion.get();
        }
        if (respuesta.isPresent()) {
            Votante votante = respuesta.get();

            ///propios
            votante.setNombreApellido(nombreApellido);
            votante.setDni(dni);
            votante.setDireccion(direccion);
            votante.setPublicacion(publicacion);

            //heredados de Usuaio
            votante.setEmail(email);
            //votante.setAltaBaja(Boolean.FALSE);
            votante.setPassword(password);
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
            votante.setAltaBaja(Boolean.TRUE);

            votanteRepositorio.save(votante);
        }

    }

    public Votante getOne(String idVotante) {
        return votanteRepositorio.getOne(idVotante);
    }

    public List<Votante> listarVotantes() {
        List<Votante> votantes = new ArrayList();
        votantes = votanteRepositorio.findAll();
        
         for (int j=0; j < votantes.size(); j++){
            if(votantes.get(j).getAltaBaja()){
                votantes.remove(j);
            }
        }
        return votantes;
    }

    private void validar(String nombreApellido, String dni, String direccion, String email, String password, String password2) throws Exception {
        if (nombreApellido.isEmpty() || nombreApellido == null) {
            throw new Exception("Nombre y Apellido no puede ser nulo o estar vacío");
        }

        if (dni.isEmpty() || dni == null || !dni.chars().allMatch(Character::isDigit)) {
            throw new Exception("DNI no puede ser nulo o estar vacío y debe ser numérico");
        }
        if (direccion.isEmpty() || direccion == null) {
            throw new Exception("Dirección no puede ser nulo o estar vacío");
        }

        if (email.isEmpty() || email == null) {
            throw new Exception("Dirección Mail no puede ser nulo o estar vacío");
        }

        if (password.isEmpty() || password == null || password.length() <= 8) {
            throw new Exception("Debe ingrear un password y de mas de 8 caracteres");
        }

        if (!password.equals(password2)) {
            throw new Exception("Los password ingresados deben ser iguales");
        }

    }

}
