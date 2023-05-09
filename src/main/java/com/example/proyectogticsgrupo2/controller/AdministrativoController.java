package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.dto.TemporalDiasDto;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
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
    String idAdministrativo = "15648912";
    final PacienteRepository pacienteRepository;
    final AdministrativoPorEspecialidadPorSedeRepository aesRepository;
    final AlergiaRepository alergiaRepository;
    final DistritoRepository distritoRepository;
    final NotificacionRepository notificacionRepository;
    final TemporalRepository temporalRepository;
    final AdministrativoRepository administrativoRepository;

    public AdministrativoController(PacienteRepository pacienteRepository, AdministrativoPorEspecialidadPorSedeRepository aesRepository, AlergiaRepository alergiaRepository, DistritoRepository distritoRepository, NotificacionRepository notificacionRepository, TemporalRepository temporalRepository, AdministrativoRepository administrativoRepository) {
        this.pacienteRepository = pacienteRepository;
        this.aesRepository = aesRepository;
        this.alergiaRepository = alergiaRepository;
        this.distritoRepository = distritoRepository;
        this.notificacionRepository = notificacionRepository;
        this.temporalRepository = temporalRepository;
        this.administrativoRepository = administrativoRepository;
    }

    @GetMapping("/administrativo")
    public String dashboard(Model model){
        List<Paciente> lista = pacienteRepository.buscarPorIdAdministrativo(idAdministrativo);
        List<Temporal> listaTemp = temporalRepository.findAll();
        List<TemporalDiasDto> listaDias = temporalRepository.obtenerDias();
        AdministrativoPorEspecialidadPorSede aes = aesRepository.buscarPorAdministrativoId(idAdministrativo);
        model.addAttribute("datos", aes);
        model.addAttribute("listaPacientes", lista);
        model.addAttribute("listaTemp", listaTemp);
        model.addAttribute("dias",listaDias);
        model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdministrativo));
        model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdministrativo));
        return "administrativo/index";
    }

    @GetMapping("/administrativo/invitar")
    public String vistaInvitar(Model model, @ModelAttribute("temporal") Temporal temporal){
        AdministrativoPorEspecialidadPorSede aes = aesRepository.buscarPorAdministrativoId(idAdministrativo);
        model.addAttribute("datos", aes);
        model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdministrativo));
        model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdministrativo));
        return "administrativo/invitar";
    }

    @GetMapping("/administrativo/editar")
    public String vistaEditar(@RequestParam(name = "id") String id,
                              Model model){
        Optional<Paciente> optPaciente = pacienteRepository.findById(id);
        if(optPaciente.isPresent()){
            Paciente paciente = optPaciente.get();
            if(paciente.getAdministrativo()!=null) {
                if (paciente.getAdministrativo().getIdAdministrativo().equals(idAdministrativo)) {
                    model.addAttribute("paciente", paciente);
                    model.addAttribute("alergias", alergiaRepository.buscarPorPacienteId(id));
                    model.addAttribute("listaDistritos", distritoRepository.findAll());


                    AdministrativoPorEspecialidadPorSede aes = aesRepository.buscarPorAdministrativoId(idAdministrativo);
                    model.addAttribute("datos", aes);
                    model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdministrativo));
                    model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdministrativo));
                    return "administrativo/editar";
                }
            }
        }
        return "redirect:/administrativo";
    }

    @GetMapping("/administrativo/editar/invitado")
    public String vistaEditarInvitado(@RequestParam(name = "id") Integer id,
                                      Model model){
        Optional<Temporal> optTemp = temporalRepository.findById(id);
        if(optTemp.isPresent()){
            Temporal temp = optTemp.get();
            if(temp.getAdministrativo().getIdAdministrativo().equals(idAdministrativo)) {
                model.addAttribute("temporal", temp);

                AdministrativoPorEspecialidadPorSede aes = aesRepository.buscarPorAdministrativoId(idAdministrativo);
                model.addAttribute("datos", aes);
                model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdministrativo));
                model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdministrativo));
                return "administrativo/editarTemp";
            }
        }
        return "redirect:/administrativo";
    }
    @GetMapping("/administrativo/mensajeria")
    public String mostrarMensajeria(Model model){
        AdministrativoPorEspecialidadPorSede aes = aesRepository.buscarPorAdministrativoId(idAdministrativo);
        model.addAttribute("datos", aes);
        model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdministrativo));
        model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdministrativo));
        return "administrativo/mensajeria";
    }
    @PostMapping("administrativo/invitar")
    public String invitarPaciente(Model model,@ModelAttribute("temporal") @Valid Temporal temporal,
                                  BindingResult bindingResult,
                                  RedirectAttributes attr){
        if(bindingResult.hasErrors()){
            AdministrativoPorEspecialidadPorSede aes = aesRepository.buscarPorAdministrativoId(idAdministrativo);
            model.addAttribute("datos", aes);
            model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdministrativo));
            model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdministrativo));
            return "administrativo/invitar";
        }
        else {
            List<Paciente> list = pacienteRepository.findAll();
            boolean exist = false;
            boolean existTemp = false;
            for(Paciente p: list){
                if(p.getIdPaciente().equals(temporal.getDni())){
                    exist = true;
                    break;
                }
            }
            List<Temporal> temp = temporalRepository.findAll();
            for (Temporal t: temp){
                if(t.getDni().equals(temporal.getDni())){
                    existTemp = true;
                    break;
                }
            }
            if(!exist & !existTemp){
                temporal.setFechainvitado(LocalDateTime.now());
                Optional<Administrativo> optAdministrativo = administrativoRepository.findById(idAdministrativo);
                temporal.setAdministrativo(optAdministrativo.get());
                temporalRepository.save(temporal);
                return "redirect:/administrativo";
            }else {
                attr.addFlashAttribute("msg","Ingrese un DNI diferente, ya se encuentra invitado o registrado");
                AdministrativoPorEspecialidadPorSede aes = aesRepository.buscarPorAdministrativoId(idAdministrativo);
                model.addAttribute("datos", aes);
                model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdministrativo));
                model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdministrativo));
                return "redirect:/administrativo/invitar";
            }
        }
    }

    @PostMapping("/administrativo/guardar/invitado")
    public String actualizarInvitado(Model model,
                                     @ModelAttribute("temporal") @Valid Temporal temporal,
                                     BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            AdministrativoPorEspecialidadPorSede aes = aesRepository.buscarPorAdministrativoId(idAdministrativo);
            model.addAttribute("datos", aes);
            model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdministrativo));
            model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdministrativo));
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
