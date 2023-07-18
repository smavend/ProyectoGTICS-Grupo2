package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import com.example.proyectogticsgrupo2.service.CorreoPacienteService;
import com.example.proyectogticsgrupo2.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class AdministrativoController {
    final PacienteRepository pacienteRepository;
    final AdministrativoPorEspecialidadPorSedeRepository aesRepository;
    final AlergiaRepository alergiaRepository;
    final DistritoRepository distritoRepository;
    final NotificacionRepository notificacionRepository;
    final TemporalRepository temporalRepository;
    final AdministrativoRepository administrativoRepository;
    final TokenRepository tokenRepository;
    final FormularioJsonRepository formularioJsonRepository;
    final StylevistasRepository stylevistasRepository;
    final CredencialesRepository credencialesRepository;
    final SedeRepository sedeRepository;


    public AdministrativoController(PacienteRepository pacienteRepository, AdministrativoPorEspecialidadPorSedeRepository aesRepository, AlergiaRepository alergiaRepository, DistritoRepository distritoRepository, NotificacionRepository notificacionRepository, TemporalRepository temporalRepository, AdministrativoRepository administrativoRepository, TokenRepository tokenRepository, StylevistasRepository stylevistasRepository, FormularioJsonRepository formularioJsonRepository, CredencialesRepository credencialesRepository, SedeRepository sedeRepository) {
        this.pacienteRepository = pacienteRepository;
        this.aesRepository = aesRepository;
        this.alergiaRepository = alergiaRepository;
        this.distritoRepository = distritoRepository;
        this.notificacionRepository = notificacionRepository;
        this.temporalRepository = temporalRepository;
        this.administrativoRepository = administrativoRepository;
        this.tokenRepository = tokenRepository;
        this.stylevistasRepository = stylevistasRepository;
        this.formularioJsonRepository = formularioJsonRepository;
        this.credencialesRepository = credencialesRepository;
        this.sedeRepository = sedeRepository;
    }
    @GetMapping("/administrativo")
    public String dashboard(Model model, HttpSession session){
        /*Administrativo admi = (Administrativo) session.getAttribute("administrativo");*/
        String impersonatedUser = (String) session.getAttribute("impersonatedUser");

        Boolean superAdminLogueadoComoAdministrativo = (Boolean) session.getAttribute("superAdminLogueadoComoAdministrativo");
        if (superAdminLogueadoComoAdministrativo == null) {
            superAdminLogueadoComoAdministrativo = false;
        }
        model.addAttribute("superAdminLogueadoComoAdministrativo", superAdminLogueadoComoAdministrativo);

        Administrativo admi;

        if (impersonatedUser != null) {
            // Si hay un usuario "impersonado", buscar al administrativo por ese correo electrónico
            admi = administrativoRepository.findByCorreo(impersonatedUser);
        } else {
            admi = (Administrativo) session.getAttribute("administrativo");
            if (admi == null) {
                return "redirect:/error";
            }
        }
        System.out.println(admi.getIdAdministrativo());
        System.out.println(admi.getNombre());
        session.setAttribute("administrativo", admi);
        String idAdmi = admi.getIdAdministrativo();

        AdministrativoPorEspecialidadPorSede aes = aesRepository.buscarPorAdministrativoId(idAdmi);
        model.addAttribute("datos", aes);
        model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorAdministrativoYActual(idAdmi));
        model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdmi));

        List<Paciente> lista = pacienteRepository.buscarPorIdAdministrativo(idAdmi);
        List<Temporal> listaTemp = temporalRepository.findByAdministrativo_IdAdministrativo(idAdmi);

        model.addAttribute("listaPacientes", lista);
        model.addAttribute("listaTemp", listaTemp);

        Optional<Stylevistas> style = stylevistasRepository.findById(3);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorAdministrativo", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        return "administrativo/index";
    }

    //Comentado por Gustavo
