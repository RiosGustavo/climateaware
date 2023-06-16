package com.egg.climateAware.servicios;

import com.egg.climateAware.entidades.Imagen;
import com.egg.climateAware.entidades.Usuario;
import com.egg.climateAware.enumeraciones.Rol;
import com.egg.climateAware.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void crearUsuario(MultipartFile archivo, String email, String password, String password2) throws Exception {

        validar(email, password, password2);

        Usuario usuario = new Usuario();

        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setFechaAlta(new Date());
        usuario.setRoles(Rol.VOT);
        usuario.setAltaBaja(Boolean.TRUE);
        Imagen imagen = imagenServicio.guardar(archivo);

        usuario.setImagen(imagen);

        usuarioRepositorio.save(usuario);

    }

    @Transactional
    public void ModificarUsuario(MultipartFile archivo, String id, String email, String password, String password2) throws Exception {

        validar(email, password, password2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            usuario.setEmail(email);
            usuario.setPassword(password);
            String idImagen = null;
            
            if(usuario.getImagen() != null){
                idImagen = usuario.getImagen().getId();
            }
            
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            
            usuario.setImagen(imagen);
            usuarioRepositorio.save(usuario);
        }

    }

    public Usuario getOne(String id) {
        return usuarioRepositorio.getOne(id);
    }

    @Transactional
    public List<Usuario> listaUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios = usuarioRepositorio.findAll();
        return usuarios;
    }

    @Transactional
    public void cambiarEstado(String id) throws Exception {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            if (!usuario.getRoles().equals(Rol.VOT)) {
                if (usuario.getAltaBaja().equals(Boolean.TRUE)) {
                    usuario.setAltaBaja(Boolean.FALSE);
                } else if (usuario.getAltaBaja().equals(Boolean.FALSE)) {
                    usuario.setAltaBaja(Boolean.TRUE);
                }
            } else {
                throw new Exception("No se puede cambiar el estado de ADMINISTRADOR");
            }

        }
    }

    private void validar(String email, String password, String password2) throws Exception {

        if (email.isEmpty() || email == null) {
            throw new Exception("El email no puede ser nulo o estar vacío");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new Exception("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
        }
        if (!password.equals(password2)) {
            throw new Exception("Las contraseñas ingresadas deben ser iguales");
        }

    }

}
