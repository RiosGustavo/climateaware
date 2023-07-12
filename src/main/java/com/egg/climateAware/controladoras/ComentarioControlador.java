package com.egg.climateAware.controladoras;

import com.egg.climateAware.entidades.Usuario;
import com.egg.climateAware.servicios.ComentarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/comentario")
public class ComentarioControlador {

    @Autowired
    private ComentarioServicio comentarioServicio;

    @PostMapping("/{idCampana}/comentar")
    public String comentar(@PathVariable String idCampana, @RequestParam() String mensaje, ModelMap modelo, HttpSession session) throws Exception{

        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");

            comentarioServicio.crearComentario(idCampana, logueado.getId(), mensaje);

        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
        }
        return "redirect:/campana/campana_one/{idCampana}#comentarios";
    }

    @GetMapping("/{idCampana}/baja/{idComentario}")
    public String bajaComentario(@PathVariable String idComentario,@PathVariable String idCampana) throws Exception{
    
        comentarioServicio.darDeBajaComentario(idComentario);
    
        return "redirect:/campana/campana_one/{idCampana}#comentarios";
    }
   
    
}
