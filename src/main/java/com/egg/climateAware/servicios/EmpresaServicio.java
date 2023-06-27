package com.egg.climateAware.servicios;

import com.egg.climateAware.entidades.Campana;
import com.egg.climateAware.entidades.Empresa;
import com.egg.climateAware.entidades.Imagen;
import com.egg.climateAware.enumeraciones.Rol;
import com.egg.climateAware.repositorios.CampanaRepositorio;
import com.egg.climateAware.repositorios.EmpresaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmpresaServicio {

    @Autowired
    private EmpresaRepositorio empresaRepositorio;

    @Autowired
    private CampanaRepositorio campanaRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void registrarEmpresa(MultipartFile archivo, String nombreEmpresa, String cuit,
            String direccion, String rubro, String email, String password, String password2) throws Exception {

        validar(nombreEmpresa, cuit, direccion, rubro, email, password, password2);

        Empresa empresa = new Empresa();

        empresa.setNombreEmpresa(nombreEmpresa);
        empresa.setCuit(cuit);
        empresa.setDireccion(direccion);
        empresa.setRubro(rubro);
        empresa.setEmail(email);
        empresa.setAltaBaja(false);
        empresa.setFechaAlta(new Date());
        empresa.setPassword(new BCryptPasswordEncoder().encode(password));
        empresa.setRoles(Rol.EMP);
        Imagen imagen = imagenServicio.guardar(archivo);
        empresa.setImagen(imagen);

        empresaRepositorio.save(empresa);

    }

    @Transactional
    public void actualizarEmpresa(MultipartFile archivo, String id, String nombreEmpresa, String cuit,
            String direccion, String rubro, String email, String password, String password2)
            throws Exception {

        Optional<Empresa> respuesta = empresaRepositorio.findById(id);

        validar(nombreEmpresa, cuit, direccion, rubro, email, password, password2);

        if (respuesta.isPresent()) {

            Empresa empresa = respuesta.get();

            empresa.setNombreEmpresa(nombreEmpresa);
            empresa.setAltaBaja(Boolean.TRUE);
            empresa.setCuit(cuit);
            empresa.setDireccion(direccion);
            empresa.setRubro(rubro);
            empresa.setEmail(email);

            empresa.setPassword(new BCryptPasswordEncoder().encode(password));

            empresa.setRoles(Rol.EMP);

            String idImagen = null;

            if (archivo.getSize() > 0) {

                idImagen = empresa.getImagen().getId();
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                empresa.setImagen(imagen);
            }

            empresaRepositorio.save(empresa);

        }

    }

    @Transactional(readOnly = true)
    public Empresa getOne(String id) {
        return empresaRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Empresa> listarEmpresas() {
        List<Empresa> empresas = new ArrayList();

        empresas = empresaRepositorio.findAll();

        return empresas;
    }

    @Transactional
    public void darDeBajaEmpresa(String id) throws Exception {

        Optional<Empresa> respuesta = empresaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Empresa empresa = respuesta.get();
            empresa.setAltaBaja(false);

            empresaRepositorio.save(empresa);
        }

    }

    @Transactional
    public void darDeAltaEmpresa(String id) throws Exception {

        Optional<Empresa> respuesta = empresaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Empresa empresa = respuesta.get();
            empresa.setAltaBaja(true);

            empresaRepositorio.save(empresa);
        }

    }

    private void validar(String nombreEmpresa, String cuit, String direccion,
            String rubro, String email, String password, String password2) throws Exception {

        // Verificar si el email ya existe en la base de datos
        Empresa usuarioExistente = empresaRepositorio.buscarPorEmail(email);
        if (usuarioExistente != null) {
            throw new Exception("El email ingresado ya está registrado");
        }

        if (nombreEmpresa.isEmpty() || nombreEmpresa == null) {
            throw new Exception("Debe ingrear el nombre de la Empresa");
        }

        if (cuit.isEmpty() || cuit == null) {
            throw new Exception("Debe ingrear el CUIT de la Empresa");
        }

        if (direccion.isEmpty() || direccion == null) {
            throw new Exception("Debe ingrear la direccion de la Empresa");
        }

        if (rubro.isEmpty() || rubro == null) {
            throw new Exception("Debe especificar el rubro al que pertenece su Empresa");
        }

        if (email.isEmpty() || email == null) {
            throw new Exception("Debe debe ingresar email valido de su Empresa");
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
