package com.egg.climateAware.controladoras;

import com.egg.climateAware.entidades.Campana;
import com.egg.climateAware.entidades.Publicacion;
import com.egg.climateAware.entidades.Usuario;
import com.egg.climateAware.repositorios.CampanaRepositorio;
import com.egg.climateAware.servicios.CampanaServicio;
import com.egg.climateAware.servicios.EmpresaServicio;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/campana")
public class CampanaControlador {

    @Autowired
    private CampanaServicio campanaServicio;

    @Autowired
    private EmpresaServicio empresaServicio;
    
    @Autowired
    private CampanaRepositorio campanaRepositorio;

    @Autowired
    private PublicacionServicio publicacionServicio;
    
    
    @PreAuthorize("hasAnyRole('ROLE_EMP')")

    @GetMapping("/registrar")
    public String registrar(HttpSession session) {
        if (session.getAttribute("usuariosession") == null || !((Usuario) session.getAttribute("usuariosession")).getAltaBaja()) {
            return "redirect:/empresa/panel-principal";
        }
        return "campana_form.html";
    }

    @PostMapping("/registro")
    public String registro(MultipartFile archivo, @RequestParam String titulo, @RequestParam String cuerpo, @RequestParam String descripcion,
            ModelMap modelo, HttpSession session) throws Exception {

        try {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            campanaServicio.crearCampana(archivo, titulo, cuerpo, descripcion, logueado.getId());
            modelo.put("exito", "La campaña fue creada correctamente");
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.addAttribute("titulo", titulo);
            modelo.addAttribute("cuerpo", cuerpo);
            modelo.addAttribute("descripcion", descripcion);
        }
         return "campana_form.html";

    }

    @GetMapping("/modificar/{idCampana}")
    public String modificar(@PathVariable String idCampana, ModelMap modelo) {

        modelo.put("campana", campanaServicio.getOne(idCampana));
        return "campana_modificar.html";
    }
  
    @PostMapping("/modificar/{idCampana}")
    public String modificar(MultipartFile archivo, @PathVariable String idCampana, String titulo,
            String cuerpo, String descripcion, ModelMap modelo) {
        try {
            campanaServicio.actualizarCampana(archivo, titulo, cuerpo, descripcion, idCampana);
            modelo.put("exito", "Campaña actualizada correctamente");
            return "redirect:/campana/lista";
        } catch (Exception ex) {
            modelo.put("campana", campanaServicio.getOne(idCampana));
            modelo.put("error", ex.getMessage());
            return "campana_modificar.html";
        }

    }

    @GetMapping("/baja/{idCampana}")
    public String darDeBajaCamapna(@PathVariable String idCampana) throws Exception {
        campanaServicio.darDeBajaCampana(idCampana);
        return "redirect:/campana/lista";
    }
    
     @GetMapping("/alta/{idCampana}")
    public String darDeAltaCamapna(@PathVariable String idCampana) throws Exception {
        campanaServicio.darDeAltaCampana(idCampana);
        return "redirect:/campana/lista";
    }
    
    
    
    @GetMapping("/campana_one/{idCampana}")
    public String mostrarDetalleCampaña(@PathVariable String idCampana,  ModelMap modelo){
        List<Publicacion> publicaciones = publicacionServicio.publicacionesPorCampanaActivas(idCampana);
        
        modelo.put("publicaciones", publicaciones);
        modelo.put("campana", campanaServicio.getOne(idCampana));
        return "campana_one.html";
    }
    
        //-----------------------------MOTOR BUSQUEDA---------------------------------
    @GetMapping("/lista")
    public String listadoCampanas(@RequestParam(required = false) String termino, Model modelo, HttpSession session) {
        
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        List<Campana> campanas = new ArrayList<>();

        if (termino != null && !termino.isEmpty()) {
            campanas = campanaServicio.buscarCampanasPorTitulo(termino.toLowerCase());
        }else{
        
            campanas = campanaRepositorio.findAllOrderByfecha_altaDesc();
        }

        modelo.addAttribute("campanas", campanas);

        return "campana_list.html";
    }
    

}
