package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.Cita;
import com.example.proyectogticsgrupo2.entity.Doctor;
import com.example.proyectogticsgrupo2.entity.Paciente;
import com.example.proyectogticsgrupo2.repository.CitaRepository;
import com.example.proyectogticsgrupo2.repository.DoctorRepository;
import com.example.proyectogticsgrupo2.repository.PacienteRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RequestMapping("/doctor")
@Controller
public class DoctorController {
    final DoctorRepository doctorRepository;
    final PacienteRepository pacienteRepository;
    final CitaRepository citaRepository;

    public DoctorController(DoctorRepository doctorRepository, PacienteRepository pacienteRepository, CitaRepository citaRepository) {
        this.doctorRepository = doctorRepository;
        this.pacienteRepository = pacienteRepository;
        this.citaRepository = citaRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){

        model.addAttribute("listaPacientes",pacienteRepository.findAll());
        model.addAttribute("listaCitas",citaRepository.findAll());
        return "doctor/DoctorDashboard";
    }

    @GetMapping("/calendario")
    public String reportes(Model model){


        return "doctor/DoctorCalendario";
    }

    @GetMapping("/recibo")
    public String recibo(Model model){


        return "doctor/DoctorRecibos";
    }

    @GetMapping("/reporte")
    public String reporte(Model model){


        return "doctor/DoctorReporteSesion";
    }

    @GetMapping("/mensajeria")
    public String mensajeria(Model model){
        List<Cita> citaList=citaRepository.BuscarPorDoctor(1); //CAMBIAR CON ID DE SESION
        model.addAttribute("listaCitas",citaList);
        return "doctor/DoctorMensajería";
    }

}
