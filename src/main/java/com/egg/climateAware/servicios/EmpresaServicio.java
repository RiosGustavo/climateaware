package com.egg.climateAware.servicios;

import com.egg.climateAware.entidades.Campana;
import com.egg.climateAware.entidades.Empresa;
import com.egg.climateAware.entidades.Imagen;
import com.egg.climateAware.enumeraciones.Rol;
import com.egg.climateAware.repositorios.CampanaRepositorio;
import com.egg.climateAware.repositorios.EmpresaRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
            String direccion, String rubro, String email, String password, String password2, String idCampana) throws Exception {

        validar(nombreEmpresa, cuit, direccion, rubro, email, password, password2, idCampana);

        Optional<Campana> respuestaCampana = campanaRepositorio.findById(idCampana);

        Campana campana = new Campana();

        if (respuestaCampana.isPresent()) {
            campana = respuestaCampana.get();

        }

        Empresa empresa = new Empresa();

        empresa.setNombreEmpresa(nombreEmpresa);
        empresa.setCuit(cuit);
        empresa.setDireccion(direccion);
        empresa.setRubro(rubro);
        empresa.setEmail(email);
        empresa.setAltaBaja(Boolean.FALSE);

        empresa.setCampanas((List<Campana>) campana);


        //// falta agregar la seguridad  "new BCryptPasswordEncoder().encode(password)"
        empresa.setPassword(password);

        
        empresa.setRoles(Rol.EMP);

        
        Imagen imagen = imagenServicio.guardar(archivo);

        empresa.setImagen(imagen);

        empresaRepositorio.save(empresa);

    }

    @Transactional
    public void actualizarEmpresa(MultipartFile archivo, String id, String nombreEmpresa, String cuit,
            String direccion, String rubro, String email, String password, String password2, String idCampana) throws Exception {

        Optional<Empresa> respuesta = empresaRepositorio.findById(id);

        if (id == null || id.isEmpty()) {
            throw new Exception("Debe ingrear el id de la Empresa");

        }
        validar(nombreEmpresa, cuit, direccion, rubro, email, password, password2, idCampana);

        Optional<Campana> respuestaCampana = campanaRepositorio.findById(id);

        Campana campana = new Campana();

        if (respuestaCampana.isPresent()) {
            campana = respuestaCampana.get();

        }

        if (respuesta.isPresent()) {

            Empresa empresa = respuesta.get();

            empresa.setNombreEmpresa(nombreEmpresa);
            empresa.setAltaBaja(Boolean.TRUE);
            empresa.setCuit(cuit);
            empresa.setDireccion(direccion);
            empresa.setRubro(rubro);
            empresa.setEmail(email);

            //// falta agregar la seguridad  "new BCryptPasswordEncoder().encode(password)"
            empresa.setPassword(password);

            empresa.setRoles(Rol.EMP);

            String idImagen = null;
           
            if (empresa.getImagen() != null) {
                idImagen = empresa.getImagen().getId();
            }
           
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

            empresa.setImagen(imagen);

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

        if (id == null || id.isEmpty()) {
            throw new Exception("Debe ingrear el id de la Empresa");

        }

        if (respuesta.isPresent()) {
            Empresa empresa = respuesta.get();
            empresa.setAltaBaja(Boolean.FALSE);

            empresaRepositorio.save(empresa);
        }

    }


    private void validar(String nombreEmpresa, String cuit, String direccion,
            String rubro, String email, String password, String password2, String idCampana) throws Exception {

        if (nombreEmpresa.isEmpty() || nombreEmpresa == null) {
            throw new Exception("Debe ingrear el Nombre de la Empresa");
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

        if (password.isEmpty() || password == null || password.length() <= 8) {
            throw new Exception("Debe ingrear un password y de mas de 8 caracteres");
        }

        if (!password.equals(password2)) {
            throw new Exception("Los password ingresados deben ser iguales");
        }

        if (idCampana.isEmpty() || idCampana == null) {
            throw new Exception("La campana no puede ser nulo o estar vacia");
        }

    }

}
