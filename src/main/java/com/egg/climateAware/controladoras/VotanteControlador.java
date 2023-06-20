/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.climateAware.controladoras;

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
//    VotanteServicio votanteServicio = new VotanteServicio();

    @GetMapping("/registrar")
    public String registrar() {
        return "votante_form.html";
    }

    @PostMapping("/registro")
    public String registro(MultipartFile archivo, String mail, String nombreApellido, String dni, String contrasena, String contrasena2, ModelMap modelo) {
        try {
//            votanteServicio.crearVotante(MultipartFile archivo, String nombreApellido, String dni, String direccion, String email, String password, String password2);
            modelo.put("exito", "La Empresa ha sido registrada Exitosamente");

        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("mail", mail);
            modelo.put("nombreAprellido", nombreApellido);
            modelo.put("dni", dni);

            return "votante_form.html";
        }
        return "index.html";
    }

    @GetMapping("/modificarPerfil/{id}")
    public String modificiarPerfil(@PathVariable String id, ModelMap modelo) {
//        modelo.put("votante", votanteRepositorio.getOne(id));
        return "modificar_votante.html";
    }

    @PostMapping("/modificacionPerfil/{id}")
    public String modificacionPerfil(@RequestParam String nombreUsuario, @RequestParam String password,
            String password2, @PathVariable String id, ModelMap modelo) {
        try {
//            votanteServicio.modificarVotante(MultipartFile archivo, String idVotante, String nombreApellido, String dni, Integer voto, String direccion, String email, String password, String password2, String idPublicacion)<
            modelo.put("exito", "El usuario se modifico exitosamente!");
            return "index.html";
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
//            modelo.put("votante", votanteRepositorio.getOne(id);
            return "modificacion_votante.html";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id){
//        votanteServicio.bajaVotante(id);
        return "index.html";
    }
}


