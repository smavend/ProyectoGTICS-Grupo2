package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.Especialidad;
import com.example.proyectogticsgrupo2.entity.Paciente;
import com.example.proyectogticsgrupo2.entity.Sede;
import com.example.proyectogticsgrupo2.repository.EspecialidadRepository;
import com.example.proyectogticsgrupo2.repository.PacienteRepository;
import com.example.proyectogticsgrupo2.repository.SedeRepository;
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
    final SedeRepository sedeRepository;
    final EspecialidadRepository especialidadRepository;

    public PacienteController(PacienteRepository pacienteRepository, EspecialidadRepository especialidadRepository, SedeRepository sedeRepository) {
        this.pacienteRepository = pacienteRepository;
        this.especialidadRepository = especialidadRepository;
        this.sedeRepository = sedeRepository;
    }

    /* INICIO */
    @GetMapping(value = {"", "/", "/index"})
    public String index(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById("45978547");
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/index";
    }

    /* RESERVAR CITA */
    @GetMapping("/reservar")
    public String reservarCita(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById("45978547");
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
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
    public String editarPerfil(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById("45978547");
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/perfilEditar";
    }

    /* SECCIÓN DOCTORES */
    @GetMapping("/doctores")
    public String verDoctores(Model model){
        List<Sede> sedeList =  sedeRepository.findAll();
        List<Especialidad> especialidadList = especialidadRepository.findAll();
        model.addAttribute("sedeList", sedeList);
        model.addAttribute("especialidadList", especialidadList);
        return "paciente/doctores";
    }

    @GetMapping("/perfilDoctor")
    public String verPerfilDoctor(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById("45978547");
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/doctorPerfil";
    }

    @GetMapping("/reservarDoctor")
    public String reservarCitaDoctor(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById("45978547");
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/reservarDoctor";
    }

    @GetMapping("/confirmacion")
    public String confirmarReserva(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById("45978547");
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/confirmacion";
    }

    @GetMapping("/sesionVirtual")
    public String sesionVirtual(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById("45978547");
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/sesionVirtual";
    }

    /* SECCIÓN CITAS */
    @GetMapping("citas")
    public String verCitas(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById("45978547");
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/citas";
    }

    /* SECCIÓN PAGOS */
    @GetMapping("/pagos")
    public String pagos(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById("45978547");
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/pagos";
    }

    @GetMapping("/recibo")
    public String verReciboPago(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById("45978547");
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/recibo";
    }

    /* SECCIÓN CUESTIONARIOS */
    @GetMapping("/cuestionarios")
    public String cuestionarios(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById("45978547");
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/cuestionarios";
    }

    @GetMapping("/completarCuestionario")
    public String completarCuestionario(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById("45978547");
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/completarCuestionario";
    }

    /* SECCIÓN CONSENTIMIENTOS */
    @GetMapping("/consentimientos")
    public String consentimientos(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById("45978547");
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/consentimientos";
    }
}
