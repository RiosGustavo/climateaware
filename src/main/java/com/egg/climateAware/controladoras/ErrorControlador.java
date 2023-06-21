/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.climateAware.controladoras;

import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author rompe
 */
@Controller
public class ErrorControlador implements ErrorController {
    
    @RequestMapping (value = "/error", method = { RequestMethod.GET, RequestMethod.POST  } )
     public ModelAndView renderPaginaError (HttpServletRequest httpRequest) {
         
     ModelAndView paginaError = new ModelAndView("error");    
     
     String errorMsg = "";
     
     int httpCodigoError = getCodigoError(httpRequest);
     
     switch (httpCodigoError){
         
         case 400: {
             errorMsg = "El recurso solicitado no existe.";
             break;
         }
         case 403: {
             errorMsg = "No tiene permisos para acceder al recurso.";
             
             break;
             }
         case 401: {
             errorMsg = "No se encuentra autorizado.";
             break;
         }
         case 404: {
             errorMsg = "El recurso solicitado no fue encontrado.";
             break;
         }
         case 500: {
             errorMsg = "Se ha producido un error interno.";
             break;
         }
         
     }
     
     paginaError.addObject ("codigo", httpCodigoError);
     paginaError.addObject ("mensaje", errorMsg);
     return paginaError;
         
     }
     
     private int getCodigoError (HttpServletRequest httpRequest){
         return (Integer) httpRequest.getAttribute ("javax.servlet.error.status_code");
         
     }
     
     public String getErrorPath(){
         return "/error.html";
     }
    
    
    
    
}
