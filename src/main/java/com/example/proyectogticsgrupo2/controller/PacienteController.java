package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.Paciente;
import com.example.proyectogticsgrupo2.repository.PacienteRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Controller
@RequestMapping("/Paciente")
public class PacienteController {

    final PacienteRepository pacienteRepository;

    public PacienteController(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping("/paciente/foto/{id}")
    public void showPacienteImage(@PathVariable String id,
                                  HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");

        Optional<Paciente> optPaciente = pacienteRepository.findById(id);
        if(optPaciente.isPresent()){
            Paciente paciente = optPaciente.get();
            InputStream is = new ByteArrayInputStream(paciente.getFoto());
            IOUtils.copy(is, response.getOutputStream());
        }
    }

    /* INICIO */
    @GetMapping(value = {"", "/", "/index"})
    public String index(){
        return "paciente/index";
    }

    /* RESERVAR CITA */
    @GetMapping("/reservar")
    public String reservarCita(){
        return "paciente/reservarCita";
    }

    /* PERFIL */
    @GetMapping("/perfil")
    public String perfil(){
        return "paciente/perfil";
    }

    /* SECCIÓN DOCTORES */
    @GetMapping("/doctores")
    public String verDoctores(){
        return "paciente/verDoctores";
    }

    @GetMapping("/perfilDoctor")
    public String verPerfilDoctor(){
        return "paciente/perfilDoctor";
    }

    @GetMapping("/reservarDoctor")
    public String reservarCitaDoctor(){
        return "paciente/reservarCitaDoctor";
    }

    @GetMapping("/confirmacion")
    public String confirmarReserva(){
        return "paciente/confirmacionCita";
    }

    @GetMapping("/sesionVirtual")
    public String sesionVirtual(){
        return "paciente/sesionVirtual";
    }

    /* SECCIÓN CITAS */
    @GetMapping("citas")
    public String verCitas(){
        return "paciente/verCitas";
    }

    /* SECCIÓN PAGOS */
    @GetMapping("/pagos")
    public String pagos(){
        return "paciente/pagos";
    }

    @GetMapping("/recibo")
    public String verReciboPago(){
        return "paciente/reciboPago";
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
