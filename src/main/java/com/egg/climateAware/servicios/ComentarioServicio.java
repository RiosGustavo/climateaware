
package com.egg.climateAware.servicios;

import com.egg.climateAware.entidades.Campana;
import com.egg.climateAware.entidades.Comentario;
import com.egg.climateAware.entidades.Usuario;
import com.egg.climateAware.entidades.Votante;
import com.egg.climateAware.repositorios.CampanaRepositorio;
import com.egg.climateAware.repositorios.ComentarioRepositorio;
import com.egg.climateAware.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComentarioServicio {
    
    @Autowired
    private ComentarioRepositorio comentarioRepositorio;
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private CampanaRepositorio campanaRepositorio;
    
    @Transactional
    public void crearComentario(String idCampana, String idVotante,String mensaje) throws Exception{
        
        
        
        if(mensaje == null || mensaje.isEmpty()){
        
            throw new Exception("¡No podes hacer un comentario vacío!");
        }
    
        Optional<Usuario> respuestaUsuario = usuarioRepositorio.findById(idVotante);
        Optional<Campana> respuestaCampana = campanaRepositorio.findById(idCampana);
       
        Comentario comentario = new Comentario();
        
        comentario.setAltaBaja(true);
        comentario.setMensaje(mensaje);
        comentario.setFechaPublicacion(new Date());
        
        if (respuestaUsuario.isPresent()) {
            Usuario usuario = respuestaUsuario.get();
            if (usuario.getRoles().toString().equalsIgnoreCase("VOT")) {
                Votante vo = (Votante) respuestaUsuario.get();
                comentario.setVotante(vo);
            }
        }
         if (respuestaCampana.isPresent()) {
            comentario.setCampana(respuestaCampana.get());
        }
       comentarioRepositorio.save(comentario);
    }
    
    
    public List<Comentario> comentariosPorCampana(String idCampana){
    
        List<Comentario> comentarios = new ArrayList();
    
        comentarios = comentarioRepositorio.comentariosPorCampana(idCampana);
        
        return comentarios;
    }
    public List<Comentario> comentariosPorVotante(String id){
    
        List<Comentario> comentarios = new ArrayList();
    
        comentarios = comentarioRepositorio.comentariosPorVotante(id);
        
        return comentarios;
    }
    
    @Transactional
    public void darDeBajaComentario(String idComentario) throws Exception {

        Optional<Comentario> respuesta = comentarioRepositorio.findById(idComentario);

        if (respuesta.isPresent()) {
            Comentario comentario = respuesta.get();
            comentario.setAltaBaja(false);
            comentarioRepositorio.save(comentario);
        }
    }
    
}
