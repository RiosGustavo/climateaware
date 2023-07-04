
package com.egg.climateAware.controladoras;

import com.egg.climateAware.entidades.Publicacion;
import com.egg.climateAware.entidades.Usuario;
import com.egg.climateAware.servicios.CampanaServicio;
import com.egg.climateAware.servicios.PublicacionServicio;
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
@RequestMapping("/publicacion")
public class PublicacionControlador {

    @Autowired
    PublicacionServicio publicacionServicio = new PublicacionServicio();
    
    @Autowired
    private CampanaServicio campanaServicio;

    @GetMapping("/{id}")
    public String publicacion_one(@PathVariable String id, ModelMap modelo){
        modelo.put("publicacion",publicacionServicio.getOne(id));
        return "publicacion_one.html";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_VOT')")
    @GetMapping("campana/{idCampana}/crearPublicacion")
    public String registrar(@PathVariable String idCampana,  ModelMap modelo,HttpSession session) {
        if (session.getAttribute("usuariosession") == null || !((Usuario) session.getAttribute("usuariosession")).getAltaBaja()) {
            return "redirect:/votante/panel-principal";
        }
        modelo.put("campana", campanaServicio.getOne(idCampana));
        return "publicacion_form.html";
    }
    
    
    @PostMapping("campana/{idCampana}/creacion")
    public String creacion(MultipartFile archivo, @PathVariable String idCampana,@RequestParam() String titulo,@RequestParam() String descripcion,
            @RequestParam() String cuerpo, @RequestParam() String video, ModelMap modelo, HttpSession session) {
        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            publicacionServicio.crearPublicacion(archivo, titulo, descripcion, cuerpo, video, logueado.getId(),idCampana);
            modelo.put("exito", "Publicacion creada exitosamente!");
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("campana", campanaServicio.getOne(idCampana));
            modelo.put("titulo", titulo);
            modelo.put("descripcion", descripcion);
            modelo.put("cuerpo", cuerpo);
            modelo.put("archivo", archivo);
            modelo.put("video", video);
            return "publicacion_form.html";
        }
        return "redirect:/campana/campana_one/{idCampana}";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@RequestParam String id, ModelMap modelo) {
         modelo.put("publicacion",publicacionServicio.getOne(id));
        return "modificar_publicacion.html";
    }

    @PostMapping("/modificacion/{id}")
    public String modificacion(@RequestParam @PathVariable String id,@RequestParam(required = false) MultipartFile archivo,
           @RequestParam() String titulo,@RequestParam() String descripcion, @RequestParam(required = false) String cuerpo, @RequestParam(required = false) String video, ModelMap modelo) {
        try {
               publicacionServicio.modificarPublicacion(archivo, id, titulo,descripcion,cuerpo, video);
            modelo.put("exito", "La publicacion se ha modificado exitosamente!");
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
           modelo.put("publicacion",publicacionServicio.getOne(id));
            return "modificar.html";
        }
        return "redirect:/publicacion/id";
    }
        @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Publicacion> publicacion = publicacionServicio.listarPublicaciones();
        
        modelo.addAttribute("publicacion", publicacion);
        return "publicacion_list.html"; 
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@RequestParam @PathVariable String id) throws Exception{
       publicacionServicio.bajaPublicacion(id);
        return "index.html";
    }
    @GetMapping("/listar")
    public String listar() throws Exception{
       publicacionServicio.listarPublicaciones();
       
       return "publicacion_list.html";
    }
}
