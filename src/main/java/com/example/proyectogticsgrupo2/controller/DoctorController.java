package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.Cita;
import com.example.proyectogticsgrupo2.entity.Doctor;
import com.example.proyectogticsgrupo2.entity.Paciente;
import com.example.proyectogticsgrupo2.repository.CitaRepository;
import com.example.proyectogticsgrupo2.repository.DoctorRepository;
import com.example.proyectogticsgrupo2.repository.PacienteRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hibernate.annotations.Parameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    @GetMapping(value={"/dashboard","/",""})
    public String dashboard(Model model) {
        List<Cita> optionalCita = citaRepository.BuscarPorDoctor("09568265");

        ArrayList<String> listaHorarios= new ArrayList<>();

        for (int i = 0; i < optionalCita.size(); i++) {
            String tiempoInicio = optionalCita.get(i).getInicio();
            String[] partesInicio = tiempoInicio.split(" ");
            String horaCompletaInicio = partesInicio[1];
            String[] partes2Inicio = horaCompletaInicio.split(":");
            String horaInicio = partes2Inicio[0] + ":" + partes2Inicio[1];

            String tiempoFin = optionalCita.get(i).getFin();
            String[] partesFin = tiempoFin.split(" ");
            String horaCompletaFin = partesFin[1];
            String[] partes2Fin = horaCompletaFin.split(":");
            String horaFin = partes2Fin[0] + ":" + partes2Fin[1];

            String horario = horaInicio + " - " + horaFin;
            System.out.println(horario);
            listaHorarios.add(horario);
        }



        model.addAttribute("listaCitas", citaRepository.BuscarPorDoctor("09568265"));//CAMBIAR POR ID SESION
        model.addAttribute("listaHorarios", listaHorarios);

        return "doctor/DoctorDashboard";
    }

    @GetMapping("/recibo")
    public String recibo(Model model) {
        List<Cita> optionalCita = citaRepository.BuscarPorDoctor("09568265");
        ArrayList<String> listaHorarios= new ArrayList<>();

        for (int i = 0; i < optionalCita.size(); i++) {
            String tiempoInicio = optionalCita.get(i).getInicio();
            String[] partesInicio = tiempoInicio.split(" ");
            String fechaCompletaInicio = partesInicio[0];

            listaHorarios.add(fechaCompletaInicio);
        }

        model.addAttribute("listaHorarios", listaHorarios);
        model.addAttribute("listaCitas", optionalCita);//CAMBIAR POR ID SESION

        return "doctor/DoctorRecibos";
    }

    @GetMapping("/verRecibo")
    public String verRecibo(Model model) {


        return "doctor/DoctorVerRecibo";
    }

    @GetMapping("/calendario")
    public String reportes(Model model){


        return "doctor/DoctorCalendario";
    }

    @GetMapping("/reporte")
    public String reporte(Model model, @RequestParam("id") String id,@RequestParam("id2") String id2){
        Optional<Cita> optionalCita= citaRepository.findById(id2);
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(id);
        if (optionalPaciente.isPresent() & optionalCita.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            Cita cita = optionalCita.get();
            String fechaHora =cita.getInicio();
            String[] partes = fechaHora.split(" ");
            String fecha = partes[0];

            model.addAttribute("paciente",paciente);
            model.addAttribute("fecha",fecha);
            model.addAttribute("cita",cita);

            return "doctor/DoctorReporteSesion";
        } else {
            return "redirect:/doctor/Dashboard";
        }

    }

    @GetMapping("/mensajeria")
    public String mensajeria(Model model){
        List<Cita> citaList=citaRepository.BuscarPorDoctor("09568265"); //CAMBIAR CON ID DE SESION
        model.addAttribute("listaCitas",citaList);
        return "doctor/DoctorMensajería";
    }

    @PostMapping("/guardarReporte")
    public String guardarReporte(Cita cita, RedirectAttributes attr) {
        citaRepository.save(cita);
        return "redirect:/doctor/dashboard";
    }

    @GetMapping("/prueba")
    public String prueba(Model model){

        return "doctor/prueba";
    }
}
