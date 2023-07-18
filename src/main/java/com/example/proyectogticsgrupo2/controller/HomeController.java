package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.config.SecurityConfig;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import com.example.proyectogticsgrupo2.service.*;
import com.google.zxing.WriterException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {
    final PacienteRepository pacienteRepository;
    final DoctorRepository doctorRepository;
    final AdministradorRepository administradorRepository;
    final CredencialesRepository credencialesRepository;
    final DistritoRepository distritoRepository;
    final SeguroRepository seguroRepository;
    final TemporalRepository temporalRepository;
    final AlergiaRepository alergiaRepository;
    final TareaRepository tareaRepository;
    final TokenRepository tokenRepository;
    final PacientePorConsentimientoRepository ppcRepository;
     final SecurityConfig securityConfig;


    public HomeController(PacienteRepository pacienteRepository, DoctorRepository doctorRepository, AdministradorRepository administradorRepository, CredencialesRepository credencialesRepository, DistritoRepository distritoRepository, SeguroRepository seguroRepository, TemporalRepository temporalRepository, AlergiaRepository alergiaRepository, TareaRepository tareaRepository, TokenRepository tokenRepository, PacientePorConsentimientoRepository ppcRepository, SecurityConfig securityConfig) {
        this.pacienteRepository = pacienteRepository;
        this.doctorRepository = doctorRepository;
        this.administradorRepository = administradorRepository;
        this.credencialesRepository = credencialesRepository;
        this.distritoRepository = distritoRepository;
        this.seguroRepository = seguroRepository;
        this.temporalRepository = temporalRepository;
        this.alergiaRepository = alergiaRepository;
        this.tareaRepository = tareaRepository;
        this.tokenRepository = tokenRepository;
        this.ppcRepository = ppcRepository;
        this.securityConfig = securityConfig;
    }

    @GetMapping("/")
    public String principal(Model model, HttpServletRequest request){
        String server = request.getServerName();
        String link = "http://"+server+":"+request.getLocalPort()+"/signin";

        byte[] image = new byte[0];
        try {

            // Generate and Return Qr Code in Byte Array
            image = QRCodeGenerator.getQRCodeImage(link,300,300);

            // Generate and Save Qr Code Image in static/image folder
            //QRCodeGenerator.generateQRCodeImage(link,250,250,QR_CODE_IMAGE_PATH);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        // Convert Byte Array into Base64 Encode String
        String qrcode = Base64.getEncoder().encodeToString(image);

        model.addAttribute("qrcode",qrcode);
        return "general/home";
    }
    @GetMapping("/invitacion")
    public String invitacion (){return "administrador/invitar";}
    @GetMapping("/sedes")
    public String mostrarSedes(){
        return "general/sedes";
    }
    @GetMapping("/login")
    public String iniciarSesion(){
        return "general/login";
    }
    @GetMapping("/login/olvidepassw")
    public String pedirCorreo(){
        return "general/pedirCorreo";
    }
    @PostMapping("/login/olvidepassw/correo")
    public String enviarCorreo(@RequestParam(name = "correo") String correo,
                               HttpServletRequest request){
        List<Paciente> lista = pacienteRepository.findAll();
        boolean validar = false;
        for(Paciente p: lista){
            if (p.getCorreo().equals(correo)){
                validar= true;
            }
        }
        if (validar){
            TokenService tokenService = new TokenService();

            Paciente paciente = pacienteRepository.findByCorreo(correo);
            Token token = new Token();
            token.setIdPaciente(paciente.getIdPaciente());
            token.setToken(tokenService.generateToken());
            token.setFechaExpiracion(LocalDateTime.now().plusDays(2));
            token.setTabla(3);
            tokenRepository.save(token);
            CorreoPacientePassw correoPacienteService = new CorreoPacientePassw();
            String link = request.getServerName()+":"+request.getLocalPort();
            correoPacienteService.enviarCorreo(paciente.getCorreo(),paciente.getNombre(), link, token.getIdPaciente(), token.getToken());

            return "redirect:/login/olvidepassw/confirmacion";
        }
        return "/login/cambiarpassw/failed";
    }

    @GetMapping("/login/{id}/{token}")
    public String cambiarPassw(@PathVariable("id") String id,
                               @PathVariable("token") String token,
                               HttpServletRequest request){
        List<Token> tokens = tokenRepository.findAll();
        boolean validar = false;
        for (Token t: tokens){
            if (t.getToken().equals(token)){
                if (t.getIdPaciente().equals(id)){
                    validar=true;
                    break;
                }
            }
        }
        if (validar){
            return "general/cambiarPassw";
        }
        else{
            return "redirect:/login";
        }
    }

    @PostMapping("/login/cambiarPassw")
    public String cambiando (@RequestParam("password") String password,
                             @RequestParam("password2") String password2,
                             @RequestParam("id") String id){
        if (password2.equals(password) && password.length()>=6){
            Credenciales credenciales = credencialesRepository.buscarPorId(id);
            PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
            // Ahora puedes usar el passwordEncoder para codificar una contraseña
            String encodedPassword = passwordEncoder.encode(password);
            credenciales.setContrasena(encodedPassword);
            credencialesRepository.save(credenciales);
            return "redirect:/login/cambiarpassw/success";
        }else{
            return "general/cambiarPassw";
        }
    }
    @GetMapping("/login/olvidepassw/confirmacion")
    public String mensajeCambioContrasenia(){
        return "general/confirmacionTokenPassw";
    }
    @GetMapping("/login/cambiarpassw")
    public String cambiarContrasenia(){
        return "general/ingresarContrasenia";
    }
    @GetMapping("/login/cambiarpassw/success")
    public String cambioPasswExitoso(){
        return "general/confirmacioncontrasenia";
    }
    @GetMapping("/login/cambiarpassw/failed")
    public String cambioPasswMal(){
        return "general/CorreoInvalido2";
    }



    @GetMapping(value = {"/signin/{id}/{token}","/signin", "/signin/save"})
    public String vistaRegistro(@PathVariable (value = "id", required = false) String id,
                                @PathVariable (value = "token", required = false) String token,
                                Model model, @ModelAttribute("paciente") Paciente paciente){
        List<Distrito> list = distritoRepository.findAll();
        List<Seguro> list1 = seguroRepository.findAll();
        if(id==null && token==null){
            model.addAttribute("distritos", list);
            model.addAttribute("seguros", list1);
            return "general/registro";
        }else {
            Optional<Token> posible = tokenRepository.findByIdPacienteAndToken(id, token);
            if(posible.isPresent()){
                Token token1 = posible.get();
                if(LocalDateTime.now().isBefore(token1.getFechaExpiracion()) && temporalRepository.findByDni(id).isPresent()){
                    Temporal temporal = temporalRepository.findByDni(id).get();
                    paciente.setIdPaciente(temporal.getDni());
                    paciente.setNombre(temporal.getNombre());
                    paciente.setApellidos(temporal.getApellidos());
                    paciente.setCorreo(temporal.getCorreo());
                    model.addAttribute("distritos", list);
                    model.addAttribute("seguros", list1);
                    model.addAttribute("token", "existe");
                    return "general/registro";
                }else {
                    return "general/tokenExpirado";
                }
            }else {
                return "redirect:/signin";
            }
        }
    }

    @PostMapping("/signin/save")
    public String guardarRegistro (HttpSession session,
                                   @RequestParam (value = "radios", required = false) String radio,
                                   @RequestParam (value = "alergias", required = false) String alergias,
                                   @RequestParam (value = "consentimientos") List<Integer> ids,
                                   Model model,
                                   @ModelAttribute("paciente") @Valid Paciente paciente,
                                   BindingResult bindingResult,
                                   HttpServletRequest request){
        List<Distrito> list = distritoRepository.findAll();
        List<Seguro> list1 = seguroRepository.findAll();
        List<Paciente> pacientes = pacienteRepository.findAll();
        boolean existDni = false;
        boolean existCorreo = false;
        boolean existDni1 = false;
        boolean existCorreo1 = false;
        paciente.setCorreo(paciente.getCorreo().toLowerCase());
        for (Paciente p : pacientes) {
            if (p.getIdPaciente().equals(paciente.getIdPaciente())) {
                existDni = true;
                break;
            } else if (p.getCorreo().equals(paciente.getCorreo())) {
                existCorreo = true;
                break;
            }
        }
        List<Temporal> temp = temporalRepository.findAll();
        for (Temporal t : temp) {
            if (t.getDni().equals(paciente.getIdPaciente()) && t.getLlenado()==1) {
                existDni1 = true;
                break;
            } else if (t.getCorreo().equals(paciente.getCorreo()) && t.getLlenado()==1) {
                existCorreo1 = true;
                break;
            }
        }
        if(bindingResult.hasErrors() || (!paciente.getGenero().equals("M") && !paciente.getGenero().equals("F")) || existCorreo1 || existDni1 || existCorreo || existDni){
            if(existDni || existDni1){
                bindingResult.rejectValue("idPaciente", "errorDni", "El DNI ya se encuentra registrado.");
            }
            if(existCorreo || existCorreo1){
                bindingResult.rejectValue("correo","errorCorreo", "El correo ya se encuentra registrado.");
            }
            if(!paciente.getGenero().equals("M") && !paciente.getGenero().equals("F")){
                bindingResult.rejectValue("genero", "errorGenero", "Debe seleccionar uno de los géneros listados");
            }
            model.addAttribute("distritos", list);
            model.addAttribute("seguros", list1);
            Object token = model.getAttribute("token");
            if(token!=null){
                model.addAttribute("token", token);
            }
            return "general/registro";
        }else{
            if(paciente.getGenero().equals("M")){
                paciente.setGenero("Masculino");
            } else{
                paciente.setGenero("Femenino");
            }

            Optional<Temporal> temp1 = temporalRepository.findByDni(paciente.getIdPaciente());

            if(temp1.isPresent()){
                Temporal temporal = temp1.get();
                temporal.setDistrito(paciente.getDistrito());
                temporal.setFecha_nacimiento(paciente.getFechanacimiento());
                temporal.setLlenado(1);
                temporal.setSeguro(paciente.getSeguro());
                temporal.setTelefono(paciente.getTelefono());
                temporal.setDireccion(paciente.getDireccion());
                temporal.setGenero(paciente.getGenero());
                temporalRepository.save(temporal);
                tokenRepository.deleteById(temporal.getDni());
            }else {
                paciente.setEstado(3);
                paciente.setFecharegistro(LocalDateTime.now());
                pacienteRepository.save(paciente);
                Tarea tarea = new Tarea();
                tarea.setFecha(LocalDateTime.now());
                tarea.setPaciente(paciente);
                tarea.setEstado(1);
                tarea.setTipo(1);
                tareaRepository.save(tarea);
                if(radio.equals("1")){
                    String[] alergiasArray = alergias.split(",");
                    String alergia;
                    Alergia alergia1;
                    for (String s : alergiasArray) {
                        alergia = s.trim();
                        alergia = alergia.replaceAll(" +", " ");
                        if (!alergia.equals(" ") && !alergia.equals("")) {
                            alergia = alergia.substring(0, 1).toUpperCase() + alergia.substring(1).toLowerCase();
                            alergia1 = new Alergia();
                            alergia1.setNombre(alergia);
                            alergia1.setPaciente(paciente);
                            alergiaRepository.save(alergia1);
                        }
                    }
                }
                ppcRepository.cargarConsentimentos(paciente.getIdPaciente(),1,1);
                ppcRepository.cargarConsentimentos(paciente.getIdPaciente(),2,1);
                ppcRepository.cargarConsentimentos(paciente.getIdPaciente(),3,1);
                ppcRepository.cargarConsentimentos(paciente.getIdPaciente(),4,1);
                if (ids.contains(5)){
                    ppcRepository.cargarConsentimentos(paciente.getIdPaciente(),5,1);
                }else {
                    ppcRepository.cargarConsentimentos(paciente.getIdPaciente(),5,0);
                }
            }
            session.setAttribute("registro", paciente.getIdPaciente());
            String link = request.getServerName()+":"+request.getLocalPort();
            CorreoAutoregistro c = new CorreoAutoregistro();
            c.props(paciente.getCorreo(), paciente, link);
            return "redirect:/signin/confirmacion";
        }
    }

    @GetMapping("/signin/confirmacion")
    public String confirmacionRegistro(HttpSession session){
        String id = (String) session.getAttribute("registro");
        if(id!=null){
            session.removeAttribute("registro");
            session.invalidate();
            return "general/confirmacionregistro";
        }else {
            return "redirect:/";
        }
    }

    @GetMapping("/usuario/foto/{id}")
    public void showUsuarioImage(@PathVariable String id,
                                 HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        response.setContentType("image/png");
        Optional<Paciente> optPaciente = pacienteRepository.findById(id);
        Optional<Doctor> optDoctor = doctorRepository.findById(id);

        if(optPaciente.isPresent()){
            Paciente paciente = optPaciente.get();
            InputStream is = new ByteArrayInputStream(paciente.getFoto());
            IOUtils.copy(is, response.getOutputStream());
        } else if (optDoctor.isPresent()) {
            Doctor doctor = optDoctor.get();
            InputStream is = new ByteArrayInputStream(doctor.getFoto());
            IOUtils.copy(is, response.getOutputStream());
        }
    }

    @PostMapping("/newToken")
    public String generarToken (@RequestParam(name = "correo") String correo,
                               HttpServletRequest request){
        List<Temporal> temporals = temporalRepository.findAll();
        boolean validar = false;
        for (Temporal t: temporals){
            if (correo.equals(t.getCorreo())){
                validar = true;
            }
        }
        if (validar){
            TokenService tokenService = new TokenService();
            Temporal temporal = temporalRepository.findByCorreo(correo);

            Optional<Token> token1 = tokenRepository.findById(temporal.getDni());
            if (token1.isPresent()){
                Token token = token1.get();
                token.setIdPaciente(temporal.getDni());
                token.setToken(tokenService.generateToken());
                token.setFechaExpiracion(LocalDateTime.now().plusDays(2));
                token.setTabla(1);
                tokenRepository.save(token);

                CorreoPacienteService correoPacienteService = new CorreoPacienteService();
                String link = request.getServerName()+":"+request.getLocalPort();
                correoPacienteService.enviarCorreo(temporal.getCorreo(),temporal.getNombre(), link, token.getIdPaciente(), token.getToken());
                return "/general/confirmacionToken";
            }
            return "general/CorreoInvalido";
        }
        else {
            return "general/CorreoInvalido";
        }
    }
}
