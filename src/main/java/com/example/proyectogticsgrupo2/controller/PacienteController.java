package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.Especialidad;
import com.example.proyectogticsgrupo2.entity.Paciente;
import com.example.proyectogticsgrupo2.repository.EspecialidadRepository;
import com.example.proyectogticsgrupo2.repository.PacienteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/Paciente")
public class PacienteController {

    final PacienteRepository pacienteRepository;
    final EspecialidadRepository especialidadRepository;

    public PacienteController(PacienteRepository pacienteRepository, EspecialidadRepository especialidadRepository) {
        this.pacienteRepository = pacienteRepository;
        this.especialidadRepository = especialidadRepository;
    }

    /* INICIO */
    @GetMapping(value = {"", "/", "/index"})
    public String index(){
        return "paciente/index";
    }

    /* RESERVAR CITA */
    @GetMapping("/reservar")
    public String reservarCita(){
        return "paciente/reservar";
    }

    /* PERFIL */
    @GetMapping("/perfil")
    public String perfil(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById("45978547");
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/perfil";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil(){
        return "paciente/perfilEditar";
    }

    /* SECCIÓN DOCTORES */
    @GetMapping("/doctores")
    public String verDoctores(Model model){
        List<Especialidad> especialidadList = especialidadRepository.findAll();
        model.addAttribute("especialidadList", especialidadList);
        return "paciente/doctores";
    }

    @GetMapping("/perfilDoctor")
    public String verPerfilDoctor(){
        return "paciente/doctorPerfil";
    }

    @GetMapping("/reservarDoctor")
    public String reservarCitaDoctor(){
        return "paciente/reservarDoctor";
    }

    @GetMapping("/confirmacion")
    public String confirmarReserva(){
        return "paciente/confirmacion";
    }

    @GetMapping("/sesionVirtual")
    public String sesionVirtual(){
        return "paciente/sesionVirtual";
    }

    /* SECCIÓN CITAS */
    @GetMapping("citas")
    public String verCitas(){
        return "paciente/citas";
    }

    /* SECCIÓN PAGOS */
    @GetMapping("/pagos")
    public String pagos(){
        return "paciente/pagos";
    }

    @GetMapping("/recibo")
    public String verReciboPago(){
        return "paciente/recibo";
    }

    /* SECCIÓN CUESTIONARIOS */
    @GetMapping("/cuestionarios")
    public String cuestionarios(){
        return "paciente/cuestionarios";
    }

    @GetMapping("/completarCuestionario")
    public String completarCuestionario(){
        return "paciente/completarCuestionario";
    }

    /* SECCIÓN CONSENTIMIENTOS */
    @GetMapping("/consentimientos")
    public String consentimientos(){
        return "paciente/consentimientos";
    }
}
