/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.climateAware.controladoras;

import com.egg.climateAware.entidades.Empresa;
import com.egg.climateAware.entidades.Publicacion;
import com.egg.climateAware.entidades.Usuario;
import com.egg.climateAware.entidades.Votante;
import com.egg.climateAware.servicios.PublicacionServicio;
import com.egg.climateAware.servicios.UsuarioServicio;
import com.egg.climateAware.servicios.VotanteServicio;
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
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/votante")
public class VotanteControlador {

    @Autowired
    VotanteServicio votanteServicio;
    @Autowired
    UsuarioServicio usuarioServicio;
    
    @Autowired
    PublicacionServicio publicacionServicio;
    

    @PreAuthorize("hasAnyRole('ROLE_ADM','ROLE_VOT','ROLE_EMP')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Votante votante = votanteServicio.getOne(usuario.getId());
        modelo.addAttribute("votante", votante);
        return "perfil_usuario.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADM','ROLE_VOT','ROLE_EMP')")
    @PostMapping("/perfil/{id}")
    public String modificacionPerfil(MultipartFile archivo, @PathVariable String id, @RequestParam String nombreApellido, @RequestParam String direccion, ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Votante votante = votanteServicio.getOne(usuario.getId());
        try {
            votanteServicio.modificarVotante(archivo, id, nombreApellido, direccion);
            modelo.put("exito", "Datos guardados correctamente");
            modelo.addAttribute("votante", votante);
            return "perfil_usuario.html";
        } catch (Exception ex) {
            modelo.addAttribute("votante", votante);
            modelo.put("error", ex.getMessage());
            return "perfil_usuario.html";
        }
    }

    //Panel votante
    @PreAuthorize("hasAnyRole('ROLE_VOT')")
    @GetMapping("/panel-principal")
    public String panelVotante(ModelMap modelo, HttpSession session) {
        //Votante
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        Usuario usuario = usuarioServicio.getOne(logueado.getId());
        Votante votante = votanteServicio.getOne(logueado.getId());
        List<Publicacion> publicaciones = publicacionServicio.publicacionesPorVotante(logueado.getId());
        
                
        modelo.addAttribute("votante", votante);
        modelo.addAttribute("publicaciones", publicaciones);

        return "panel_votante.html";
    }

     //Dar de baja un votante
     @GetMapping("/baja/{id}")
    public String darDeBaja(@PathVariable String id) throws Exception {

        votanteServicio.darDeBajaVotante(id);
        
        return "redirect:/admin/votantes";
    }
    //Dar de alta un votante
     @GetMapping("/alta/{id}")
    public String darDeAlta(@PathVariable String id) throws Exception {

        votanteServicio.darDeAltaVotante(id);
        
        return "redirect:/admin/votantes";
    }
    
   
    @GetMapping("/listar")
    public String listar() throws Exception {
        votanteServicio.listarVotantes();
        return "publicacion_list.html";
    }
}
