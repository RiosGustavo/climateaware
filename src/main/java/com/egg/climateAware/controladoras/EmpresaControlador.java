package com.egg.climateAware.controladoras;

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

    
    
    @GetMapping("/listado")
    public String listar(ModelMap modelo) {
        List<Empresa> empresas = empresaServicio.listarEmpresas();
        modelo.addAttribute("empresas", empresas);
        return "empresa_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        modelo.put("empresa", empresaServicio.getOne(id));
        return "empresa_modificar.html";

    }

    @PostMapping("/modificar/{id}")
    public String modificar(MultipartFile archivo, @PathVariable String id, String nombreEmpresa, String cuit,
            String direccion, String rubro, String email, String password, String password2, ModelMap modelo) {

        try {
            empresaServicio.actualizarEmpresa(archivo, id, nombreEmpresa, cuit, direccion, rubro, email, password, password2);

            return "redirect .../lista";

        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            return "empresa_modificar.html";
        }
    }

    //Dar de baja una empresa
    @GetMapping("/baja/{id}")
    public String darDeBajaEmpresa(@PathVariable String id) {

        try {
            empresaServicio.darDeBajaEmpresa(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:../listado";
    }

    //Dar de alta una empresa
    @GetMapping("/alta/{id}")
    public String darDeAlta(@PathVariable String id) {
         try {
            empresaServicio.darDeAltaEmpresa(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:../listado";
        

        
    }

}
