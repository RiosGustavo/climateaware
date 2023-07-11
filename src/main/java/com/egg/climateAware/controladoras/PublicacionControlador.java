package com.egg.climateAware.controladoras;

import com.egg.climateAware.entidades.Publicacion;
import com.egg.climateAware.entidades.Usuario;
import com.egg.climateAware.servicios.CampanaServicio;
import com.egg.climateAware.servicios.PublicacionServicio;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/publicacion")
public class PublicacionControlador {

    @Autowired
    PublicacionServicio publicacionServicio = new PublicacionServicio();

    @Autowired
    private CampanaServicio campanaServicio;


    @GetMapping("publicacion_one/{idPublicacion}")
    public String publicacion_one(@PathVariable String idPublicacion, ModelMap modelo){
        modelo.put("publicacion",publicacionServicio.getOne(idPublicacion));
        return "publicacion_one.html";
    }

    @PostMapping("/{id}/votar")
    @ResponseBody
    public Integer votar(@PathVariable("id") String id, ModelMap modelo, HttpSession session) {
        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            Publicacion publicacion = publicacionServicio.getOne(id);
            modelo.put("exito", "Publicacion creada exitosamente!");
            modelo.put("usuario",logueado);
            if(logueado.getRoles().toString().equals("VOT")){
            publicacionServicio.votar(id, logueado.getId());
            }else{
                modelo.put("error","Debe ser un usuario votante.");
                return -2;
            }
            return publicacion.getVotos().size();
        } catch (Exception ex) {
            modelo.put("error",ex.getMessage());
            return -1;
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_VOT')")
    @GetMapping("campana/{idCampana}/crearPublicacion")
    public String registrar(@PathVariable String idCampana, ModelMap modelo, HttpSession session) {
        if (session.getAttribute("usuariosession") == null || !((Usuario) session.getAttribute("usuariosession")).getAltaBaja()) {
            return "redirect:/votante/panel-principal";
        }
        modelo.put("campana", campanaServicio.getOne(idCampana));
        return "publicacion_form.html";
    }

    @PostMapping("campana/{idCampana}/creacion")
    public String creacion(MultipartFile archivo, @PathVariable String idCampana, @RequestParam() String titulo, @RequestParam() String descripcion,
            @RequestParam() String cuerpo, @RequestParam() String video, ModelMap modelo, HttpSession session) {
        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            publicacionServicio.crearPublicacion(archivo, titulo, descripcion, cuerpo, video, logueado.getId(), idCampana);
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


    @GetMapping("/modificar/{idPublicacion}")
    public String modificar(@PathVariable String idPublicacion, ModelMap modelo) {
         modelo.put("publicacion",publicacionServicio.getOne(idPublicacion));
        return "publicacion_modificar.html";
    }

    @PostMapping("/modificar/{idPublicacion}")
    public String modificacion(@PathVariable String idPublicacion,@RequestParam(required = false) MultipartFile archivo,
           @RequestParam(required = false)  String titulo,@RequestParam(required = false)  String descripcion, @RequestParam(required = false) String cuerpo, @RequestParam(required = false) String video, ModelMap modelo) {
        try {
               publicacionServicio.modificarPublicacion(archivo, idPublicacion, titulo,descripcion,cuerpo, video);
            modelo.put("exito", "La publicacion se ha modificado exitosamente!");
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
           modelo.put("publicacion",publicacionServicio.getOne(idPublicacion));
            return "publicacion_modificar.html";
        }
        return "redirect:/publicacion/lista";
    }
  
    @GetMapping("/baja/{idPublicacion}")
    public String darDeBajaPublicacion(@PathVariable String idPublicacion) throws Exception {
        publicacionServicio.darDeBajaPublicacion(idPublicacion);
        return "redirect:/publicacion/lista";
    }
    
    @GetMapping("/alta/{idPublicacion}")
    public String darDeAltaPublicacion(@PathVariable String idPublicacion) throws Exception {
        publicacionServicio.darDeAltaPublicacion(idPublicacion);
        return "redirect:/publicacion/lista";
    }
    
         //-----------------------------MOTOR BUSQUEDA---------------------------------
    @GetMapping("/lista")
    public String listadoPublicaciones(@RequestParam(required = false) String termino, Model modelo, HttpSession session) {
        
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        List<Publicacion> publicaciones = new ArrayList<>();

        if (termino != null && !termino.isEmpty()) {
            publicaciones = publicacionServicio.publicacionPorTitulo(termino.toLowerCase());
        }else{
        
            publicaciones = publicacionServicio.publicacionPorFecha();
        }

        modelo.addAttribute("publicaciones", publicaciones);

        return "publicacion_list.html";
    }
}
