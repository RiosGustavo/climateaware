/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.climateAware.controladoras;

import com.egg.climateAware.servicios.VotanteServicio;
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
@RequestMapping("/votante")
public class VotanteControlador {

    @Autowired
    VotanteServicio votanteServicio = new VotanteServicio();
    @GetMapping("/modificarPerfil/{id}")
    public String modificiarPerfil(@RequestParam @PathVariable String id, ModelMap modelo) {
       modelo.put("votante", votanteServicio.getOne(id));
        return "modificar_votante.html";
    }

    @PostMapping("/modificacionPerfil/{id}")
    public String modificacionPerfil(@RequestParam(required = false)MultipartFile archivo,@RequestParam(required = false) String idVotante, 
            @RequestParam(required = false)String nombreApellido,@RequestParam(required = false)String dni, 
            @RequestParam(required = false)Integer voto, @RequestParam(required = false)String direccion,
            @RequestParam(required = false)String email, @RequestParam(required = false)String password, 
            @RequestParam(required = false)String password2,@RequestParam(required = false)String idPublicacion, ModelMap modelo) {
        
        try {
            votanteServicio.modificarVotante(archivo, idVotante, nombreApellido, dni, voto, direccion, email, password, password2, idPublicacion);
            modelo.put("exito", "El usuario se modifico exitosamente!");
            return "index.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
           modelo.put("votante", votanteServicio.getOne(idVotante));
            return "modificacion_votante.html";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@RequestParam @PathVariable String id)throws Exception{
        votanteServicio.bajaVotante(id);
        return "index.html";
    }
}


