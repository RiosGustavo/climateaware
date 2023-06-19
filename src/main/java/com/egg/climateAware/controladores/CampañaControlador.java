package com.egg.climateAware.controladores;

import com.egg.climateAware.entidades.Empresa;
import com.egg.climateAware.servicios.CampañaServicio;
import com.egg.climateAware.servicios.EmpresaServicio;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/campaña")
public class CampañaControlador {

    @Autowired
    private CampañaServicio campañaServicio;

    @Autowired
    private EmpresaServicio empresaServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) {
        List<Empresa> empresas = empresaServicio.listarEmpresas();
        modelo.addAttribute("empresas", empresas);
        return "campaña_form.html"; //// PONER ACA EL NOMBRE DEL ARCHIVO HTML QUE LOS CHICOS HICIERON PARA CREAR LAS CAMPAÑAS
    }

    @PostMapping("/registro")
    public String registro(MultipartFile archivo, @RequestParam(required = false) String titulo, @RequestParam String cuerpo,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaAlta, @RequestParam String idPublicacion,
            @RequestParam String id, ModelMap modelo) throws MiExcepcion {//// PONER ACA COMO LOS CHICOS LE PUSIERON A LA EXCEPCION
        //(MultipartFile archivo, String titulo, String cuerpo, Date fechaAlta, String idPublicacion, String id)
        try {
            campañaServicio.crearCampaña(archivo, titulo, cuerpo, fechaAlta, idPublicacion, id);

            modelo.put("exito", "Campaña Creada Exitosamente");

            ///// REVIZAR ACA COMO PUSIERON LOS CHICOS A LA CLASE EXCEPCION
        } catch (Exception ex) {
            List<Empresa> empresas = empresaServicio.listarEmpresas();
            modelo.addAttribute("empresas", empresas);

            modelo.put("error", ex.getMessage());

            return "campaña_form.html";  //// PONER ACA EL NOMBRE DEL ARCHIVO HTML QUE LOS CHICOS HICIERON PARA CREAR LAS CAMPAÑAS

        }
        return "index.html"; ////PREGUNTAR SI LE PONEMOS QUE DESPUES DE REGISTRASE SE DEVUELVA AL INDEX

    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Campaña> campañas = campañaServicio.listarCampañas()
        :
        modelo.addAttribute("campañas", campañas);
        return "campaña_list.html"; //// PONER ACA EL NOMBRE DEL ARCHIVO HTML QUE LOS CHICOS HICIERON PARA LISTAR LAS campañas
    }

    @GetMapping("/modificar/{idCampaña}")
    public String modificar(@PathVariable String idCampaña, ModelMap modelo) {
        modelo.put("campaña", campañaServicio.getOne(idCampaña));
        List<Empresa> empresas = empresaServicio.listarEmpresas();

        modelo.addAttribute("empresas", empresas);
        return "campaña_modificar.html"; //// PONER ACA EL NOMBRE DEL ARCHIVO HTML QUE LOS CHICOS HICIERON PARA MODIFICAR LAS campañas
    }

    @PostMapping("/modificar/{idCampaña}")
    public String modificar(MultipartFile archivo, String idCampaña, String titulo,
            String cuerpo, Date fechaAlta, String idPublicacion, String id, ModelMap modelo) {
        try {
            List<Empresa> empresas = empresaServicio.listarEmpresas();
            modelo.addAttribute("empresas", empresas);

            campañaServicio.actualizarCampaña(archivo, idCampaña, titulo, cuerpo, fechaAlta, idPublicacion, id);
            return "redirect:../lista";
        } catch (MiException ex) {
            List<Empresa> empresas = empresaServicio.listarEmpresas();
            modelo.addAttribute("empresas", empresas);
            modelo.put("error", ex.getMessage());
            return "campaña_modificar.html"; //// PONER ACA EL NOMBRE DEL ARCHIVO HTML QUE LOS CHICOS HICIERON PARA MODIFICAR LAS campañas
        }

    }
    
    
    

    @GetMapping("/eliminar/{id}")
    public String darDeBajaCamapña(@PathVariable String idCampaña) {
        campañaServicio.darDeBajaCampaña(idCampaña);

        return "redirec.../lista";

    }

}
