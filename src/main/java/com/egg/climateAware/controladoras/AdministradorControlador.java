/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.climateAware.controladoras;

import com.egg.climateAware.entidades.Blogger;
import com.egg.climateAware.entidades.Empresa;
import com.egg.climateAware.entidades.Votante;
import com.egg.climateAware.repositorios.EmpresaRepositorio;
import com.egg.climateAware.servicios.BloggerServicio;
import com.egg.climateAware.servicios.EmpresaServicio;
import com.egg.climateAware.servicios.UsuarioServicio;
import com.egg.climateAware.servicios.VotanteServicio;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdministradorControlador {

    @Autowired
    VotanteServicio votanteServicio = new VotanteServicio();

    @Autowired
    EmpresaServicio empresaServicio = new EmpresaServicio();

    @Autowired
    UsuarioServicio usuarioServicio = new UsuarioServicio();

    @Autowired
    BloggerServicio bloggerServicio = new BloggerServicio();

    @GetMapping("/dashboard")
    public String panelAdministrativo() {

        return "panel_admin.html";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@RequestParam @PathVariable String id) throws Exception {
        usuarioServicio.cambiarEstado(id);
        return "index.html";
    }

    //Cambiar usuario a blogger
    @GetMapping("/rolBlogger/{id}")
    public String rolBlogger(@PathVariable String id) {

        bloggerServicio.crearBlogger(id);

        return "redirect:/admin/bloggers";
    }

    //Cambiar blogger a votante
    @GetMapping("/rolVotante/{id}")
    public String bloggerAvotante(@PathVariable String id) {
        bloggerServicio.bloggerAvotante(id);
        return "redirect:/admin/votantes";
    }

    //-----------------------------MOTOR BUSQUEDA---------------------------------
    @GetMapping("/votantes")
    public String busquedaVotantes(@RequestParam(required = false) String termino, ModelMap modelo) {

        List<Votante> votantes = new ArrayList<>();

        if (termino != null && !termino.isEmpty()) {
            votantes = votanteServicio.buscarVotantesPorTermino(termino.toLowerCase());

            if (votantes.isEmpty()) {

                votantes = votanteServicio.listarVotantes();
                modelo.put("error", "No se encontró nada con el término ingresado. Intente de otra manera.");
            }

        } else {

            votantes = votanteServicio.listarVotantes();
        }

        modelo.addAttribute("votantes", votantes);

        return "votantes_list.html";
    }

    @GetMapping("/empresas")
    public String busquedaEmpresas(@RequestParam(required = false) String termino, @RequestParam(required = false) String estado, @RequestParam(required = false) String orden, ModelMap modelo) {

        List<Empresa> empresas = new ArrayList<>();

        if (termino != null && !termino.isEmpty()) {
            empresas = empresaServicio.search(termino, estado, orden);
            
        } else {
            empresas = empresaServicio.search2(estado, orden);
        } 

        if (empresas.isEmpty()) {
            empresas = empresaServicio.listarEmpresas();
            modelo.put("error", "No se encontró nada con el término ingresado. Intente de otra manera.");
        }

        modelo.put("termino", termino);
        modelo.put("estado", estado);
        modelo.put("orden", orden);
        modelo.addAttribute("empresas", empresas);

        return "empresa_list.html";
    }

    @GetMapping("/bloggers")
    public String busquedaBloggers(@RequestParam(required = false) String termino, ModelMap modelo) {

        List<Blogger> bloggers = new ArrayList<>();

        if (termino != null && !termino.isEmpty()) {
            bloggers = bloggerServicio.buscarBloggersPorTermino(termino.toLowerCase());

            if (bloggers.isEmpty()) {

                bloggers = bloggerServicio.listarBloggers();
                modelo.put("error", "No se encontró nada con el término ingresado. Intente de otra manera.");
            }

        } else {

            bloggers = bloggerServicio.listarBloggers();
        }

        modelo.addAttribute("bloggers", bloggers);

        return "bloggers_list.html";
    }
}
