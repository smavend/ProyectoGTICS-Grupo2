package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.AdministrativoPorEspecialidadPorSede;
import com.example.proyectogticsgrupo2.entity.Paciente;
import com.example.proyectogticsgrupo2.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    public AdministrativoController(PacienteRepository pacienteRepository, AdministrativoPorEspecialidadPorSedeRepository aesRepository, AlergiaRepository alergiaRepository, DistritoRepository distritoRepository, NotificacionRepository notificacionRepository) {
        this.pacienteRepository = pacienteRepository;
        this.aesRepository = aesRepository;
        this.alergiaRepository = alergiaRepository;
        this.distritoRepository = distritoRepository;
        this.notificacionRepository = notificacionRepository;
    }

    @GetMapping("/administrativo")
    public String dashboard(Model model){
        List<Paciente> lista = pacienteRepository.buscarPorIdAdministrativo(idAdministrativo);
        AdministrativoPorEspecialidadPorSede aes = aesRepository.buscarPorAdministrativoId(idAdministrativo);
        model.addAttribute("datos", aes);
        model.addAttribute("listaPacientes", lista);
        model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdministrativo));
        model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdministrativo));
        return "administrativo/index";
    }

    @GetMapping("/administrativo/invitar")
    public String vistaInvitar(Model model){
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
            model.addAttribute("paciente", paciente);
            model.addAttribute("alergias", alergiaRepository.buscarPorPacienteId(id));
            model.addAttribute("listaDistritos", distritoRepository.findAll());


            AdministrativoPorEspecialidadPorSede aes = aesRepository.buscarPorAdministrativoId(idAdministrativo);
            model.addAttribute("datos", aes);
            model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdministrativo));
            model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdministrativo));
            return "administrativo/editar";
        }else {
            return "redirect:/administrativo";
        }
    }
}
