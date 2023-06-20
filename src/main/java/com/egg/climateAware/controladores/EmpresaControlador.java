package com.egg.climateAware.controladores;

import com.egg.climateAware.entidades.Empresa;
import com.egg.climateAware.servicios.EmpresaServicio;
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
@RequestMapping("/empresa")
public class EmpresaControlador {

    @Autowired
    private EmpresaServicio empresaServicio;

    @GetMapping("/registrar")
    public String registrar() {
        return "empresa_form.html"; /// ACA CAMBIAR POR COMO LE PUSIERON LOS CHICOS
        /// A ESTE ARCHIVO DE HTML PARA REGISTRAR LAS EMPRESAS
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombreEmpresa, MultipartFile archivo, @RequestParam String cuit,
            @RequestParam String direccion, @RequestParam String rubro, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, ModelMap modelo) {
        try {
            empresaServicio.registrarEmpresa(archivo, nombreEmpresa, cuit, direccion, rubro, email, password, password2, email);
            modelo.put("exito", "La Empresa ha sido registrada Exitosamente");

            ///// REVIZAR ACA COMO PUSIERON LOS CHICOS A LA CLASE EXCEPCION
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombreEmpresa", nombreEmpresa);
            return "empresa_form.html";  //// PONER ACA EL NOMBRE DEL ARCHIVO HTML QUE LOS CHICOS HICIERON PARA REGISTRAR LAS EMPRESAS
        }
        return "index.html"; ////PREGUNTAR SI LE PONEMOS QUE DESPUES DE REGISTRASE SE DEVUELVA AL INDEX
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Empresa> empresas = empresaServicio.listarEmpresas();
        modelo.addAttribute("empresas", empresas);
        return "empresa_list.html"; //// PONER ACA EL NOMBRE DEL ARCHIVO HTML QUE LOS CHICOS HICIERON PARA LISTAR LAS EMPRESAS
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        modelo.put("empresa", empresaServicio.getOne(id));
        return "empresa_modificar.html"; //// PONER ACA EL NOMBRE DEL ARCHIVO HTML QUE LOS CHICOS HICIERON PARA MODIFICAR LAS EMPRESAS

    }

    @PostMapping("/modificar/{id}")
    public String modificar(MultipartFile archivo, @PathVariable String id, String nombreEmpresa, String cuit,
            String direccion, String rubro, String email, String password, String password2, ModelMap modelo) {

        try {
            empresaServicio.actualizarEmpresa(archivo, id, nombreEmpresa, cuit, direccion, rubro, email, password, password2, email);

            return "redirect .../lista";

            ///// REVIZAR ACA COMO PUSIERON LOS CHICOS A LA CLASE EXCEPCION
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "empresa_modificar.html";  //// PONER ACA EL NOMBRE DEL ARCHIVO HTML QUE LOS CHICOS HICIERON PARA MODIFICAR LAS EMPRESAS
        }
    }

    @GetMapping("/eliminar/{id}")
    public String darDeBajaEmpresa(@PathVariable String id) {
        empresaServicio.darDeBajaEmpresa(id);
        
        return"redirec.../lista";

    }

}