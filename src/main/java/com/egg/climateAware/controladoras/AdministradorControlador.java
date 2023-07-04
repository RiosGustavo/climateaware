/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.climateAware.controladoras;

import com.egg.climateAware.entidades.Empresa;
import com.egg.climateAware.entidades.Votante;
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

//    @Autowired
//    AdministadorServicio administradorServicio = new AdministradorServicio();
    @Autowired
    VotanteServicio votanteServicio = new VotanteServicio();

    @Autowired
    EmpresaServicio empresaServicio = new EmpresaServicio();

    @Autowired
    UsuarioServicio usuarioServicio = new UsuarioServicio();

    @GetMapping("/dashboard")
    public String panelAdministrativo() {

        return "panel_admin.html";
    }

//LISTA DE TODOS
    @GetMapping("/lista")
    public String lista(ModelMap modelo) {
        modelo.put("usuarios", usuarioServicio.listaUsuarios());
        return "lista.html";
    }

//LISTAS SEPARADAS  
    @GetMapping("/listaAdministrador")
    public String listaAdministradores(ModelMap modelo) {
//       modelo.put("administradores",administradorServicio.listaAdministradores());
        return "lista_admin.html";
    }

    @GetMapping("/listaEmpresas")
    public String listaEmpresas(ModelMap modelo) {
//       modelo.put("empresas",administradorServicio.listarEmpresas());
        return "lista_empresa.html";
    }

    @GetMapping("/listaVotantes")
    public String listaVotantes(ModelMap modelo) {
        modelo.put("votantes", votanteServicio.listarVotantes());
        return "lista_votante.html";
    }

    @GetMapping("/ListaBlogger")
    public String listaBlogger(ModelMap modelo) {
//         modelo.put("bloggers",bloggerServicio.listarBloggers());
        return "lista_blogger.html";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@RequestParam @PathVariable String id) throws Exception {
        usuarioServicio.cambiarEstado(id);
        return "index.html";
    }
    
    
    //-----------------------------MOTOR BUSQUEDA---------------------------------
    @GetMapping("/votantes")
    public String busquedaVotantes(@RequestParam(required = false) String termino, Model modelo) {

        List<Votante> votantes = new ArrayList<>();

        if (termino != null && !termino.isEmpty()) {
            votantes = votanteServicio.buscarVotantesPorTermino(termino.toLowerCase());
        }else{
        
            votantes = votanteServicio.listarVotantes();
        }

        modelo.addAttribute("votantes", votantes);

        return "votantes_list.html";
    }
    
    
     @GetMapping("/empresas")
    public String busquedaEmpresas(@RequestParam(required = false) String termino, Model modelo) {

        List<Empresa> empresas = new ArrayList<>();

        if (termino != null && !termino.isEmpty()) {
            empresas = empresaServicio.buscarEmpresasPorTermino(termino.toLowerCase());
        }else{
        
           empresas = empresaServicio.listarEmpresas();
        }

        modelo.addAttribute("empresas", empresas);

        return "empresa_list.html";
    }
    
}
