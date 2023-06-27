/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.climateAware.controladoras;

import com.egg.climateAware.entidades.Publicacion;
import com.egg.climateAware.servicios.PublicacionServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/{id}")
    public String id(@RequestParam @PathVariable String id, ModelMap modelo){
        modelo.put("publicacion",publicacionServicio.getOne(id));
        return "publicacion.html";
    }
    
    @GetMapping("/crear")
    public String crear() {
        return "publicacion_form.html";
    }

    @PostMapping("creacion")
    public String creacion(@RequestParam(required = false) String cuerpo,@RequestParam(required = false) MultipartFile archivo,
            @RequestParam(required = false) String video, ModelMap modelo) {
        try {

        publicacionServicio.crearPublicacion(archivo, cuerpo, video);
            modelo.put("exito", "Publicacion creada exitosamente!");
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("cuerpo", cuerpo);
            modelo.put("archivo", archivo);
            modelo.put("video", video);
            return "publicacion_form.html";
        }
        return "campaña.html ? redirect:/campaña/id ? redirect:/publicacion/id ?";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@RequestParam String id, ModelMap modelo) {
         modelo.put("publicacion",publicacionServicio.getOne(id));
        return "modificar_publicacion.html";
    }

    @PostMapping("/modificacion/{id}")
    public String modificacion(@RequestParam @PathVariable String id,@RequestParam(required = false) MultipartFile archivo,
            @RequestParam(required = false) String cuerpo, @RequestParam(required = false) String video, ModelMap modelo) {
        try {
               publicacionServicio.modificarPublicacion(archivo, id, cuerpo, video);
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

}
