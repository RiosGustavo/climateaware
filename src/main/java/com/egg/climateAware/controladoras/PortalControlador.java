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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
    public String registro(MultipartFile archivo,@RequestParam String email, @RequestParam String password1, @RequestParam String password2, ModelMap modelo) {
        try {
            usuarioServicio.crearUsuario(archivo,email, password1, password2);
            modelo.put("exito", "¡Felicitaciones! Ya sos parte de Climate Aware");
            return "register.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            modelo.put("email", email);
            return "register.html";
        }

    }
}