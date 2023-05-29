package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.dto.TemporalDiasDto;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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

    public AdministrativoController(PacienteRepository pacienteRepository, AdministrativoPorEspecialidadPorSedeRepository aesRepository, AlergiaRepository alergiaRepository, DistritoRepository distritoRepository, NotificacionRepository notificacionRepository, TemporalRepository temporalRepository, AdministrativoRepository administrativoRepository, HttpSession session, HttpServletRequest request) {
        this.pacienteRepository = pacienteRepository;
        this.aesRepository = aesRepository;
        this.alergiaRepository = alergiaRepository;
        this.distritoRepository = distritoRepository;
        this.notificacionRepository = notificacionRepository;
        this.temporalRepository = temporalRepository;
        this.administrativoRepository = administrativoRepository;
    }
    @GetMapping("/administrativo")
    public String dashboard(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        Administrativo admi = (Administrativo) session.getAttribute("administrativo");
        String idAdmi = admi.getIdAdministrativo();

        AdministrativoPorEspecialidadPorSede aes = aesRepository.buscarPorAdministrativoId(idAdmi);
        model.addAttribute("datos", aes);
        model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdmi));
        model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdmi));

        List<Paciente> lista = pacienteRepository.buscarPorIdAdministrativo(idAdmi);
        List<Temporal> listaTemp = temporalRepository.findByAdministrativo_IdAdministrativo(idAdmi);
        List<TemporalDiasDto> listaDias = temporalRepository.obtenerDias();

        model.addAttribute("listaPacientes", lista);
        model.addAttribute("listaTemp", listaTemp);
        model.addAttribute("dias",listaDias);

        return "administrativo/index";
    }

    @GetMapping("/administrativo/invitar")
    public String vistaInvitar(HttpServletRequest request, Model model, @ModelAttribute("temporal") Temporal temporal){
        HttpSession session = request.getSession();
        Administrativo admi = (Administrativo) session.getAttribute("administrativo");
        String idAdmi = admi.getIdAdministrativo();

        model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdmi));
        model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdmi));
        return "administrativo/invitar";
    }

    @GetMapping("/administrativo/editar")
    public String vistaEditar(HttpServletRequest request, @RequestParam(name = "id") String id,
                              Model model){

        Optional<Paciente> optPaciente = pacienteRepository.findById(id);
        if(optPaciente.isPresent()){
            Paciente paciente = optPaciente.get();
            if(paciente.getAdministrativo()!=null) {
                HttpSession session = request.getSession();
                Administrativo admi = (Administrativo) session.getAttribute("administrativo");
                String idAdmi = admi.getIdAdministrativo();

                if (paciente.getAdministrativo().getIdAdministrativo().equals(idAdmi)) {
                    model.addAttribute("paciente", paciente);
                    model.addAttribute("alergias", alergiaRepository.buscarPorPacienteId(id));
                    model.addAttribute("listaDistritos", distritoRepository.findAll());

                    model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdmi));
                    model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdmi));
                    return "administrativo/editar";
                }
            }
        }
        return "redirect:/administrativo";
    }

    @GetMapping("/administrativo/editar/invitado")
    public String vistaEditarInvitado(HttpServletRequest request,
                                      @RequestParam(name = "id") Integer id,
                                      Model model){

        Optional<Temporal> optTemp = temporalRepository.findById(id);
        if(optTemp.isPresent()){
            Temporal temp = optTemp.get();
            HttpSession session = request.getSession();
            Administrativo admi = (Administrativo) session.getAttribute("administrativo");
            String idAdmi = admi.getIdAdministrativo();
            if(temp.getAdministrativo().getIdAdministrativo().equals(idAdmi)) {
                model.addAttribute("temporal", temp);
                model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdmi));
                model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdmi));
                return "administrativo/editarTemp";
            }
        }
        return "redirect:/administrativo";
    }
    @GetMapping("/administrativo/mensajeria")
    public String mostrarMensajeria(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        Administrativo admi = (Administrativo) session.getAttribute("administrativo");
        String idAdmi = admi.getIdAdministrativo();

        model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdmi));
        model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdmi));
        return "administrativo/mensajeria";
    }

    @PostMapping("administrativo/invitar")
    public String invitarPaciente(HttpServletRequest request, Model model,
                                  @ModelAttribute("temporal") @Valid Temporal temporal,
                                  BindingResult bindingResult,
                                  RedirectAttributes attr){
        HttpSession session = request.getSession();
        Administrativo admi = (Administrativo) session.getAttribute("administrativo");
        String idAdmi = admi.getIdAdministrativo();

        if(bindingResult.hasErrors()){
            model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdmi));
            model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdmi));
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
                return "redirect:/administrativo";
            } else {
                attr.addFlashAttribute("msg", "Ingrese un DNI o correo diferente, ya se encuentra invitado o registrado");
                return "redirect:/administrativo/invitar";
            }
        }
    }

    @PostMapping("/administrativo/guardar/invitado")
    public String actualizarInvitado(HttpServletRequest request,
                                     Model model,
                                     @ModelAttribute("temporal") @Valid Temporal temporal,
                                     BindingResult bindingResult){
        HttpSession session = request.getSession();
        Administrativo admi = (Administrativo) session.getAttribute("administrativo");
        String idAdmi = admi.getIdAdministrativo();

        if(bindingResult.hasErrors()){
            model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdmi));
            model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdmi));
            return "administrativo/editarTemp";
        }else {
            temporalRepository.actualizarInvitado(temporal.getNombre(), temporal.getApellidos(), temporal.getCorreo(), temporal.getId_temporal());
            return "redirect:/administrativo";
        }
    }

    @PostMapping("/administrativo/guardar")
    public String actualizarPaciente(Paciente paciente){
        pacienteRepository.actualizarPaciente(paciente.getCorreo(), paciente.getDireccion(),
                paciente.getDistrito().getIdDistrito(), paciente.getIdPaciente());
        return "redirect:/administrativo";
    }
}
