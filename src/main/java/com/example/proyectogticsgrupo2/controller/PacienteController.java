package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.Paciente;
import com.example.proyectogticsgrupo2.repository.PacienteRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Controller
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
    @GetMapping(value = {"", "/", "index"})
    public String index(){
        return "paciente/index";
    }

    @GetMapping(value = {"/perfil"})
    public String perfil(){
        return "paciente/perfil";
    }

    @GetMapping(value = {"/reservarCita"})
    public String reservarCita(){
        return "paciente/reservarCita";
    }

    @GetMapping(value = {"/verCitas"})
    public String verCitas(){
        return "paciente/verCitas";
    }

    @GetMapping(value = {"/pagos"})
    public String pagos(){
        return "paciente/pagos";
    }

    @GetMapping(value = {"/cuestionarios"})
    public String cuestionarios(){ return "paciente/cuestionarios"; }

    @GetMapping(value = {"/consentimientos"})
    public String consentimientos(){ return "paciente/consentimientos"; }

    @GetMapping(value = {"/doctores"})
    public String verDoctores(){ return "paciente/verDoctores"; }

    @GetMapping(value = {"/perfilDoctor"})
    public String verPerfilDoctor(){ return "paciente/perfilDoctor"; }

    @GetMapping(value = {"/reservarCitaDoctor"})
    public String reservarCitaDoctor(){ return "paciente/reservarCitaDoctor"; }

    @GetMapping(value = {"/confirmarReserva"})
    public String confirmarReserva(){ return "paciente/confirmacionCita"; }

    @GetMapping(value = {"/sesionVirtual"})
    public String sesionVirtual(){ return "paciente/sesionVirtual"; }

    @GetMapping(value = {"/reciboPago"})
    public String verReciboPago(){ return "paciente/reciboPago"; }

    @GetMapping(value = {"/completarCuestionario"})
    public String completarCuestionario(){ return "paciente/completarCuestionario"; }
}
