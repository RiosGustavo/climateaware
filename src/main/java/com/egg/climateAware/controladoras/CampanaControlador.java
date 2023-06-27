package com.egg.climateAware.controladoras;

import com.egg.climateAware.entidades.Campana;
import com.egg.climateAware.entidades.Empresa;
import com.egg.climateAware.servicios.CampanaServicio;
import com.egg.climateAware.servicios.EmpresaServicio;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/campana")
public class CampanaControlador {

    @Autowired
    private CampanaServicio campanaServicio;

    @Autowired
    private EmpresaServicio empresaServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) {
        List<Empresa> empresas = empresaServicio.listarEmpresas();
        modelo.addAttribute("empresas", empresas);
        return "campana_form.html"; //// PONER ACA EL NOMBRE DEL ARCHIVO HTML QUE LOS CHICOS HICIERON PARA CREAR LAS CAMPAÑAS
    }

    @PostMapping("/registro")
    public String registro(MultipartFile archivo, @RequestParam(required = false) String titulo, @RequestParam String cuerpo,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaAlta, @RequestParam String idPublicacion,
            @RequestParam String id, ModelMap modelo) throws Exception {
        
        try {
            campanaServicio.crearCampana(archivo, titulo, cuerpo, fechaAlta, idPublicacion, id);

            modelo.put("exito", "Campana Creada Exitosamente");

            
        } catch (Exception ex) {
            List<Empresa> empresas = empresaServicio.listarEmpresas();
            modelo.addAttribute("empresas", empresas);

            modelo.put("error", ex.getMessage());

            return "campana_form.html";  

        }
        return "index.html"; 

    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Campana> campanas = campanaServicio.listarCampanas();
        
        modelo.addAttribute("campanas", campanas);
        return "campana_list.html"; 
    }

    @GetMapping("/modificar/{idCampana}")
    public String modificar(@PathVariable String idCampana, ModelMap modelo) {
        modelo.put("campana", campanaServicio.getOne(idCampana));
        List<Empresa> empresas = empresaServicio.listarEmpresas();

        modelo.addAttribute("empresas", empresas);
        return "campana_modificar.html"; 
    }

    @PostMapping("/modificar/{idCampana}")
    public String modificar(MultipartFile archivo, String idCampana, String titulo,
            String cuerpo, Date fechaAlta, String idPublicacion, String id, ModelMap modelo) {
        try {
            List<Empresa> empresas = empresaServicio.listarEmpresas();
            modelo.addAttribute("empresas", empresas);

            campanaServicio.actualizarCampana(archivo, idCampana, titulo, cuerpo, fechaAlta, idPublicacion, id);
            return "redirect:../lista";
        } catch (Exception ex) {
            List<Empresa> empresas = empresaServicio.listarEmpresas();
            modelo.addAttribute("empresas", empresas);
            modelo.put("error", ex.getMessage());
            return "campana_modificar.html"; 
        }

    }
    
   
    

    @GetMapping("/eliminar/{id}")
    public String darDeBajaCamapna(@PathVariable String idCampana) throws Exception {
        campanaServicio.darDeBajaCampana(idCampana);

        return "redirec.../lista";

    }

}
