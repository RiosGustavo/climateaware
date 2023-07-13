package com.egg.climateAware.controladoras;

import com.egg.climateAware.entidades.Noticia;
import com.egg.climateAware.entidades.Usuario;
import com.egg.climateAware.repositorios.NoticiaRepositorio;
import com.egg.climateAware.servicios.NoticiaServicio;
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

@Controller
@RequestMapping("/noticia")
public class NoticiaControlador {

    @Autowired
    NoticiaServicio noticiaServicio = new NoticiaServicio();
    @Autowired
    private NoticiaRepositorio noticiaRepositorio;

    @GetMapping("/noticia_one/{idNoticia}")
    public String id(@PathVariable String idNoticia, ModelMap modelo) {
        modelo.put("noticia", noticiaServicio.getOne(idNoticia));
        return "noticia_one.html";
    }

    @GetMapping("/lista")
    public String listadoCampanas(@RequestParam(required = false) String termino, ModelMap modelo, HttpSession session) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        List<Noticia> noticias = new ArrayList<>();

        if (termino != null && !termino.isEmpty()) {
            noticias = noticiaServicio.buscarNoticiasPorTitulo(termino.toLowerCase());

            if (noticias.isEmpty()) {
                noticias = noticiaRepositorio.findAllOrderByfecha_altaDesc();
                modelo.put("error", "No se encontró nada con el término ingresado. Intente de otra manera.");
            }

        } else {
            noticias = noticiaRepositorio.findAllOrderByfecha_altaDesc();
        }

        modelo.addAttribute("noticias", noticias);

        return "noticia_list.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_BLO')")
    @GetMapping("/crear")
    public String crear(HttpSession session) {
        if (session.getAttribute("usuariosession") == null || !((Usuario) session.getAttribute("usuariosession")).getAltaBaja()) {
            return "redirect:/blogger/panel-principal";
        }
        return "noticia_form.html";
    }

    @PostMapping("/creacion")
    public String creacion(@RequestParam() String titulo, @RequestParam() String descripcion, @RequestParam String cuerpo, @RequestParam MultipartFile archivo,
            @RequestParam() String video, ModelMap modelo, HttpSession session) {
        System.out.println("primer error");
        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            noticiaServicio.crearNoticia(archivo, titulo, descripcion, cuerpo, video, logueado.getId());
            System.out.println("llego al try");
            modelo.put("exito", "Noticia creada exitosamente!");
        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
            modelo.addAttribute("titulo", titulo);
            modelo.addAttribute("descripcion", descripcion);
            modelo.addAttribute("cuerpo", cuerpo);
            modelo.addAttribute("video", video);

        }
        return "noticia_form.html";
    }

    @GetMapping("/modificar/{idNoticia}")
    public String modificar(@PathVariable String idNoticia, ModelMap modelo) {
        modelo.put("noticia", noticiaServicio.getOne(idNoticia));
        System.out.println("llego hasta aca el error");
        return "noticia_modificar.html"; //hasta definir el nombre del html
    }

    @PostMapping("/modificar/{idNoticia}")
    public String modificacion(@PathVariable String idNoticia, MultipartFile archivo,
            String titulo, String descripcion, String cuerpo, String video, ModelMap modelo) {
        System.out.println("llego al try del post");
        try {
            noticiaServicio.modificarNoticia(archivo, idNoticia, titulo, descripcion, cuerpo, video);
            modelo.put("exito", "La noticia se ha modificado exitosamente!");
            System.out.println("llego al try antes de return");
            return "redirect:/noticia/lista";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("noticia", noticiaServicio.getOne(idNoticia));
            System.out.println("llego al catch del post");
            return "noticia_modificar.html";
        }

    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@RequestParam @PathVariable String id) throws Exception {
        noticiaServicio.bajaNoticia(id);
        return "index.html";
    }

}
