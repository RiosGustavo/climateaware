package com.egg.climateAware.controladoras;

import com.egg.climateAware.entidades.Campana;
import com.egg.climateAware.entidades.Empresa;
import com.egg.climateAware.entidades.Usuario;
import com.egg.climateAware.servicios.CampanaServicio;
import com.egg.climateAware.servicios.EmpresaServicio;
import com.egg.climateAware.servicios.UsuarioServicio;
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
@RequestMapping("/empresa")
public class EmpresaControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private EmpresaServicio empresaServicio;

    @Autowired
    private CampanaServicio campanaServicio;

   
    @PreAuthorize("hasAnyRole('ROLE_EMP')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Empresa empresa = empresaServicio.getOne(usuario.getId());
        modelo.addAttribute("empresa", empresa);
        return "perfil_empresa.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_EMP')")
    @PostMapping("/perfil/{id}")
    public String modificacionPerfil(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre, @RequestParam String direccion, ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Empresa empresa = empresaServicio.getOne(usuario.getId());
        try {
            empresaServicio.modificarEmpresa(archivo, id, nombre, direccion);
            modelo.put("exito", "Datos guardados correctamente");
            modelo.addAttribute("empresa", empresa);
            return "perfil_empresa.html";
        } catch (Exception ex) {
            modelo.addAttribute("empresa", empresa);
            modelo.put("error", ex.getMessage());
            return "perfil_empresa.html";
        }
    }

    
    
    
    //Panel empresa
    @PreAuthorize("hasAnyRole('ROLE_EMP')")
    @GetMapping("/panel-principal")
    public String panelEmpresa(ModelMap modelo, HttpSession session) {
        //Empresa
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        Usuario usuario = usuarioServicio.getOne(logueado.getId());
        Empresa empresa = empresaServicio.getOne(logueado.getId());

        //Campañas de la empresa
        List<Campana> campanas = campanaServicio.campanasPorEmpresa(logueado.getId());
        modelo.addAttribute("campanas", campanas);
        modelo.addAttribute("empresa", empresa);

        return "panel_empresa.html";
    }

    //Dar de baja una empresa
    @GetMapping("/baja/{id}")
    public String darDeBajaEmpresa(@PathVariable String id) {

        try {
            empresaServicio.darDeBajaEmpresa(id);
        } catch (Exception e) {
            System.err.println( e.getMessage());
        }
        return "redirect:/admin/empresas";
    }

    //Dar de alta una empresa
    @GetMapping("/alta/{id}")
    public String darDeAlta(@PathVariable String id) {
        try {
            empresaServicio.darDeAltaEmpresa(id);
        } catch (Exception e) {
             System.err.println( e.getMessage());
        }
        return "redirect:/admin/empresas";

    }

}
