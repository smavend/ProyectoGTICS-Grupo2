package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.dto.TemporalDiasDto;
import com.example.proyectogticsgrupo2.entity.AdministrativoPorEspecialidadPorSede;
import com.example.proyectogticsgrupo2.entity.Paciente;
import com.example.proyectogticsgrupo2.entity.PacienteTemporal;
import com.example.proyectogticsgrupo2.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    final PacienteTemporalRepository pacienteTemporalRepository;

    public AdministrativoController(PacienteRepository pacienteRepository, AdministrativoPorEspecialidadPorSedeRepository aesRepository, AlergiaRepository alergiaRepository, DistritoRepository distritoRepository, NotificacionRepository notificacionRepository, PacienteTemporalRepository pacienteTemporalRepository) {
        this.pacienteRepository = pacienteRepository;
        this.aesRepository = aesRepository;
        this.alergiaRepository = alergiaRepository;
        this.distritoRepository = distritoRepository;
        this.notificacionRepository = notificacionRepository;
        this.pacienteTemporalRepository = pacienteTemporalRepository;
    }

    @GetMapping("/administrativo")
    public String dashboard(Model model){
        List<Paciente> lista = pacienteRepository.buscarPorIdAdministrativo(idAdministrativo);
        List<PacienteTemporal> listaTemp = pacienteTemporalRepository.findAll();
        List<TemporalDiasDto> listaDias = pacienteTemporalRepository.obtenerDias();
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

    @GetMapping("/administrativo/editar/invitado")
    public String vistaEditarInvitado(@RequestParam(name = "id") Integer id,
                              Model model){
        Optional<PacienteTemporal> optPaciente = pacienteTemporalRepository.findById(id);
        if(optPaciente.isPresent()){
            PacienteTemporal temp = optPaciente.get();
            model.addAttribute("temporal", temp);


            AdministrativoPorEspecialidadPorSede aes = aesRepository.buscarPorAdministrativoId(idAdministrativo);
            model.addAttribute("datos", aes);
            model.addAttribute("listaNotificaciones", notificacionRepository.buscarPorUsuarioYActual(idAdministrativo));
            model.addAttribute("listaMensajes", pacienteRepository.obtenerMensajeDatos(idAdministrativo));
            return "administrativo/editarTemp";
        }else {
            return "redirect:/administrativo";
        }
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
    public String invitarPaciente(PacienteTemporal pacienteTemporal){
        pacienteTemporalRepository.guardarInvitado(pacienteTemporal.getNombre(),
                pacienteTemporal.getApellidos(),pacienteTemporal.getCorreo(),idAdministrativo);
        return "redirect:/administrativo";
    }

    @PostMapping("/administrativo/guardar/invitado")
    public String actualizarInvitado(PacienteTemporal pacienteTemporal){
        pacienteTemporalRepository.actualizarInvitado(pacienteTemporal.getNombre(), pacienteTemporal.getApellidos(), pacienteTemporal.getCorreo(), pacienteTemporal.getIdTemporal());
        return "redirect:/administrativo";
    }

    @PostMapping("/administrativo/guardar")
    public String actualizarPaciente(Paciente paciente){
        pacienteRepository.actualizarPaciente(paciente.getCorreo(), paciente.getDireccion(),
                paciente.getDistrito().getIdDistrito(), paciente.getIdPaciente());
        return "redirect:/administrativo";
    }
}
