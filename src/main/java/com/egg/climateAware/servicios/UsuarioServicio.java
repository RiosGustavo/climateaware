package com.egg.climateAware.servicios;

import com.egg.climateAware.entidades.Imagen;
import com.egg.climateAware.entidades.Usuario;
import com.egg.climateAware.enumeraciones.Rol;
import com.egg.climateAware.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void crearUsuario(MultipartFile archivo, String email, String password, String password2) throws Exception {

        validar(email, password, password2);

        Usuario usuario = new Usuario();

        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
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
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            String idImagen = null;

            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

            usuario.setImagen(imagen);
            usuarioRepositorio.save(usuario);
        }

    }

    @Transactional
    public void cambiarClave(String claveActual, String id, String clave, String clave2) throws Exception {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
           
            if (clave.isEmpty() || clave == null) {
                throw new Exception("La contraseña no puede ser vacía.");
            }
             if (clave.length() < 8) {
                throw new Exception("La contraseña nueva debe tener al menos 8 caracteres");
            }
            if (!clave.equals(clave2)) {
                throw new Exception("Las contraseñas no coinciden. Por favor introduzcalas correctamente.");
            }

            Usuario usuario = respuesta.get();

            String encodedPassword = usuario.getPassword();

            if (bCryptPasswordEncoder.matches(claveActual, encodedPassword)) {
                usuario.setPassword(new BCryptPasswordEncoder().encode(clave));

                usuarioRepositorio.save(usuario);
            } else {
                throw new Exception("La contraseña actual no es válida.");
            }

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

        // Verificar si el email ya existe en la base de datos
        Usuario usuarioExistente = usuarioRepositorio.buscarPorEmail(email);
        if (usuarioExistente != null) {
            throw new Exception("El email ingresado ya está registrado");
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRoles().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);

        } else {
            return null;
        }

    }

}
