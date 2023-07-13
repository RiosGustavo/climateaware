package com.egg.climateAware.controladoras;

import com.egg.climateAware.Utility;
import com.egg.climateAware.entidades.Campana;
import com.egg.climateAware.entidades.Empresa;
import com.egg.climateAware.entidades.Noticia;
import com.egg.climateAware.entidades.Publicacion;
import com.egg.climateAware.entidades.Usuario;
import com.egg.climateAware.entidades.Votante;
import com.egg.climateAware.servicios.CampanaServicio;
import com.egg.climateAware.servicios.EmpresaServicio;
import com.egg.climateAware.servicios.NoticiaServicio;
import com.egg.climateAware.servicios.PublicacionServicio;
import com.egg.climateAware.servicios.UsuarioServicio;
import com.egg.climateAware.servicios.VotanteServicio;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private NoticiaServicio noticiaServicio;

    @Autowired
    private VotanteServicio votanteServicio;

    @Autowired
    private EmpresaServicio empresaServicio;

    @Autowired
    private CampanaServicio campanaServicio;

    @Autowired
    private PublicacionServicio publicacionServicio;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/")
    public String index(ModelMap modelo, HttpSession session) {

        List<Noticia> noticias = noticiaServicio.listarNoticias();
        modelo.addAttribute("noticias", noticias);

        List<Campana> campanas = campanaServicio.listarCampanas();
        modelo.addAttribute("campanas", campanas);

        List<Publicacion> publicacion = publicacionServicio.listarPublicaciones();
        modelo.addAttribute("publicaciones", publicacion);

        return "index.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            modelo.put("error", "Usuario o contraseña inválidos");
        }
        return "login.html";
    }

    @GetMapping("/registrar")
    public String registrar() {

        return "register.html";
    }

    @PostMapping("/registro")
    public String registro(MultipartFile archivo, @RequestParam String email, @RequestParam String password, @RequestParam String password2, @RequestParam String rol,
            @RequestParam(required = false) String nombreApellido, @RequestParam(required = false) String dni, @RequestParam(required = false) String direccionUsuario,
            @RequestParam(required = false) String nombreEmpresa, @RequestParam(required = false) String cuit, @RequestParam(required = false) String direccionEmpresa,
            @RequestParam(required = false) String rubro, ModelMap modelo) {
        try {

            if (rol.equalsIgnoreCase("votante")) {

                votanteServicio.crearVotante(archivo, nombreApellido, dni, direccionUsuario, email, password, password2);

            } else if (rol.equalsIgnoreCase("empresa")) {

                empresaServicio.registrarEmpresa(archivo, nombreEmpresa, cuit, direccionEmpresa, rubro, email, password, password2);
            } else {

                usuarioServicio.crearUsuario(archivo, email, password, password2);
            }

            modelo.put("exito", "Usuario registrado correctamente");
            return "register.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            modelo.put("email", email);

            return "register.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADM','ROLE_EMP','ROLE_VOT','ROLE_BLO')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado.getRoles().toString().equals("ADM")) {
            return "redirect:/admin/dashboard";
        }
        if (logueado.getRoles().toString().equals("EMP")) {
            return "redirect:empresa/panel-principal";
        }
        if (logueado.getRoles().toString().equals("BLO")) {
            return "redirect:/blogger/panel-principal";
        }
        if (logueado.getRoles().toString().equals("VOT")) {
            String votanteUrl = String.format("redirect:/votante/%s", logueado.getId());
            return votanteUrl;
        }
        return "index.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADM','ROLE_VOT','ROLE_EMP')")
    @GetMapping("/perfil/changePassword")
    public String perfil(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Usuario usuarioActualizado = usuarioServicio.getOne(usuario.getId());
        modelo.put("usuario", usuario);
        modelo.addAttribute("usuarioActualizado", usuarioActualizado);
        return "change_password.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADM','ROLE_VOT','ROLE_EMP')")
    @PostMapping("/perfil/changePassword")
    public String perfil(@RequestParam String claveActual, @RequestParam String id, @RequestParam String clave,
            @RequestParam String clave2, ModelMap model) {
        try {
            usuarioServicio.cambiarClave(claveActual, id, clave, clave2);
            model.put("exito", "La contraseña ha sido actualizada correctamente.");
            return "change_password.html";
        } catch (Exception e) {
            model.put("error", e.getMessage());
            return "change_password.html";
        }
    }

    @GetMapping("/forgot_password")
    public String recuperarContraseñaForm(Model modelo) {

        return "forgot_password_form.html";

    }

    @PostMapping("/forgot_password")
    public String forgot_password(@RequestParam String email, ModelMap model, HttpServletRequest request) throws Exception {

        String token = RandomString.make(45);

        try {
            usuarioServicio.updateResetPasswordToken(token, email);

            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;

            sendEmail(email, resetPasswordLink);

            model.put("exito", "Ya te enviamos a tu mail las instrucciones para recuperar tu contraseña.");
        } catch (Exception e) {
            model.put("error", e.getMessage());
        }

        return "forgot_password_form.html";
    }

    private void sendEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@climateaware.com", "Climate Aware Support");
        helper.setTo(email);

        String subject = "Aquí está el link para restablecer tu contraseña";
        String content = "<p>Hola,</p>"
                + "<p>Solicitaste restablecer tu contraseña.</p>"
                + "<p>Haz click en el link de abajo para cambiar tu contraseña:</p>"
                + "<p><b><a href=\"" + resetPasswordLink + "\">Restablecer mi contraseña</a><b></p>"
                + "<p>Ignora este email si recordaste tu contraseña o no solicitaste restablecerla.</p>";

        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String resetPasswordForm(@Param(value = "token") String token, Model modelo) {

        Usuario usuario = usuarioServicio.obtenrUsuarioPorToken(token);
        modelo.addAttribute("token", token);
        return "reset_password_form.html";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, ModelMap model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        Usuario usuario = usuarioServicio.obtenrUsuarioPorToken(token);
        model.addAttribute("titulo", "Restablecer contraseña");

        if (usuario == null) {
            model.put("error", "Token inválido");
            return "mensaje.html";
        } else {
            usuarioServicio.updatePassword(usuario, password);
            model.put("exito", "¡Tu contraseña fue cambiada exitosamente!");
        }
        return "mensaje.html";
    }
}
