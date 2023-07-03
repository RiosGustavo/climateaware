
package com.egg.climateAware.controladoras;

import com.egg.climateAware.entidades.Campana;
import com.egg.climateAware.entidades.Publicacion;
import com.egg.climateAware.entidades.Usuario;
import com.egg.climateAware.servicios.CampanaServicio;
import com.egg.climateAware.servicios.PublicacionServicio;
import com.egg.climateAware.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/imagen")
public class ImagenControladora {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
   
    @Autowired
    private CampanaServicio campanaServicio;
    
    @Autowired
    private PublicacionServicio publicacionServicio;
    
    
    //Este metodo nos devuelve la imagen en arreglo de bytes, para poder visualizarla
    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id){
        Usuario usuario = usuarioServicio.getOne(id);
        
        byte[] imagen = usuario.getImagen().getContenido();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); //Indica el formato en que se va a recibir la imagen
        
        
        
        return new ResponseEntity<>(imagen,headers,HttpStatus.OK);
    }
    
    
    
    @GetMapping("/campana/{id}")
    public ResponseEntity<byte[]> imagenCampana(@PathVariable String id){
        Campana campana = campanaServicio.getOne(id);
        
        byte[] imagen = campana.getImagen().getContenido();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); 
       
        return new ResponseEntity<>(imagen,headers,HttpStatus.OK);
    }
    
     
    @GetMapping("/publicacion/{id}")
    public ResponseEntity<byte[]> imagenPublicacion(@PathVariable String id){
        Publicacion publicacion = publicacionServicio.getOne(id);
        
        byte[] imagen = publicacion.getImagen().getContenido();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); 
       
        return new ResponseEntity<>(imagen,headers,HttpStatus.OK);
    }
}