/*    @GetMapping("/administrativo/invitar")
    public String vistaInvitar(HttpSession session, Model model, @ModelAttribute("temporal") Temporal temporal){

        Administrativo admi = (Administrativo) session.getAttribute("administrativo");
        String idAdmi = admi.getIdAdministrativo();

        model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdmi));
        model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdmi));
        return "administrativo/invitar";
    }*/
    @GetMapping("/administrativo/invitar")
    public String vistaInvitar(HttpSession session, Model model, @ModelAttribute("temporal") Temporal temporal, Authentication authentication){
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Administrativo admi = administrativoRepository.findByCorreo(userEmail);
        session.setAttribute("administrativo", admi);

        String idAdmi = admi.getIdAdministrativo();

        model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorAdministrativoYActual(idAdmi));
        model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdmi));
        Optional<Stylevistas> style = stylevistasRepository.findById(3);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorAdministrativo", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        return "administrativo/invitar";
    }

    @GetMapping("/administrativo/editar")
    public String vistaEditar(HttpSession session, @RequestParam(name = "id") String id,
                              Model model){
        Optional<Stylevistas> style = stylevistasRepository.findById(3);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorAdministrativo", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        Optional<Paciente> optPaciente = pacienteRepository.findById(id);
        if(optPaciente.isPresent()){
            Paciente paciente = optPaciente.get();
            if(paciente.getAdministrativo()!=null) {
                Administrativo admi = (Administrativo) session.getAttribute("administrativo");
                String idAdmi = admi.getIdAdministrativo();

                if (paciente.getAdministrativo().getIdAdministrativo().equals(idAdmi)) {
                    model.addAttribute("paciente", paciente);
                    model.addAttribute("alergias", alergiaRepository.buscarPorPacienteId(id));
                    model.addAttribute("listaDistritos", distritoRepository.findAll());

                    model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorAdministrativoYActual(idAdmi));
                    model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdmi));
                    return "administrativo/editar";
                }
            }
        }
        return "redirect:/administrativo";
    }

    @GetMapping("/administrativo/editar/invitado")
    public String vistaEditarInvitado(HttpSession session,
                                      @RequestParam(name = "id") Integer id,
                                      Model model,Authentication authentication){

        Optional<Stylevistas> style = stylevistasRepository.findById(3);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorAdministrativo", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        Optional<Temporal> optTemp = temporalRepository.findById(id);
        if(optTemp.isPresent()){
            Temporal temp = optTemp.get();
            /*          Administrativo admi = (Administrativo) session.getAttribute("administrativo");*/
            String userEmail;
            if (session.getAttribute("impersonatedUser") != null) {
                userEmail = (String) session.getAttribute("impersonatedUser");
            } else {
                userEmail = authentication.getName();
            }
            Administrativo admi = administrativoRepository.findByCorreo(userEmail);
            session.setAttribute("administrativo", admi);
            String idAdmi = admi.getIdAdministrativo();
            if(temp.getAdministrativo().getIdAdministrativo().equals(idAdmi)) {
                model.addAttribute("temporal", temp);
                model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorAdministrativoYActual(idAdmi));
                model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdmi));
                return "administrativo/editarTemp";
            }
        }
        return "redirect:/administrativo";
    }
    @GetMapping("/administrativo/mensajeria")
    public String mostrarMensajeria(HttpSession session, Model model,Authentication authentication){
        /*      Administrativo admi = (Administrativo) session.getAttribute("administrativo");*/
        Optional<Stylevistas> style = stylevistasRepository.findById(3);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorAdministrativo", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Administrativo admi = administrativoRepository.findByCorreo(userEmail);
        session.setAttribute("administrativo", admi);
        String idAdmi = admi.getIdAdministrativo();

        model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorAdministrativoYActual(idAdmi));
        model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdmi));
        return "administrativo/mensajeria";
    }

    @PostMapping("administrativo/invitar")
    public String invitarPaciente(HttpServletRequest request, HttpSession session, Model model,
                                  @ModelAttribute("temporal") @Valid Temporal temporal,
                                  BindingResult bindingResult,
                                  RedirectAttributes attr,
                                  Authentication authentication){
        Optional<Stylevistas> style = stylevistasRepository.findById(3);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorAdministrativo", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        /*      Administrativo admi = (Administrativo) session.getAttribute("administrativo");*/
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Administrativo admi = administrativoRepository.findByCorreo(userEmail);
        session.setAttribute("administrativo", admi);
        String idAdmi = admi.getIdAdministrativo();

        if(bindingResult.hasErrors()){
            model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorAdministrativoYActual(idAdmi));
            model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdmi));
            Optional<Stylevistas> style2 = stylevistasRepository.findById(3);
            if (style2.isPresent()) {
                Stylevistas styleActual = style2.get();

                model.addAttribute("headerColorAdministrativo", styleActual.getHeader());
            } else {
                // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
            }

            return "administrativo/invitar";
        }
        else{
            List<Paciente> list = pacienteRepository.findAll();
            boolean exist = false;
            boolean existTemp = false;
            for (Paciente p : list) {
                if (p.getIdPaciente().equals(temporal.getDni())) {
                    exist = true;
                    break;
                } else if (p.getCorreo().equals(temporal.getCorreo())) {
                    exist = true;
                    break;
                }
            }
            List<Temporal> temp = temporalRepository.findAll();
            for (Temporal t : temp) {
                if (t.getDni().equals(temporal.getDni())) {
                    existTemp = true;
                    break;
                } else if (t.getCorreo().equals(temporal.getCorreo())) {
                    existTemp = true;
                    break;
                }
            }
            if (!exist & !existTemp) {
                temporal.setFechainvitado(LocalDateTime.now());
                Optional<Administrativo> optAdministrativo = administrativoRepository.findById(idAdmi);
                temporal.setAdministrativo(optAdministrativo.get());
                temporalRepository.save(temporal);
                TokenService tokenService = new TokenService();

                Token token = new Token();
                token.setIdPaciente(temporal.getDni());
                token.setToken(tokenService.generateToken());
                token.setFechaExpiracion(LocalDateTime.now().plusDays(2));
                token.setTabla(1);
                tokenRepository.save(token);

                CorreoPacienteService correoPacienteService = new CorreoPacienteService();
                String link = request.getServerName()+":"+request.getLocalPort();
                correoPacienteService.enviarCorreo(temporal.getCorreo(),temporal.getNombre(), link, token.getIdPaciente(), token.getToken());
                attr.addFlashAttribute("msg","Paciente "+temporal.getNombre()+" "+temporal.getApellidos()+" invitado exitosamente");
                return "redirect:/administrativo";
            } else {
                attr.addFlashAttribute("msg", "Ingrese un DNI o correo diferente, ya se encuentra invitado o registrado");
                return "redirect:/administrativo/invitar";
            }
        }
    }

    @PostMapping("/administrativo/guardar/invitado")
    public String actualizarInvitado(HttpServletRequest request,
                                     HttpSession session,
                                     Model model,
                                     @ModelAttribute("temporal") @Valid Temporal temporal,
                                     BindingResult bindingResult,
                                     RedirectAttributes attr,
                                     Authentication authentication){
        /*      Administrativo admi = (Administrativo) session.getAttribute("administrativo");*/
        Optional<Stylevistas> style = stylevistasRepository.findById(3);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorAdministrativo", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Administrativo admi = administrativoRepository.findByCorreo(userEmail);
        session.setAttribute("administrativo", admi);
        String idAdmi = admi.getIdAdministrativo();

        List<Paciente> pacientes = pacienteRepository.findAll();
        boolean existDni = false;
        for (Paciente p : pacientes) {
            if (p.getCorreo().equals(temporal.getCorreo())) {
                existDni = true;
                break;
            }
        }
        List<Temporal> temp = temporalRepository.findAll();
        for (Temporal t : temp) {
            if (t.getCorreo().equals(temporal.getCorreo())) {
                existDni = true;
                break;
            }
        }

        if(bindingResult.hasErrors() || existDni){
            if (existDni){
                bindingResult.rejectValue("correo", "errorCorreo", "El correo ingresado ya está registrado");
            }
            model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorAdministrativoYActual(idAdmi));
            model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdmi));
            return "administrativo/editarTemp";
        }else {
            temporalRepository.actualizarInvitado(temporal.getCorreo(), temporal.getId_temporal());
            Optional<Token> tokenOpt = tokenRepository.findById(temporal.getDni());
            Token token = tokenOpt.get();
            TokenService tokenService = new TokenService();
            token.setToken(tokenService.generateToken());
            token.setFechaExpiracion(LocalDateTime.now().plusDays(2));
            tokenRepository.save(token);

            CorreoPacienteService correoPacienteService = new CorreoPacienteService();
            String link = request.getServerName()+":"+request.getLocalPort();
            correoPacienteService.enviarCorreo(temporal.getCorreo(),temporal.getNombre(), link, token.getIdPaciente(), token.getToken());
            attr.addFlashAttribute("msg","Paciente temporal "+temporal.getNombre()+" "+temporal.getApellidos()+" actualizado exitosamente.");
            return "redirect:/administrativo";
        }
    }

    @PostMapping("/administrativo/guardar")
    public String actualizarPaciente(@ModelAttribute("paciente") @Valid Paciente paciente,
                                     BindingResult bindingResult){
        List<Paciente> pacientes = pacienteRepository.findAll();
        boolean existDni = false;
        for (Paciente p : pacientes) {
            if (p.getCorreo().equals(paciente.getCorreo())) {
                existDni = true;
                break;
            }
        }
        List<Temporal> temp = temporalRepository.findAll();
        for (Temporal t : temp) {
            if (t.getCorreo().equals(paciente.getCorreo())) {
                existDni = true;
                break;
            }
        }
        if (bindingResult.hasErrors() || existDni){
            if (existDni){
                bindingResult.rejectValue("correo", "errorCorreo", "El correo ingresado ya está registrado");
            }
            return "administrativo/editar";
        }
        else {
            pacienteRepository.actualizarPaciente(paciente.getCorreo(), paciente.getDireccion(),
                    paciente.getDistrito().getIdDistrito(), paciente.getIdPaciente());

            Credenciales credenciales = credencialesRepository.buscarPorId(paciente.getIdPaciente());

            credenciales.setCorreo(paciente.getCorreo());

            return "redirect:/administrativo";
        }
    }
    @GetMapping("/administrativo/elegirFormulario")
    public String elegirFormulario(Model model, HttpSession session, Authentication authentication){
        Optional<Stylevistas> style = stylevistasRepository.findById(3);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorAdministrativo", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Administrativo admi = administrativoRepository.findByCorreo(userEmail);
        session.setAttribute("administrativo", admi);

        List<FormularioJson> formularios = formularioJsonRepository.findAll();
        model.addAttribute("formularios", formularios);
        String idAdmi = admi.getIdAdministrativo();

        AdministrativoPorEspecialidadPorSede aes = aesRepository.buscarPorAdministrativoId(idAdmi);
        model.addAttribute("datos", aes);
        model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorAdministrativoYActual(idAdmi));
        model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdmi));

        List<Paciente> lista = pacienteRepository.buscarPorIdAdministrativo(idAdmi);
        List<Temporal> listaTemp = temporalRepository.findByAdministrativo_IdAdministrativo(idAdmi);

        model.addAttribute("listaPacientes", lista);
        model.addAttribute("listaTemp", listaTemp);

        return "administrativo/elegirFormulario";
    }

    @PostMapping("/administrativo/guardarAlergia")
    public String guardarAlergia(RedirectAttributes attr, Authentication authentication, HttpSession session,
                                 @RequestParam ("idPaciente") String idPaciente,
                                 @RequestParam ("nombre") String alergia) {

        /*  Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());*/
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        Optional<Paciente> paciente1 = pacienteRepository.findByIdPaciente(idPaciente);
        if (paciente1.isPresent()){
            Alergia alergia1 = new Alergia();
            alergia1.setPaciente(paciente1.get());
            alergia1.setNombre(alergia);
            alergiaRepository.save(alergia1);
            return "redirect:/administrativo/editar?id="+idPaciente;
        } else {
            attr.addFlashAttribute("msgError", "Ocurrió un error al eliminar las alergias");
            return "redirect:/administrativo/editar?id="+idPaciente;
        }
    }
    @GetMapping("/administrativo/borrarAlergia")
    public String borrarAlergia(@RequestParam(name = "idAlergia") int idAlergia,
                                Authentication authentication, HttpSession session,
                                RedirectAttributes attr) {

        /*Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());
        session.setAttribute("paciente", paciente);*/

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        Optional<Alergia> optionalAlergia = alergiaRepository.findById(idAlergia);
        if (optionalAlergia.isPresent()) {
            alergiaRepository.deleteById(idAlergia);
            attr.addFlashAttribute("msg", "Se eliminó la alergia seleccionada con éxito");
        }
        return "redirect:/administrativo";
    }
    @GetMapping("administrativo/imageSede")
    public ResponseEntity<byte[]> mostrarImagenSede(@RequestParam("idSede") int idSede) {

        Optional<Sede> optionalSede = sedeRepository.findById(idSede);
        if (optionalSede.isPresent()) {
            Sede sede = optionalSede.get();
            byte[] imagenComoBytes = sede.getFoto();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType(sede.getFotocontenttype()));
            return new ResponseEntity<>(imagenComoBytes, httpHeaders, HttpStatus.OK);
        } else {
            return null;
        }
    }
}
