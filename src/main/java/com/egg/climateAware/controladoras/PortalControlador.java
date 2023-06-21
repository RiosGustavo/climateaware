/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.climateAware.controladoras;

import com.egg.climateAware.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PortalControlador {
    
//    @Autowired
//    CampañaServicio campañaServicio = new CampañaServicio();
    
//    @Autowired
//    PublicacionServicio publicacionServicio = new PublicacionServicio();
    
      @Autowired
      UsuarioServicio usuarioServicio = new UsuarioServicio();
    

    @GetMapping("/")
    public String index(ModelMap modelo) {
//        modelo.put("campañas",campañaServicio.listarCampañas());
//        moduleo.put("publicaciones",publicacionServicio.listarPublicaciones());

//ORDENAR POR FECHA EN EL REPOSITORIO
        return "index.html";
    }
    
    
      @GetMapping("/login")
    public String login(String error, ModelMap modelo) {
        if (error!=null){
            modelo.put("error", "Mail o contraseña incorrectos");
        }
        return "login_form.html";
    }

}
