package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.dto.ListaBuscadorDoctor;
import com.example.proyectogticsgrupo2.dto.ListaRecibosDTO;
import com.example.proyectogticsgrupo2.entity.Cita;
import com.example.proyectogticsgrupo2.entity.Paciente;
import com.example.proyectogticsgrupo2.repository.CitaRepository;
import com.example.proyectogticsgrupo2.repository.DoctorRepository;
import com.example.proyectogticsgrupo2.repository.PacienteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
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
        List<ListaBuscadorDoctor> optionalCita = citaRepository.listarPorDoctorProxCitas("10304011"); //CAMBIAR POR ID SESION
        List<ListaBuscadorDoctor> optionalCita2 = citaRepository.listarPorDoctorListaPacientes("10304011"); //CAMBIAR POR ID SESION
        ArrayList<String> listaHorarios= new ArrayList<>();

        // Transformar LocalDateTime a LocalDate
        optionalCita.forEach(cita -> {
            LocalDateTime fechaHora = cita.getInicio(); // Obtener LocalDateTime
            String hora1 = fechaHora.toLocalTime().toString();

            LocalDateTime fechaHora2 = cita.getFin();
            String hora2 = fechaHora2.toLocalTime().toString();
            // Transformar a LocalDate
             // Actualizar objeto ListaBuscadorDoctor con la fecha

            String horaFinal=hora1+" - "+hora2;
            listaHorarios.add(horaFinal);
        });




        model.addAttribute("listaHorarios", listaHorarios);
        model.addAttribute("listaCitas", optionalCita);
        model.addAttribute("listaPacientes", optionalCita2);


        return "doctor/DoctorDashboard";
    }

    @GetMapping("/recibo")
    public String recibo(Model model) {
        List<ListaRecibosDTO> optionalCita = citaRepository.listarRecibos("10304011");

        model.addAttribute("listaRecibos", optionalCita);//CAMBIAR POR ID SESION

        return "doctor/DoctorRecibos";
    }

    @PostMapping("/buscarRecibo")
    public String buscarRecibo(@RequestParam("searchField") String searchField,
                             Model model) {

        try {
            float floatSearchField = Float.parseFloat(searchField);
            System.out.println(Float.valueOf(floatSearchField).intValue());
            List<ListaRecibosDTO> optionalCita = citaRepository.buscarRecibosPago("10304011", Float.toString(floatSearchField));
            model.addAttribute("listaRecibos", optionalCita);

            // La variable es de tipo float
        } catch (NumberFormatException e) {
            // La variable no es de tipo float
            List<ListaRecibosDTO> optionalCita = citaRepository.buscarRecibosNombre("10304011",searchField);
            model.addAttribute("listaRecibos", optionalCita);
        }


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
            String fechaHora =cita.getInicio().toString();
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

    @GetMapping("/verCuestionario")
    public String verCuestionario(@RequestParam("id") String id){

        return "doctor/DoctorVerCuestionario";


    }

    @GetMapping("/mensajeria")
    public String mensajeria(Model model){
        List<ListaBuscadorDoctor> citaList=citaRepository.listarPorDoctorProxCitas("10304011"); //CAMBIAR CON ID DE SESION
        model.addAttribute("listaCitas",citaList);
        return "doctor/DoctorMensajería";
    }

    @PostMapping("/guardarReporte")
    public String guardarReporte(Cita cita, RedirectAttributes attr) {
        citaRepository.save(cita);
        return "redirect:/doctor/dashboard";
    }

    @PostMapping("/BuscarProxCita")
    public String buscarCita(@RequestParam("searchField") String searchField,
                                      Model model) {

        List<ListaBuscadorDoctor> optionalCita = citaRepository.buscadorProximasCitas("10304011",searchField);
        List<ListaBuscadorDoctor> optionalCita2 = citaRepository.listarPorDoctorListaPacientes("10304011"); //CAMBIAR POR ID SESION

        ArrayList<String> listaHorarios= new ArrayList<>();

        // Transformar LocalDateTime a LocalDate
        optionalCita.forEach(cita -> {
            LocalDateTime fechaHora = cita.getInicio(); // Obtener LocalDateTime
            String hora1 = fechaHora.toLocalTime().toString();

            LocalDateTime fechaHora2 = cita.getFin();
            String hora2 = fechaHora2.toLocalTime().toString();
            // Transformar a LocalDate
            // Actualizar objeto ListaBuscadorDoctor con la fecha

            String horaFinal=hora1+" - "+hora2;
            listaHorarios.add(horaFinal);
        });

        model.addAttribute("listaHorarios", listaHorarios);
        model.addAttribute("listaCitas", optionalCita);//CAMBIAR POR ID SESION
        model.addAttribute("listaPacientes", optionalCita2);//CAMBIAR POR ID SESION

        return "doctor/DoctorDashboard";
    }

    @PostMapping("/BuscarPaciente")
    public String buscarPaciente(@RequestParam("searchField") String searchField,
                             Model model) {

        List<ListaBuscadorDoctor> optionalCita = citaRepository.buscadorPaciente("10304011",searchField);
        List<ListaBuscadorDoctor> optionalCita2 = citaRepository.listarPorDoctorProxCitas("10304011"); //CAMBIAR POR ID SESION

        ArrayList<String> listaHorarios= new ArrayList<>();

        // Transformar LocalDateTime a LocalDate
        optionalCita2.forEach(cita -> {
            LocalDateTime fechaHora = cita.getInicio(); // Obtener LocalDateTime
            String hora1 = fechaHora.toLocalTime().toString();

            LocalDateTime fechaHora2 = cita.getFin();
            String hora2 = fechaHora2.toLocalTime().toString();
            // Transformar a LocalDate
            // Actualizar objeto ListaBuscadorDoctor con la fecha

            String horaFinal=hora1+" - "+hora2;
            listaHorarios.add(horaFinal);
        });

        model.addAttribute("listaHorarios", listaHorarios);
        model.addAttribute("listaCitas", optionalCita2);//CAMBIAR POR ID SESION
        model.addAttribute("listaPacientes", optionalCita);//CAMBIAR POR ID SESION

        return "doctor/DoctorDashboard";
    }

    @GetMapping("/prueba")
    public String prueba(Model model){

        return "doctor/prueba";
    }
}
