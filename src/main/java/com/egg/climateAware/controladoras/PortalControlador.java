
package com.egg.climateAware.controladoras;

import com.egg.climateAware.entidades.Usuario;
import com.egg.climateAware.servicios.EmpresaServicio;
import com.egg.climateAware.servicios.UsuarioServicio;
import com.egg.climateAware.servicios.VotanteServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class PortalControlador {
    
//    @Autowired
//    CampañaServicio campañaServicio = new CampañaServicio();
    
//    @Autowired
//    PublicacionServicio publicacionServicio = new PublicacionServicio();
    
      @Autowired
      UsuarioServicio usuarioServicio = new UsuarioServicio();
      
      @Autowired
      VotanteServicio votanteServicio = new VotanteServicio();
      
      @Autowired
      EmpresaServicio empresaServicio = new EmpresaServicio();

    @GetMapping("/")
    public String index(ModelMap modelo) {
//        modelo.put("campañas",campañaServicio.listarCampañas());
//        moduleo.put("publicaciones",publicacionServicio.listarPublicaciones());

//ORDENAR POR FECHA EN EL REPOSITORIO
        return "index.html";
    }
    
    
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        
        if (error != null) {
            modelo.put("error", "Usuario o contraseña inválidos");
        }
        return "login.html";
    }

     @GetMapping("/registrar")
    public String registrar() {

        return "register.html";
    }
    @PostMapping("/registro")
    public String registro(MultipartFile archivo,@RequestParam String email, @RequestParam String password, @RequestParam String password2,@RequestParam String rol,
            @RequestParam(required = false) String nombreApellido ,@RequestParam(required = false) String dni ,@RequestParam(required = false) String direccionUsuario , 
            @RequestParam(required = false) String nombreEmpresa,@RequestParam(required = false) String cuit,@RequestParam(required = false) String direccionEmpresa,
            @RequestParam(required = false) String rubro ,ModelMap modelo,RedirectAttributes redirectAttributes) {
        try {
            
            if (rol.equalsIgnoreCase("votante")){
            
            votanteServicio.crearVotante(archivo, nombreApellido, dni, direccionUsuario, email, password, password2);
            
            }else if (rol.equalsIgnoreCase("empresa")){
            
            empresaServicio.registrarEmpresa(archivo, nombreEmpresa, cuit, direccionEmpresa, rubro, email, password, password2);
            }else {
            
                usuarioServicio.crearUsuario(archivo, email, password, password2);
            }
            
            redirectAttributes.addFlashAttribute("exito", "¡Has sido registrado con éxito!");
            
             return "redirect:/login";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            modelo.put("email", email);
            
            
            return "register.html";
        }

    }

}
