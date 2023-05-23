package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.dto.AlergiasPacienteDTO;
import com.example.proyectogticsgrupo2.dto.ListaBuscadorDoctor;
import com.example.proyectogticsgrupo2.dto.ListaRecibosDTO;
import com.example.proyectogticsgrupo2.dto.TratamientoDTO;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.print.Doc;
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

    private String idPaciente;
    private int idCita;
    private String fecha;
    private final AlergiaRepository alergiaRepository;



    public DoctorController(DoctorRepository doctorRepository, PacienteRepository pacienteRepository, CitaRepository citaRepository,
                            AlergiaRepository alergiaRepository
                            ) {
        this.doctorRepository = doctorRepository;
        this.pacienteRepository = pacienteRepository;
        this.citaRepository = citaRepository;
        this.alergiaRepository = alergiaRepository;
    }

    @GetMapping(value={"/dashboard","/",""})
    public String dashboard(Model model) {
        List<ListaBuscadorDoctor> optionalCita = citaRepository.listarPorDoctorProxCitas("10304011"); //CAMBIAR POR ID SESION
        List<ListaBuscadorDoctor> optionalCita2 = citaRepository.listarPorDoctorListaPacientes("10304011"); //CAMBIAR POR ID SESION
        ArrayList<String> listaHorarios= new ArrayList<>();
        Optional<Doctor> doctorOptional=doctorRepository.findById("10304011");
        Doctor doctor= doctorOptional.get();


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



        model.addAttribute("doctor", doctor);
        model.addAttribute("listaHorarios", listaHorarios);
        model.addAttribute("listaCitas", optionalCita);
        model.addAttribute("listaPacientes", optionalCita2);


        return "doctor/DoctorDashboard";
    }

    @GetMapping("/recibo")
    public String recibo(Model model) {
        List<ListaRecibosDTO> optionalCita = citaRepository.listarRecibos("10304011");

        Optional<Doctor> doctorOptional=doctorRepository.findById("10304011");
        Doctor doctor= doctorOptional.get();
        model.addAttribute("doctor", doctor);
        model.addAttribute("listaRecibos", optionalCita);//CAMBIAR POR ID SESION

        return "doctor/DoctorRecibos";
    }


    @GetMapping("/verRecibo")
    public String verRecibo(Model model, @RequestParam("id") int id_cita,@RequestParam("id2") String id_doctor) {
        System.out.println("hasta aca funciona");

        Optional<ListaRecibosDTO> optionalListaRecibosDTO=citaRepository.buscarRecibosPorIdCitaIdDoctor(id_doctor,id_cita);

        Optional<Doctor> optionalDoctor=doctorRepository.findById(id_doctor);

        if (optionalDoctor.isPresent() & optionalListaRecibosDTO.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            ListaRecibosDTO listaRecibosDTO=optionalListaRecibosDTO.get();
            model.addAttribute("doctor",doctor);
            model.addAttribute("cita",listaRecibosDTO);

            return "doctor/DoctorVerRecibo";
        } else {
            return "redirect:/doctor/recibo";
        }


    }

    @PostMapping("/buscarRecibo")
    public String buscarRecibo(@RequestParam("searchField") String searchField,
                             Model model) {

        try {
            float floatSearchField = Float.parseFloat(searchField);
            System.out.println(Float.valueOf(floatSearchField).intValue());
            List<ListaRecibosDTO> optionalCita = citaRepository.buscarRecibosPago("10304011", Float.toString(floatSearchField));

            Optional<Doctor> doctorOptional=doctorRepository.findById("10304011");
            Doctor doctor= doctorOptional.get();

            model.addAttribute("doctor", doctor);
            model.addAttribute("listaRecibos", optionalCita);

            // La variable es de tipo float
        } catch (NumberFormatException e) {
            // La variable no es de tipo float
            List<ListaRecibosDTO> optionalCita = citaRepository.buscarRecibosNombre("10304011",searchField);

            Optional<Doctor> doctorOptional=doctorRepository.findById("10304011");
            Doctor doctor= doctorOptional.get();

            model.addAttribute("doctor", doctor);
            model.addAttribute("listaRecibos", optionalCita);
        }


        return "doctor/DoctorRecibos";
    }



    @GetMapping("/calendario")
    public String reportes(Model model){

        Optional<Doctor> doctorOptional=doctorRepository.findById("10304011");
        Doctor doctor= doctorOptional.get();

        model.addAttribute("doctor", doctor);

        return "doctor/DoctorCalendario";
    }

    @GetMapping("/reporte")
    public String reporte(Model model, @RequestParam("id") String id,@RequestParam("id2") int id2){
        setIdPaciente(id);
        setIdCita(id2);
        Optional<Cita> optionalCita= citaRepository.findById(id2);
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(id);
        if (optionalPaciente.isPresent() & optionalCita.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            Cita cita = optionalCita.get();


            Optional<Doctor> doctorOptional=doctorRepository.findById("10304011");
            Doctor doctor= doctorOptional.get();
            model.addAttribute("doctor", doctor);
            model.addAttribute("paciente",paciente);
            model.addAttribute("cita",cita);

            return "doctor/DoctorReporteSesion";
        } else {
            return "redirect:/doctor/dashboard";
        }

    }

    @GetMapping("/verCuestionario")
    public String verCuestionario(Model model, @RequestParam("id") String id){
        Optional<Doctor> doctorOptional=doctorRepository.findById("10304011");

        Doctor doctor= doctorOptional.get();
        model.addAttribute("doctor", doctor);
        return "doctor/DoctorVerCuestionario";
    }

    @GetMapping("/mensajeria")
    public String mensajeria(Model model){
        List<ListaBuscadorDoctor> citaList=citaRepository.listarPorDoctorProxCitas("10304011"); //CAMBIAR CON ID DE SESION
        Optional<Doctor> doctorOptional=doctorRepository.findById("10304011");
        Doctor doctor= doctorOptional.get();
        model.addAttribute("doctor", doctor);
        model.addAttribute("listaCitas",citaList);
        return "doctor/DoctorMensajería";
    }

    @PostMapping("/guardarReporte")
    public String guardarReporte(Model model,@Valid Cita cita, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){

            Optional<Cita> optionalCita= citaRepository.findById(getIdCita());
            Optional<Paciente> optionalPaciente = pacienteRepository.findById(getIdPaciente());
            Paciente paciente = optionalPaciente.get();
            Cita cita1 = optionalCita.get();

            Optional<Doctor> doctorOptional=doctorRepository.findById("10304011");
            Doctor doctor= doctorOptional.get();
            model.addAttribute("doctor", doctor);

            model.addAttribute("paciente", paciente);
            model.addAttribute("fecha", getFecha());
            model.addAttribute("cita", cita1);
            return "doctor/DoctorReporteSesion";
        }else{

            citaRepository.save(cita);
            return "redirect:/doctor/dashboard";
        }
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

        Optional<Doctor> doctorOptional=doctorRepository.findById("10304011");
        Doctor doctor= doctorOptional.get();

        model.addAttribute("doctor", doctor);

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

        Optional<Doctor> doctorOptional=doctorRepository.findById("10304011");
        Doctor doctor= doctorOptional.get();

        model.addAttribute("doctor", doctor);

        model.addAttribute("listaHorarios", listaHorarios);
        model.addAttribute("listaCitas", optionalCita2);//CAMBIAR POR ID SESION
        model.addAttribute("listaPacientes", optionalCita);//CAMBIAR POR ID SESION

        return "doctor/DoctorDashboard";
    }
    @GetMapping("/historialClinico")
    public String hClinico(Model model, @RequestParam("id") String id) {
        List<Alergia> alergiaList= alergiaRepository.buscarPorPacienteId(id);
        List<TratamientoDTO> tratamientoList=citaRepository.listarTratamientos(id);
        Optional<Paciente> optionalPaciente=pacienteRepository.findById(id);
        List<ListaBuscadorDoctor> listProxCita=citaRepository.listarPorPacienteProxCitas(id);



        if (optionalPaciente.isPresent()) {
            Optional<Doctor> doctorOptional=doctorRepository.findById("10304011");
            Doctor doctor= doctorOptional.get();
            Paciente paciente=optionalPaciente.get();
            model.addAttribute("doctor", doctor);
            model.addAttribute("paciente",paciente);
            model.addAttribute("alergiaList", alergiaList);
            model.addAttribute("ListaTratamiento", tratamientoList);
            model.addAttribute("lisProxCitas", listProxCita);
            return "doctor/DoctorHistorialClinico";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/configuracion")
    public String configuracion(Model model) {

        Optional<Doctor> doctorOptional=doctorRepository.findById("10304011");
        Doctor doctor= doctorOptional.get();

        model.addAttribute("doctor", doctor);
        return "doctor/DoctorConfiguracion";

    }




    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
