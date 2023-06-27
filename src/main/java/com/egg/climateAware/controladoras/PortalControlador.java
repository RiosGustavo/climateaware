package com.egg.climateAware.controladoras;

import com.egg.climateAware.entidades.Campana;
import com.egg.climateAware.entidades.Empresa;
import com.egg.climateAware.entidades.Usuario;
import com.egg.climateAware.entidades.Votante;
import com.egg.climateAware.servicios.CampanaServicio;
import com.egg.climateAware.servicios.EmpresaServicio;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    UsuarioServicio usuarioServicio;

    @Autowired
    VotanteServicio votanteServicio;

    @Autowired
    EmpresaServicio empresaServicio;

    @Autowired
    CampanaServicio campanaServicio;

    @GetMapping("/")
    public String index(ModelMap modelo, HttpSession session) {

        List<Campana> campanas = campanaServicio.listarCampanas();

        modelo.put("campañas", campanas);
//        moduleo.put("publicaciones",publicacionServicio.listarPublicaciones());

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
    public String registro(MultipartFile archivo, @RequestParam String email, @RequestParam String password, @RequestParam String password2, @RequestParam String rol,
            @RequestParam(required = false) String nombreApellido, @RequestParam(required = false) String dni, @RequestParam(required = false) String direccionUsuario,
            @RequestParam(required = false) String nombreEmpresa, @RequestParam(required = false) String cuit, @RequestParam(required = false) String direccionEmpresa,
            @RequestParam(required = false) String rubro, ModelMap modelo) {
        try {

            if (rol.equalsIgnoreCase("votante")) {

                votanteServicio.crearVotante(archivo, nombreApellido, dni, direccionUsuario, email, password, password2);

            } else if (rol.equalsIgnoreCase("empresa")) {

                empresaServicio.registrarEmpresa(archivo, nombreEmpresa, cuit, direccionEmpresa, rubro, email, password, password2);
            } else {

                usuarioServicio.crearUsuario(archivo, email, password, password2);
            }

            modelo.put("exito", "Usuario registrado correctamente");
            return "register.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            modelo.put("email", email);

            return "register.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADM','ROLE_EMP','ROLE_VOT')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado.getRoles().toString().equals("ADM")) {
            return "redirect:/admin/dashboard";
        }
        if (logueado.getRoles().toString().equals("EMP")) {
            return "redirect:/panel-empresa";
        }
        if (logueado.getRoles().toString().equals("BLO")) {
            return "redirect:/";
        }

        return "redirect:/panel-votante";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADM','ROLE_VOT','ROLE_EMP')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Usuario usuarioActualizado = usuarioServicio.getOne(usuario.getId());
        modelo.put("usuario", usuario);
        modelo.addAttribute("usuarioActualizado", usuarioActualizado);
        return "perfil_usuario.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADM','ROLE_VOT','ROLE_EMP')")
    @PostMapping("/perfil/{id}")
    public String actualizar(MultipartFile archivo, @PathVariable String id, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, ModelMap modelo) {

        try {
            usuarioServicio.ModificarUsuario(archivo, id, email, password, password2);
            modelo.put("exito", "Datos actualizados correctamente.");
            Usuario usuario = usuarioServicio.getOne(id);
            modelo.addAttribute("usuarioActualizado", usuario);
            return "perfil_usuario.html";
        } catch (Exception e) {
            Usuario usuario = usuarioServicio.getOne(id);
            modelo.addAttribute("usuarioActualizado", usuario);
            modelo.put("error", e.getMessage());
            modelo.put("email", email);
            return "perfil_usuario.html";
        }
    }

    //Panel votante
    @PreAuthorize("hasAnyRole('ROLE_VOT')")
    @GetMapping("/panel-votante")
    public String panelVotante(ModelMap modelo, HttpSession session) {
        //Votante
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        Usuario usuario = usuarioServicio.getOne(logueado.getId());
        Votante votante = votanteServicio.getOne(logueado.getId());

        modelo.addAttribute("votante", votante);

        return "panel_votante.html";
    }

    //Panel empresa
    @PreAuthorize("hasAnyRole('ROLE_EMP')")
    @GetMapping("/panel-empresa")
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

}
