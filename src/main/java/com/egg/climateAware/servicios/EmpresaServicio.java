package com.egg.climateAware.servicios;

import com.egg.climateAware.entidades.Campaña;
import com.egg.climateAware.entidades.Empresa;
import com.egg.climateAware.entidades.Imagen;
import com.egg.climateAware.enumeraciones.Rol;
import com.egg.climateAware.repositorios.CampañaRepositorio;
import com.egg.climateAware.repositorios.EmpresaRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

//import org.springframework.security.core.userdetails.UserDetailsService; /// ver si es necesario poner esto abjoa en el public class  implements UserDetailsService
@Service
public class EmpresaServicio {

    @Autowired
    private EmpresaRepositorio empresaRepositorio;

    @Autowired
    private CampañaRepositorio campañaRepositorio;

    //// falta implementar la entidad imagen
    @Autowired
    private ImagenServicio imagenServicio;

    ///// FALTA AGREGAR EL throws MiExcepcion
    @Transactional
    public void registrarEmpresa(MultipartFile archivo, String nombreEmpresa, String cuit,
            String direccion, String rubro, String email, String password, String password2, String idCampaña) throws Exception {

        validar(nombreEmpresa, cuit, direccion, rubro, email, password, password2, idCampaña);

        Optional<Campaña> respuestaCampaña = campañaRepositorio.findById(idCampaña);

        Campaña campaña = new Campaña();

        if (respuestaCampaña.isPresent()) {
            campaña = respuestaCampaña.get();

        }

        Empresa empresa = new Empresa();

        empresa.setNombreEmpresa(nombreEmpresa);
        empresa.setCuit(cuit);
        empresa.setDireccion(direccion);
        empresa.setRubro(rubro);
        empresa.setEmail(email);
        empresa.setAltaBaja(Boolean.FALSE);
        empresa.setCampañas((List<Campaña>) campaña);

        //// falta agregar la seguridad  "new BCryptPasswordEncoder().encode(password)"
        empresa.setPassword(password);

        //// FALTA TRAER EL USUARIO ACUTALIZADO 
        empresa.setRoles(Rol.EMP);

        ////// falta agregar la parete de la imagen 
        Imagen imagen = imagenServicio.guardar(archivo);

        empresa.setImagen(imagen);

        empresaRepositorio.save(empresa);

    }

    @Transactional
    public void actualizarEmpresa(MultipartFile archivo, String id, String nombreEmpresa, String cuit,
            String direccion, String rubro, String email, String password, String password2, String idCampaña) throws Exception {

        Optional<Empresa> respuesta = empresaRepositorio.findById(id);

        if (id == null || id.isEmpty()) {
            throw new Exception("Debe ingrear el id de la Empresa");

        }
        validar(nombreEmpresa, cuit, direccion, rubro, email, password, password2, idCampaña);

        Optional<Campaña> respuestaCampaña = campañaRepositorio.findById(id);

        Campaña campaña = new Campaña();

        if (respuestaCampaña.isPresent()) {
            campaña = respuestaCampaña.get();

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
            /// falta implementar la entidad imagen 
            if (empresa.getImagen() != null) {
                idImagen = empresa.getImagen().getId();
            }
            //// falta implentar la entidad imagen
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

    //// FALTA IMPLEMENTAR LA SEGURIDAD PARA CUANDO SE LOGUE LA EMPRESA  "loadUserByUsername"
///// FALTA AGREGAR EL throws MiExcepcion
    private void validar(String nombreEmpresa, String cuit, String direccion,
            String rubro, String email, String password, String password2, String idCampaña) throws Exception {

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

        if (idCampaña.isEmpty() || idCampaña == null) {
            throw new Exception("La campaña no puede ser nulo o estar vacia");
        }

    }

}
