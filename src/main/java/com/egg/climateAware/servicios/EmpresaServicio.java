package com.egg.climateAware.servicios;

import com.egg.climateAware.entidades.Empresa;
import com.egg.climateAware.repositorios.CampañaRepositorio;
import com.egg.climateAware.repositorios.EmpresaRepositorio;
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
            String direccion, String rubro, String email, String password, String password2, String idCampaña) {
        
        validar(nombreEmpresa, cuit, direccion, rubro,email, password, password2, idCampaña);
        
        Empresa empresa = new Empresa();
        
        empresa.setNombreEmpresa(nombreEmpresa);
        empresa.setCuit(cuit);
        empresa.setDireccion(direccion);
        empresa.setRubro(rubro);
        empresa.setEmail(email);
        
        //// falta agregar la seguridad  "new BCryptPasswordEncoder().encode(password)"
        empresa.setPassword(password);
        
        //// FALTA TRAER EL USUARIO ACUTALIZADO 
        empresa.setRol(Rol.EMPRESA);
        
        ////// falta agregar la parete de la imagen 
        Imagen imagen = imagenServicio.guardar(archivo);
        
        empresa.setImagen(imagen);
        
       empresaRepositorio.save(empresa);
            

    }
    
    public void actualizarEmpresa (MultipartFile archivo, String nombreEmpresa, String cuit,
            String direccion, String rubro, String email, String password, String password2, String idCampaña){
        
        
    }
    
    
    

    ///// FALTA AGREGAR EL throws MiExcepcion
    private void validar(String nombreEmpresa, String cuit, String direccion, String rubro, String email,
            String password, String password2, String idCampaña) {

        if (nombreEmpresa.isEmpty() || nombreEmpresa == null) {
            throw new MiExcepcion("Debe ingrear el Nombre de la Empresa");
        }

        if (cuit.isEmpty() || cuit == null) {
            throw new MiExcepcion("Debe ingrear el CUIT de la Empresa");
        }

        if (direccion.isEmpty() || direccion == null) {
            throw new MiExcepcion("Debe ingrear la direccion de la Empresa");
        }
        
        if (rubro.isEmpty() || rubro == null) {
            throw new MiExcepcion("Debe especificar el rubro al que pertenece su Empresa");
        }
        
        if (email.isEmpty() || email== null) {
            throw new MiExcepcion("Debe debe ingresar email valido de su Empresa");
        }

        if (password.isEmpty() || password == null || password.length() <= 8) {
            throw new MiExcepcion("Debe ingrear un password y de mas de 8 caracteres");
        }

        if (!password.equals(password2)) {
            throw new MiExcepcion("Los password ingresados deben ser iguales");
        }
        
        if (idCampaña.isEmpty() || idCampaña == null) {
            throw new MiExcepcion("La campaña no puede ser nulo o estar vacia");
        }
        
        
    }
}
