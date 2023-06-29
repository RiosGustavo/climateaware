
package com.egg.climateAware.controladoras;

import com.egg.climateAware.entidades.Noticia;
import com.egg.climateAware.servicios.NoticiaServicio;
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
@RequestMapping("/noticia")
public class NoticiaControlador {
    @Autowired
    NoticiaServicio noticiaServicio = new NoticiaServicio();

    @GetMapping("/{id}")
    public String id(@RequestParam @PathVariable String id, ModelMap modelo){
        modelo.put("noticia",noticiaServicio.getOne(id));
        return "noticia_one.html";
    }
    
    @GetMapping("/crear")
    public String crear() {
        return "noticia_form.html";
    }
    
    
   @PostMapping("creacion")
    public String creacion(@RequestParam() String titulo,@RequestParam() String descripcion,@RequestParam(required = false) String cuerpo,@RequestParam(required = false) MultipartFile archivo,
            @RequestParam(required = false) String video, ModelMap modelo) {
        try {
            // public void crearPublicacion(MultipartFile archivo, String titulo, String descripcion, String cuerpo, String video)
        noticiaServicio.crearNoticia(archivo, titulo, descripcion, cuerpo, video);
            modelo.put("exito", "Noticia creada exitosamente!");
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("titulo", titulo);
            modelo.put("descripcion", descripcion);
            modelo.put("cuerpo", cuerpo);
            modelo.put("archivo", archivo);
            modelo.put("video", video);
            return "noticia_form.html";
        }
        return "noticia.html ? redirect:/noticia/id ? redirect:/noticia/id ?"; // aca puede haber error.. casi seguro...
    }
    
    
    @GetMapping("/modificar/{id}")
    public String modificar(@RequestParam String id, ModelMap modelo) {
         modelo.put("noticia",noticiaServicio.getOne(id));
        return "noticia_modiificar.html"; //hasta definir el nombre del html
    }

    @PostMapping("/modificacion/{id}")
    public String modificacion(@RequestParam @PathVariable String id,@RequestParam(required = false) MultipartFile archivo,
           @RequestParam() String titulo,@RequestParam() String descripcion, @RequestParam(required = false) String cuerpo, @RequestParam(required = false) String video, ModelMap modelo) {
        try {
               noticiaServicio.modificarNoticia(archivo, id, titulo,descripcion,cuerpo, video);
            modelo.put("exito", "La noticia se ha modificado exitosamente!");
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
           modelo.put("noticia",noticiaServicio.getOne(id));
            return "modificar.html";
        }
        return "redirect:/noticia/id";
    }

   @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Noticia> noticias = noticiaServicio.listarNoticias();
        
        modelo.addAttribute("noticias", noticias);
        return "noticias_list.html"; 
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@RequestParam @PathVariable String id) throws Exception{
       noticiaServicio.bajaNoticia(id);
        return "index.html";
    }
    
    @GetMapping("/listar")
    public String listar() throws Exception{
       noticiaServicio.listarNoticias();
       
       return "noticia_list.html";
    }

}
