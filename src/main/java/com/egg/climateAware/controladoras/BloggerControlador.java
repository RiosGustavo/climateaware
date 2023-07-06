
package com.egg.climateAware.controladoras;

import com.egg.climateAware.entidades.Blogger;
import com.egg.climateAware.entidades.Noticia;
import com.egg.climateAware.entidades.Usuario;
import com.egg.climateAware.servicios.BloggerServicio;
import com.egg.climateAware.servicios.NoticiaServicio;
import com.egg.climateAware.servicios.UsuarioServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/blogger")
public class BloggerControlador {
    
   
    @Autowired
    BloggerServicio bloggerServicio;
    
    @Autowired
    UsuarioServicio usuarioServicio;
    
    @Autowired
    NoticiaServicio noticiaServicio;
    
    @PreAuthorize("hasAnyRole('ROLE_ADM','ROLE_VOT','ROLE_EMP','ROLE_BLO')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Blogger blogger = bloggerServicio.getOne(usuario.getId());
        modelo.addAttribute("blogger", blogger);
        return "perfil_blogger.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADM','ROLE_VOT','ROLE_EMP','ROLE_BLO')")
    @PostMapping("/perfil/{id}")
    public String modificacionPerfil(MultipartFile archivo, @PathVariable String id, @RequestParam String nombreApellido, @RequestParam String direccion, ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Blogger blogger = bloggerServicio.getOne(usuario.getId());
        try {
            bloggerServicio.modificarBlogger(archivo, id, nombreApellido, direccion);
            modelo.put("exito", "Datos guardados correctamente");
            modelo.addAttribute("blogger", blogger);
            return "perfil_blogger.html";
        } catch (Exception ex) {
            modelo.addAttribute("blogger", blogger);
            modelo.put("error", ex.getMessage());
            return "perfil_blogger.html";
        }
    }

    //Panel blogger
    @PreAuthorize("hasAnyRole('ROLE_BLO')")
    @GetMapping("/panel-principal")
    public String panelBlogger(ModelMap modelo, HttpSession session) {
        //Votante
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        Usuario usuario = usuarioServicio.getOne(logueado.getId());
        Blogger blogger = bloggerServicio.getOne(logueado.getId());
        
        List<Noticia> noticias = noticiaServicio.noticiasPorBlogger(logueado.getId());
        
                
        modelo.addAttribute("blogger", blogger);
        modelo.addAttribute("noticias", noticias);

        return "panel_blogger.html";
    }

     //Dar de baja un blogger
     @GetMapping("/baja/{id}")
    public String darDeBaja(@PathVariable String id) throws Exception {

        bloggerServicio.darDeBajaBlogger(id);
        
        return "redirect:/admin/bloggers";
    }
    //Dar de alta un blogger
     @GetMapping("/alta/{id}")
    public String darDeAlta(@PathVariable String id) throws Exception {

        bloggerServicio.darDeAltaBlogger(id);
        
        return "redirect:/admin/bloggers";
    } 
    
    
    
    
}
