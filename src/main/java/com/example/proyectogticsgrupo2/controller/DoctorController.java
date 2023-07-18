package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.dto.*;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.proyectogticsgrupo2.config.SecurityConfig;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequestMapping("/doctor")
@Controller
public class DoctorController {
    final DoctorRepository doctorRepository;
    final PacienteRepository pacienteRepository;
    final CitaRepository citaRepository;
    final SpringSessionRepository springSessionRepository;

    private String idPaciente;



    private int CitaIdLink;
    private int idCita;
    private String fecha;
    private final AlergiaRepository alergiaRepository;
    private final EspecialidadRepository especialidadRepository;
    private final SedeRepository sedeRepository;
    private final HorarioRepository horarioRepository;
    private final CredencialesRepository credencialesRepository;
    private final CuestionarioPorCitaRepository cuestionarioPorCitaRepository;
    private final CuestionarioRepository cuestionarioRepository;
    private final PagoRepository pagoRepository;

    final StylevistasRepository stylevistasRepository;
    final SecurityConfig securityConfig;


    public DoctorController(DoctorRepository doctorRepository, PacienteRepository pacienteRepository, CitaRepository citaRepository,
                            SpringSessionRepository springSessionRepository, AlergiaRepository alergiaRepository,
                            EspecialidadRepository especialidadRepository,
                            SedeRepository sedeRepository, HorarioRepository horarioRepository,
                            CredencialesRepository credencialesRepository, CuestionarioPorCitaRepository cuestionarioPorCitaRepository, CuestionarioRepository cuestionarioRepository, PagoRepository pagoRepository, SecurityConfig securityConfig,StylevistasRepository stylevistasRepository) {

        this.doctorRepository = doctorRepository;
        this.pacienteRepository = pacienteRepository;
        this.citaRepository = citaRepository;
        this.springSessionRepository = springSessionRepository;
        this.alergiaRepository = alergiaRepository;
        this.especialidadRepository = especialidadRepository;
        this.sedeRepository = sedeRepository;
        this.horarioRepository = horarioRepository;
        this.credencialesRepository = credencialesRepository;
        this.cuestionarioPorCitaRepository = cuestionarioPorCitaRepository;
        this.cuestionarioRepository = cuestionarioRepository;
        this.pagoRepository = pagoRepository;
        this.securityConfig = securityConfig;
        this.stylevistasRepository = stylevistasRepository;
    }

    @GetMapping(value = {"/dashboard", "/", ""}) //actual
    public String dashboard(Model model, HttpSession session, Authentication authentication, HttpServletRequest request) {


        Optional<Stylevistas> style = stylevistasRepository.findById(4);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorDoctor", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        // Obtener información del usuario y la sesión
        String usuario = authentication.getName();
        String sesionId = session.getId();

        System.out.println("Usuario: " + usuario);
        System.out.println("Sesión ID: " + sesionId);

        // Obtener roles del usuario
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            System.out.println("Rol: " + authority.getAuthority());
        }

        // Obtener el correo electrónico del doctor a "impersonar" desde la sesión
        String impersonatedUser = (String) session.getAttribute("impersonatedUser");

        // Check if superadmin is logged in as doctor
        Boolean superAdminLogueadoComoDoctor = (Boolean) session.getAttribute("superAdminLogueadoComoDoctor");
        if (superAdminLogueadoComoDoctor == null) {
            superAdminLogueadoComoDoctor = false;
        }
        model.addAttribute("superAdminLogueadoComoDoctor", superAdminLogueadoComoDoctor);

        Doctor doctor;

        if (impersonatedUser != null) {
            // Si hay un usuario "impersonado", buscar al doctor por ese correo electrónico
            doctor = doctorRepository.findByCorreo(impersonatedUser);
        } else {
            doctor = doctorRepository.findByCorreo(authentication.getName());
            if (doctor == null) {
                return "redirect:/error";
            }
        }

        session.setAttribute("doctor", doctor);

        List<ListaBuscadorDoctor> listaCitas = citaRepository.listarPorDoctorProxCitas(doctor.getId_doctor());
        List<ListaBuscadorDoctor> listaPacientes = citaRepository.listarPorDoctorListaPacientes(doctor.getId_doctor());
        List<Cuestionario> listaCuestionarios = cuestionarioRepository.findAll();
        ArrayList<String> listaHorarios = new ArrayList<>();
        List<CuestionarioPorCita> cuestionarioPorCitaList = cuestionarioPorCitaRepository.findAll();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        listaCitas.forEach(cita -> {
            LocalDateTime fechaHora = cita.getInicio();
            String hora1 = fechaHora.format(formatter);

            listaHorarios.add(hora1);
        });

        List<Cita> listaCitaPresencial = new ArrayList<>();
        List<Cita> listaCitaVirtual= new ArrayList<>();
        List<Cita> citasDelDoctor=citaRepository.obtenerCitasPorDoctorId(doctor.getId_doctor(),doctor.getSede().getIdSede());

        for (Cita cita : citasDelDoctor) {

            if (cita.getModalidad()==0){
                listaCitaPresencial.add(cita);
            }else{
                listaCitaVirtual.add(cita);
            }
        }

        StringBuffer path = request.getRequestURL();

        String[] partes = path.toString().split("/");
        String url = partes[0] + "//" + partes[2] + "/" + "doctor";

        model.addAttribute("url", url);
        model.addAttribute("doctor", doctor);
        model.addAttribute("citasPresenciales", listaCitaPresencial);
        model.addAttribute("citasVirtuales", listaCitaVirtual);
        model.addAttribute("cantidadCitasPresenciales", listaCitaPresencial.size());
        model.addAttribute("cantidadCitasVirtuales", listaCitaVirtual.size());
        model.addAttribute("listaCuestionarios", listaCuestionarios);
        model.addAttribute("listaHorarios", listaHorarios);
        model.addAttribute("listaCitas", listaCitas);
        model.addAttribute("listaCitasSize", listaCitas.size());
        model.addAttribute("listaPacientes", listaPacientes);
        model.addAttribute("listaCuestionarioPorCita", cuestionarioPorCitaList);

        return "doctor/DoctorDashboard";
    }

    @GetMapping(value = {"/obtenerSesion"})
    @ResponseBody
    public List<String> obtenerSesion(Model model, HttpSession session, Authentication authentication) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Doctor doctor_session = doctorRepository.findByCorreo(userEmail);
        session.setAttribute("doctor", doctor_session);
        List<String> listaCorreos = new ArrayList<>();

        List<SpringSession> sesiones=springSessionRepository.buscarSesiones();

        for (int i=0; i<sesiones.size(); i++) {
            listaCorreos.add(sesiones.get(i).getPrincipalName());
        }
        //verificar cuando es nulo

        return listaCorreos;
    }


    @GetMapping("/enviarCuestionario")
    public String enviarCuestionario(HttpSession session, Authentication authentication, Model model, @RequestParam("id") int id) {
        /*Doctor doctor_session = doctorRepository.findByCorreo(authentication.getName());*/
        Optional<Stylevistas> style = stylevistasRepository.findById(4);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorDoctor", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Doctor doctor_session = doctorRepository.findByCorreo(userEmail);
        session.setAttribute("doctor", doctor_session);

        Optional<Cita> optionalCita = citaRepository.findById(id);
        List<Cuestionario> listaCuestionarios = cuestionarioRepository.findAll();
        Optional<CuestionarioPorCita> optionalCuestionarioPorCita = cuestionarioPorCitaRepository.findByIdIdCita(id);

        if (optionalCita.isPresent()) {

            if (optionalCuestionarioPorCita.isEmpty()) {
                Cita cita = optionalCita.get();

                model.addAttribute("doctor", doctor_session);
                model.addAttribute("listaCuestionarios", listaCuestionarios);
                model.addAttribute("cita", cita);

                return "doctor/DoctorEnviarCuestionario";

            } else {

                return "redirect:/doctor/dashboard";
            }

        } else {
            return "redirect:/doctor/dashboard";
        }
    }

    @PostMapping("/guardarCuestionario")
    public String guardaCuestionario(HttpSession session, Authentication authentication, Model model, @Valid CuestionarioPorCita cuestionarioPorCita, BindingResult bindingResult) {
        /*Doctor doctor_session = doctorRepository.findByCorreo(authentication.getName());*/
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Doctor doctor_session = doctorRepository.findByCorreo(userEmail);
        session.setAttribute("doctor", doctor_session);


        // Obtener los valores de los campos hidden
        Integer cuestionarioId = cuestionarioPorCita.getCuestionario().getId_cuestionario();
        Integer citaId = cuestionarioPorCita.getCita().getId_cita();

        // Crear instancias de las entidades relacionadas
        Cuestionario cuestionario = new Cuestionario();
        cuestionario.setId_cuestionario(cuestionarioId);

        Cita cita = new Cita();
        cita.setId_cita(citaId);

        // Establecer las relaciones entre las entidades
        cuestionarioPorCita.setCuestionario(cuestionario);
        cuestionarioPorCita.setCita(cita);
        cuestionarioPorCita.getId().setIdCuestionario(cuestionarioId);
        cuestionarioPorCita.getId().setIdCita(citaId);
        cuestionarioPorCita.setEstado(0);
        cuestionarioPorCita.setFecha_enviado(cuestionarioPorCitaRepository.FechaHora());
        cuestionarioPorCita.setR1("·");
        cuestionarioPorCita.setR2("·");
        cuestionarioPorCita.setR3("·");
        cuestionarioPorCita.setR4("·");
        cuestionarioPorCita.setR5("·");
        cuestionarioPorCita.setR6("·");
        cuestionarioPorCita.setR7("·");
        cuestionarioPorCita.setR8("·");
        cuestionarioPorCita.setR9("·");
        cuestionarioPorCita.setR10("·");
        cuestionarioPorCita.setR11("·");
        cuestionarioPorCita.setOpcion_inicio_sesion(0);
        cuestionarioPorCitaRepository.save(cuestionarioPorCita);

        return "redirect:/doctor/dashboard";
    }


    @GetMapping("/recibo")
    public String recibo(Model model, HttpSession session, Authentication authentication) {
        /*Doctor doctor_session = doctorRepository.findByCorreo(authentication.getName());*/
        Optional<Stylevistas> style = stylevistasRepository.findById(4);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorDoctor", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Doctor doctor = doctorRepository.findByCorreo(userEmail);
        session.setAttribute("doctor", doctor);

        List<ListaRecibosDTO> listaRecibos = citaRepository.listarRecibos(doctor.getId_doctor());
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctor.getId_doctor());
        Doctor doctorDeRepositorio = doctorOptional.get();
        model.addAttribute("doctor", doctorDeRepositorio);
        model.addAttribute("listaRecibos", listaRecibos);

        return "doctor/DoctorRecibos";
    }


    @GetMapping("/verRecibo")
    public String verRecibo(Model model, @RequestParam("id") int id_cita, @RequestParam("id2") String id_doctor, HttpSession session, Authentication authentication) {

        /*Doctor doctor_session = doctorRepository.findByCorreo(authentication.getName());*/
        Optional<Stylevistas> style = stylevistasRepository.findById(4);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorDoctor", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Doctor doctor_session = doctorRepository.findByCorreo(userEmail);
        session.setAttribute("doctor", doctor_session);

        Optional<ListaRecibosDTO> optionalListaRecibosDTO = citaRepository.buscarRecibosPorIdCitaIdDoctor(id_doctor, id_cita);
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id_doctor);

        if (optionalDoctor.isPresent() & optionalListaRecibosDTO.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            ListaRecibosDTO listaRecibosDTO = optionalListaRecibosDTO.get();
            Pago pago=pagoRepository.buscarPorCita(id_cita);
            Optional<Cita> cita=citaRepository.findById(id_cita);

            model.addAttribute("doctor", doctor);
            model.addAttribute("cita", listaRecibosDTO);
            model.addAttribute("cita2", cita.get());
            model.addAttribute("pago", pago);

            return "doctor/DoctorVerRecibo";
        } else {
            return "redirect:/doctor/recibo";
        }
    }

    @GetMapping("/calendario")
    public String calendario(Model model, HttpSession session, Authentication authentication, @ModelAttribute("doctor") Doctor doctor) {
        /*Doctor doctor_session = doctorRepository.findByCorreo(authentication.getName());*/
        Optional<Stylevistas> style = stylevistasRepository.findById(4);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorDoctor", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Doctor doctor_session = doctorRepository.findByCorreo(userEmail);
        session.setAttribute("doctor", doctor_session);

        Optional<Doctor> doctorOptional = doctorRepository.findById(doctor_session.getId_doctor());
        doctor = doctorOptional.get();

        List<Cita> citasDelDoctor=citaRepository.obtenerCitasPorDoctorId(doctor_session.getId_doctor(),doctor_session.getSede().getIdSede());
        Doctor buscarHorarioDeDoctor=doctorRepository.buscarHorarioPorDoctorId(doctor_session.getId_doctor());
        Horario horarioDeDoctor = null;
        try {
            horarioDeDoctor = horarioRepository.buscarHorarioPorDoctorId(buscarHorarioDeDoctor.getHorario().getId_horario());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        List<Cita> listaCitaPresencial = new ArrayList<>();
        List<Cita> listaCitaVirtual= new ArrayList<>();

        for (Cita cita : citasDelDoctor) {

            if (cita.getModalidad()==0){
                listaCitaPresencial.add(cita);
            }else{
                listaCitaVirtual.add(cita);
            }
        }

        model.addAttribute("doctor", doctor);
        model.addAttribute("citas", citasDelDoctor);
        model.addAttribute("citasPresenciales", listaCitaPresencial);
        model.addAttribute("citasVirtuales", listaCitaVirtual);
        model.addAttribute("cantidadCitasPresenciales", listaCitaPresencial.size());
        model.addAttribute("cantidadCitasVirtuales", listaCitaVirtual.size());
        model.addAttribute("horario",horarioDeDoctor );
        System.out.println(listaCitaPresencial.size());
        System.out.println(listaCitaVirtual.size());
        return "doctor/DoctorCalendario";
    }

    @PostMapping("/guardarHorario")
    public String guardarHorario(Model model, HttpSession session, Authentication authentication, @ModelAttribute("doctor") @Valid Doctor doctor, BindingResult bindingResult, RedirectAttributes redirectAttributes) {


        /*Doctor doctor_session = doctorRepository.findByCorreo(authentication.getName());*/
        Optional<Stylevistas> style = stylevistasRepository.findById(4);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorDoctor", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Doctor doctor_session = doctorRepository.findByCorreo(userEmail);
        session.setAttribute("doctor", doctor_session);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError error : fieldErrors) {
                String fieldName = error.getField();
                String errorMessage = error.getDefaultMessage();
                System.out.println("Error en el campo " + fieldName + ": " + errorMessage);
            }
            redirectAttributes.addFlashAttribute("error", "Debe de ingresar carácteres válidos");

            return "redirect:/doctor/calendario";
        } else if (doctor.getHorario().getDisponibilidad_inicio_lunes().isAfter(doctor.getHorario().getDisponibilidad_fin_lunes()) ||
                doctor.getHorario().getDisponibilidad_inicio_martes().isAfter(doctor.getHorario().getDisponibilidad_fin_martes()) ||
                doctor.getHorario().getDisponibilidad_inicio_miercoles().isAfter(doctor.getHorario().getDisponibilidad_fin_miercoles()) ||
                doctor.getHorario().getDisponibilidad_inicio_jueves().isAfter(doctor.getHorario().getDisponibilidad_fin_jueves()) ||
                doctor.getHorario().getDisponibilidad_inicio_viernes().isAfter(doctor.getHorario().getDisponibilidad_fin_viernes()) ||
                doctor.getHorario().getDisponibilidad_inicio_sabado().isAfter(doctor.getHorario().getDisponibilidad_fin_sabado())) {
            redirectAttributes.addFlashAttribute("error", "La hora de inicio debe ser menor que la hora de fin");


        } else if (doctor.getHorario().getComida_inicio_lunes().isAfter(doctor.getHorario().getDisponibilidad_fin_lunes()) ||
                doctor.getHorario().getComida_inicio_lunes().isBefore(doctor.getHorario().getDisponibilidad_inicio_lunes()) ||
                doctor.getHorario().getComida_inicio_martes().isAfter(doctor.getHorario().getDisponibilidad_fin_martes()) ||
                doctor.getHorario().getComida_inicio_martes().isBefore(doctor.getHorario().getDisponibilidad_inicio_martes()) ||
                doctor.getHorario().getComida_inicio_miercoles().isAfter(doctor.getHorario().getDisponibilidad_fin_miercoles()) ||
                doctor.getHorario().getComida_inicio_miercoles().isBefore(doctor.getHorario().getDisponibilidad_inicio_miercoles()) ||
                doctor.getHorario().getComida_inicio_jueves().isAfter(doctor.getHorario().getDisponibilidad_fin_jueves()) ||
                doctor.getHorario().getComida_inicio_jueves().isBefore(doctor.getHorario().getDisponibilidad_inicio_jueves()) ||
                doctor.getHorario().getComida_inicio_viernes().isAfter(doctor.getHorario().getDisponibilidad_fin_viernes()) ||
                doctor.getHorario().getComida_inicio_viernes().isBefore(doctor.getHorario().getDisponibilidad_inicio_viernes()) ||
                doctor.getHorario().getComida_inicio_sabado().isAfter(doctor.getHorario().getDisponibilidad_fin_sabado()) ||
                doctor.getHorario().getComida_inicio_sabado().isBefore(doctor.getHorario().getDisponibilidad_inicio_sabado())) {
            redirectAttributes.addFlashAttribute("error", "La hora de comida debe de estar dentro del horario de trabajo");
        } else if (doctor.getHorario().getComida_inicio_lunes().isAfter(doctor.getHorario().getDisponibilidad_fin_lunes().minusHours(1)) ||
                doctor.getHorario().getComida_inicio_martes().isAfter(doctor.getHorario().getDisponibilidad_fin_martes().minusHours(1)) ||
                doctor.getHorario().getComida_inicio_miercoles().isAfter(doctor.getHorario().getDisponibilidad_fin_miercoles().minusHours(1)) ||
                doctor.getHorario().getComida_inicio_jueves().isAfter(doctor.getHorario().getDisponibilidad_fin_jueves().minusHours(1)) ||
                doctor.getHorario().getComida_inicio_viernes().isAfter(doctor.getHorario().getDisponibilidad_fin_viernes().minusHours(1)) ||
                doctor.getHorario().getComida_inicio_sabado().isAfter(doctor.getHorario().getDisponibilidad_fin_sabado().minusHours(1))
        ) {

            redirectAttributes.addFlashAttribute("error", "La hora de comida debe estar al menos una hora antes de la hora de fin");

        } else {

            if (doctor_session.getHorario() == null) {
                doctor_session.setHorario(new Horario());
            }


            doctor_session.getHorario().setDisponibilidad_inicio_lunes(LocalTime.parse(doctor.getHorario().getDisponibilidad_inicio_lunes().format(formatter), formatter));
            doctor_session.getHorario().setDisponibilidad_fin_lunes(LocalTime.parse(doctor.getHorario().getDisponibilidad_fin_lunes().format(formatter), formatter));
            doctor_session.getHorario().setComida_inicio_lunes(LocalTime.parse(doctor.getHorario().getComida_inicio_lunes().format(formatter), formatter));

            doctor_session.getHorario().setDisponibilidad_inicio_martes(LocalTime.parse(doctor.getHorario().getDisponibilidad_inicio_martes().format(formatter), formatter));
            doctor_session.getHorario().setDisponibilidad_fin_martes(LocalTime.parse(doctor.getHorario().getDisponibilidad_fin_martes().format(formatter), formatter));
            doctor_session.getHorario().setComida_inicio_martes(LocalTime.parse(doctor.getHorario().getComida_inicio_martes().format(formatter), formatter));

            doctor_session.getHorario().setDisponibilidad_inicio_miercoles(LocalTime.parse(doctor.getHorario().getDisponibilidad_inicio_miercoles().format(formatter), formatter));
            doctor_session.getHorario().setDisponibilidad_fin_miercoles(LocalTime.parse(doctor.getHorario().getDisponibilidad_fin_miercoles().format(formatter), formatter));
            doctor_session.getHorario().setComida_inicio_miercoles(LocalTime.parse(doctor.getHorario().getComida_inicio_miercoles().format(formatter), formatter));

            doctor_session.getHorario().setDisponibilidad_inicio_jueves(LocalTime.parse(doctor.getHorario().getDisponibilidad_inicio_jueves().format(formatter), formatter));
            doctor_session.getHorario().setDisponibilidad_fin_jueves(LocalTime.parse(doctor.getHorario().getDisponibilidad_fin_jueves().format(formatter), formatter));
            doctor_session.getHorario().setComida_inicio_jueves(LocalTime.parse(doctor.getHorario().getComida_inicio_jueves().format(formatter), formatter));

            doctor_session.getHorario().setDisponibilidad_inicio_viernes(LocalTime.parse(doctor.getHorario().getDisponibilidad_inicio_viernes().format(formatter), formatter));
            doctor_session.getHorario().setDisponibilidad_fin_viernes(LocalTime.parse(doctor.getHorario().getDisponibilidad_fin_viernes().format(formatter), formatter));
            doctor_session.getHorario().setComida_inicio_viernes(LocalTime.parse(doctor.getHorario().getComida_inicio_viernes().format(formatter), formatter));

            doctor_session.getHorario().setDisponibilidad_inicio_sabado(LocalTime.parse(doctor.getHorario().getDisponibilidad_inicio_sabado().format(formatter), formatter));
            doctor_session.getHorario().setDisponibilidad_fin_sabado(LocalTime.parse(doctor.getHorario().getDisponibilidad_fin_sabado().format(formatter), formatter));
            doctor_session.getHorario().setComida_inicio_sabado(LocalTime.parse(doctor.getHorario().getComida_inicio_sabado().format(formatter), formatter));

            Horario horarioGuardado = horarioRepository.save(doctor_session.getHorario());

            doctor_session.setDuracion_cita_minutos(doctor.getDuracion_cita_minutos());
            doctor_session.setHorario(horarioGuardado);

            doctorRepository.save(doctor_session);
            redirectAttributes.addFlashAttribute("success_creado", "Horario creado correctamente");
            redirectAttributes.addFlashAttribute("success_editado", "Horario editado correctamente");
        }

        return "redirect:/doctor/calendario";
    }


    @GetMapping("/reporte")
    public String reporte(Model model, @RequestParam("id") String idPaciente, @RequestParam("id2") int idCita,
                          HttpSession session, Authentication authentication, RedirectAttributes redirectAttributes) {
        setIdPaciente(idPaciente);
        setIdCita(idCita);
        /*Doctor doctor_session = doctorRepository.findByCorreo(authentication.getName());*/
        Optional<Stylevistas> style = stylevistasRepository.findById(4);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorDoctor", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Doctor doctor_session = doctorRepository.findByCorreo(userEmail);
        session.setAttribute("doctor", doctor_session);

        Optional<Pago> verificarPago=pagoRepository.buscarPagoPorIdcita(idCita);
        Optional<Cita> optionalCita = citaRepository.findById(idCita);
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPaciente);
        List<Alergia> alergiaList = alergiaRepository.buscarPorPacienteId(idPaciente);
        //si sale error de verificar pago, añadir a la base de la fila pago de esa cita, ya que siempre estaran presentes
        //las filas de pago de cada cita

        if (optionalPaciente.isPresent() & optionalCita.isPresent() && ((optionalCita.get().getModalidad() == 1 && (optionalCita.get().getEstado()==1 || optionalCita.get().getEstado()==2 || optionalCita.get().getEstado()==3) ) || (optionalCita.get().getModalidad() == 0 && (optionalCita.get().getEstado()==2 ||optionalCita.get().getEstado()==1 || optionalCita.get().getEstado()==5))) && optionalCita.get().getDoctor().getId_doctor() == doctor_session.getId_doctor() && verificarPago.get().getEstadoPago()==1 ) {
            Paciente paciente = optionalPaciente.get();
            Cita cita = optionalCita.get();

            String alergias = "";

            for (int i = 0; i < alergiaList.size(); i++) {
                if (i == alergiaList.size() - 1) {
                    Alergia alergiaIterador = alergiaList.get(i);
                    alergias = alergias + " " + alergiaIterador.getNombre();
                } else {
                    Alergia alergiaIterador = alergiaList.get(i);
                    alergias = alergias + " " + alergiaIterador.getNombre() + ",";
                }
            }

            Optional<Doctor> doctorOptional = doctorRepository.findById(doctor_session.getId_doctor());
            Doctor doctor = doctorOptional.get();

            model.addAttribute("doctor", doctor);
            model.addAttribute("paciente", paciente);
            model.addAttribute("cita", cita);
            model.addAttribute("alergias", alergias);
            model.addAttribute("momentoActual", LocalDateTime.now());

            setCitaIdLink(idCita);

            return "doctor/DoctorReporteSesion";
        } else {
            redirectAttributes.addFlashAttribute("error", "El paciente aún no realiza el pago o se intentó burlar el sistema");
            return "redirect:/doctor/dashboard";
        }

    }

    @PostMapping("/iniciarCita")
    public String iniciarCita(@RequestParam("id_cita") Integer idCita,
                              @RequestParam("idPaciente") String idPaciente,
                              Model model, HttpSession session, Authentication authentication){

        Cita cita = citaRepository.findById(idCita).get();

        LocalDateTime horaFinCita = cita.getFin().plusHours(12);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String horaFinReunion = horaFinCita.format(formatter);

        var apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmFwcGVhci5pbiIsImF1ZCI6Imh0dHBzOi8vYXBpLmFwcGVhci5pbi92MSIsImV4cCI6OTAwNzE5OTI1NDc0MDk5MSwiaWF0IjoxNjg4MDA4NTgxLCJvcmdhbml6YXRpb25JZCI6MTg0MjM1LCJqdGkiOiI3NTYwYmMwOC05ODhmLTRjYTEtYTgyNS1mOTVhOTU0NTM4NTcifQ.jOsnLwuVcqDmAWcgo24rvZgfO5fcDJIIDQiF92ugAzg";
        var data = Map.of(
                "endDate", horaFinReunion,
                "fields", Collections.singletonList("hostRoomUrl")
        );

        try {

            if (cita.getLink() == null){
                var request = HttpRequest.newBuilder(
                                URI.create("https://api.whereby.dev/v1/meetings"))
                        .header("Authorization", "Bearer " + apiKey)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(data)))
                        .build();

                var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println("Status code: " + response.statusCode());
                System.out.println("Body: " + response.body() + "\n");

                Reunion reunion = new Gson().fromJson(response.body(), Reunion.class);

                citaRepository.guardarLink(reunion.getRoomUrl(), cita.getId_cita());
                cita.setLink(reunion.getRoomUrl());
            }

            // Fin de creación o obtencion y guardado de link de reunión ----------

            String userEmail;
            if (session.getAttribute("impersonatedUser") != null) {
                userEmail = (String) session.getAttribute("impersonatedUser");
            } else {
                userEmail = authentication.getName();
            }
            Doctor doctor_session = doctorRepository.findByCorreo(userEmail);
            session.setAttribute("doctor", doctor_session);

            Paciente paciente = pacienteRepository.findById(idPaciente).get();
            List<Alergia> alergiaList = alergiaRepository.buscarPorPacienteId(idPaciente);
            String alergias = "";

            for (int i = 0; i < alergiaList.size(); i++) {
                if (i == alergiaList.size() - 1) {
                    Alergia alergiaIterador = alergiaList.get(i);
                    alergias = alergias + " " + alergiaIterador.getNombre();
                } else {
                    Alergia alergiaIterador = alergiaList.get(i);
                    alergias = alergias + " " + alergiaIterador.getNombre() + ",";
                }
            }

            Optional<Doctor> doctorOptional = doctorRepository.findById(doctor_session.getId_doctor());
            Doctor doctor = doctorOptional.get();

            model.addAttribute("doctor", doctor);
            model.addAttribute("paciente", paciente);
            model.addAttribute("cita", cita);
            model.addAttribute("alergias", alergias);

            citaRepository.actualizarEstadoEnConsulta(cita.getId_cita());

            return "doctor/DoctorCita";
        }
        catch (IOException | InterruptedException e){
            e.printStackTrace();
            return "redirect:/doctor/";
        }
    }
    @PostMapping("/iniciarCitaPresencial")
    public String iniciarCitaPresencial(@RequestParam("id_cita") Integer idCita,
                              @RequestParam("idPaciente") String idPaciente,
                              Model model, HttpSession session, Authentication authentication){

        Cita cita = citaRepository.findById(idCita).get();

        LocalDateTime horaFinCita = cita.getFin().plusHours(12);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String horaFinReunion = horaFinCita.format(formatter);

        try {
            String userEmail;
            if (session.getAttribute("impersonatedUser") != null) {
                userEmail = (String) session.getAttribute("impersonatedUser");
            } else {
                userEmail = authentication.getName();
            }
            Doctor doctor_session = doctorRepository.findByCorreo(userEmail);
            session.setAttribute("doctor", doctor_session);

            Paciente paciente = pacienteRepository.findById(idPaciente).get();
            List<Alergia> alergiaList = alergiaRepository.buscarPorPacienteId(idPaciente);
            String alergias = "";

            for (int i = 0; i < alergiaList.size(); i++) {
                if (i == alergiaList.size() - 1) {
                    Alergia alergiaIterador = alergiaList.get(i);
                    alergias = alergias + " " + alergiaIterador.getNombre();
                } else {
                    Alergia alergiaIterador = alergiaList.get(i);
                    alergias = alergias + " " + alergiaIterador.getNombre() + ",";
                }
            }

            Optional<Doctor> doctorOptional = doctorRepository.findById(doctor_session.getId_doctor());
            Doctor doctor = doctorOptional.get();

            model.addAttribute("doctor", doctor);
            model.addAttribute("paciente", paciente);
            model.addAttribute("cita", cita);
            model.addAttribute("alergias", alergias);

            citaRepository.actualizarEstadoEnConsulta(cita.getId_cita());

            return "doctor/DoctorCitaPresencial";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/doctor/";
        }

    }

    @GetMapping("/verCuestionario")
    public String verCuestionario(Model model, @RequestParam("id") int id, HttpSession session, Authentication authentication) {
        /*Doctor doctor_session = doctorRepository.findByCorreo(authentication.getName());*/
        Optional<Stylevistas> style = stylevistasRepository.findById(4);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorDoctor", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Doctor doctor_session = doctorRepository.findByCorreo(userEmail);        session.setAttribute("doctor", doctor_session);

        Optional<Doctor> doctorOptional = doctorRepository.findById(doctor_session.getId_doctor());
        Optional<CuestionarioxDoctorDTO> cuestionarioxDoctorDTOS = citaRepository.verCuestionario(id);


        if (doctorOptional.isPresent() && cuestionarioxDoctorDTOS.isPresent()) {
            CuestionarioxDoctorDTO lista1 = cuestionarioxDoctorDTOS.orElse(null);
            Doctor doctor = doctorOptional.get();
            model.addAttribute("doctor", doctor);
            model.addAttribute("cuestionario", lista1);

            return "doctor/DoctorVerCuestionario";
        } else {
            return "redirect:/doctor/dashboard";
        }


    }

    @GetMapping("/mensajeria")
    public String mensajeria(Model model, HttpSession session, Authentication authentication) {
        /*Doctor doctor_session = doctorRepository.findByCorreo(authentication.getName());*/
        Optional<Stylevistas> style = stylevistasRepository.findById(4);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorDoctor", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Doctor doctor_session = doctorRepository.findByCorreo(userEmail);
        session.setAttribute("doctor", doctor_session);

        List<ListaBuscadorDoctor> citaList = citaRepository.listarPorDoctorProxCitas(doctor_session.getId_doctor()); //CAMBIAR CON ID DE SESION
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctor_session.getId_doctor());
        Doctor doctor = doctorOptional.get();
        model.addAttribute("doctor", doctor);
        model.addAttribute("listaCitas", citaList);
        return "doctor/DoctorMensajería";
    }

    @PostMapping("/guardarReporte")
    public String guardarReporte(@Validated(Cita.validacion.class) @ModelAttribute("cita") Cita cita, BindingResult bindingResult, @RequestParam("especialidadExamenPendiente") int idEspecExamenPendiente,
                                 HttpSession session, Authentication authentication, Model model) {
        /*Doctor doctor_session = doctorRepository.findByCorreo(authentication.getName());*/
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Doctor doctor_session = doctorRepository.findByCorreo(userEmail);
        session.setAttribute("doctor", doctor_session);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        if (bindingResult.hasErrors()) {
            Optional<Stylevistas> style = stylevistasRepository.findById(4);
            if (style.isPresent()) {
                Stylevistas styleActual = style.get();

                model.addAttribute("headerColorDoctor", styleActual.getHeader());
            } else {
                // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
            }

            Optional<Cita> optionalCita = citaRepository.findById(getIdCita());
            Optional<Paciente> optionalPaciente = pacienteRepository.findById(getIdPaciente());
            Paciente paciente = optionalPaciente.get();
            Cita cita1 = optionalCita.get();

            Optional<Doctor> doctorOptional = doctorRepository.findById(doctor_session.getId_doctor());
            Doctor doctor = doctorOptional.get();
            model.addAttribute("doctor", doctor);

            model.addAttribute("paciente", paciente);
            model.addAttribute("fecha", getFecha());
            model.addAttribute("cita", cita1);
            return "doctor/DoctorCita"; // se podria cambiar a DOctorCita
        } else {

            cita.setEstado(4);
            citaRepository.save(cita); // guardar cita finalizada

            if (idEspecExamenPendiente == 4 || idEspecExamenPendiente == 5 || idEspecExamenPendiente == 6){ // si se le asigno un examen pendiente al paciente

                Cita cita_examen= new Cita(); // creacion de nueva cita para examenes
                cita_examen.setPaciente(cita.getPaciente());

                // doctor seleccionado de manera aleatoria cuando paciente selecciona horario
                // inicio y fin de cita seleccionados por el paciente

                cita_examen.setModalidad(0);
                cita_examen.setEstado(5);
                cita_examen.setSede(cita.getSede()); // cita de examen en la misma sede que la original
                cita_examen.setIdSeguro(cita.getIdSeguro());
                cita_examen.setDiagnostico(cita.getDiagnostico()); // No poner nulo pq si no sale error
                cita_examen.setTratamiento(cita.getTratamiento()); // No poner nulo pq si no sale error
                cita_examen.setReceta(cita.getReceta()); // No poner nulo pq si no sale error
                cita_examen.setCita_previa(cita);
                Especialidad esp = especialidadRepository.findById(idEspecExamenPendiente).get();
                cita_examen.setEspecialidad(esp);
                citaRepository.save(cita_examen);

                //pagoRepository.nuevoPagoDeSoloExamen(citaRepository.obtenerUltimoId());

            }

        }
        return "redirect:/doctor/dashboard";
    }

    @PostMapping("/guardarExamen")
    public String guardarExamen(HttpSession session, Authentication authentication, Model model,
                                @RequestParam("archivo") MultipartFile file,
                                @RequestParam("diagnostico") String diagnostico,
                                @RequestParam("idCita") int idCita,
                                RedirectAttributes attr) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Doctor doctor_session = doctorRepository.findByCorreo(userEmail);
        session.setAttribute("doctor", doctor_session);

        try {
            Optional<Cita> optionalCita = citaRepository.findById(idCita);
            if (optionalCita.isPresent()) {

                Cita cita = optionalCita.get();

                String fileName = file.getOriginalFilename();
                if (fileName.contains("..")) {
                    attr.addFlashAttribute("msgError", "No se permiten '..' en el archivo");
                    model.addAttribute("cita", cita);
                    return "doctor/DoctorReporteSesion";
                }

                // Considerando que file puede ser empty y diagnostico no
                if (diagnostico.equals("")){
                    model.addAttribute("cita", cita);
                    attr.addFlashAttribute("error", "Debe incluir un diagnóstico válido"); // no se por qué no muestra esta validacion :u
                    return "doctor/DoctorReporteSesion";
                }
                else{
                    cita.setExamenname(fileName);
                    cita.setExamencontenttype(file.getContentType());
                    cita.setExamendoc(file.getBytes());

                    cita.setDiagnostico(diagnostico);
                    cita.setTratamiento("-");
                    cita.setReceta("-");

                    cita.setEstado(4); // cita finalizada
                    citaRepository.save(cita);

                    attr.addFlashAttribute("msgActualizacion", "Archivo subido correctamente");

                    // si la cita es un examen pendiente, se debe crear la cita que ofrezca el descuento
                    if (cita.getCita_previa() != null){

                        citaRepository.guardarCitaPendiente(cita.getPaciente().getIdPaciente(), cita.getCita_previa().getDoctor().getId_doctor(), cita.getCita_previa().getModalidad(),
                                cita.getSede().getIdSede(), cita.getCita_previa().getId_cita(), cita.getPaciente().getSeguro().getIdSeguro(),
                                cita.getCita_previa().getEspecialidad().getIdEspecialidad());
                    }
                }
            } else {
                attr.addFlashAttribute("error", "No se encontró la cita");
            }
        } catch (IOException e) {
            e.printStackTrace();
            attr.addFlashAttribute("error", "Ocurrió un error al subir el archivo");
        }

        return "redirect:/doctor/dashboard";
    }


    @GetMapping("/configuracion")
    public String configuracion(Model model, HttpSession session, Authentication authentication) {

        /*Doctor doctor_session = doctorRepository.findByCorreo(authentication.getName());*/
        Optional<Stylevistas> style = stylevistasRepository.findById(4);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorDoctor", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Doctor doctor = doctorRepository.findByCorreo(userEmail);
        session.setAttribute("doctor", doctor);

        model.addAttribute("doctor", doctor);
        List<Sede> sedeList = sedeRepository.findAll();
        model.addAttribute("sedeList", sedeList);

        return "doctor/DoctorConfiguracion";

    }

    @GetMapping("/imageSede")
    public ResponseEntity<byte[]> mostrarImagenSede(@RequestParam("idSede") int idSede) {
        Optional<Sede> optionalSede = sedeRepository.findById(idSede);

        if (optionalSede.isPresent()) {
            Sede sede = optionalSede.get();
            byte[] imagenComoBytes = sede.getFoto();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(
                    MediaType.parseMediaType(sede.getFotocontenttype()));
            return new ResponseEntity<>(
                    imagenComoBytes,
                    httpHeaders,
                    HttpStatus.OK);
        } else {
            return null;
        }
    }

    @GetMapping("/perfil")
    public String perfilDoctor(Model model, HttpSession session, Authentication authentication) {
        /*Doctor doctor_session = doctorRepository.findByCorreo(authentication.getName());*/
        Optional<Stylevistas> style = stylevistasRepository.findById(4);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorDoctor", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Doctor doctor = doctorRepository.findByCorreo(userEmail);
        session.setAttribute("doctor", doctor);

        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctor.getId_doctor());
        if (optionalDoctor.isPresent()) {
            Doctor doctor1 = optionalDoctor.get();
            model.addAttribute("doctor", doctor1);
        }
        return "doctor/DoctorPerfil";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfilDoctor(HttpSession session, Authentication authentication, @ModelAttribute("doctor") Doctor doctor,
                                     @RequestParam(name = "id") String id,
                                     Model model) {

        /*Doctor doctor_session = doctorRepository.findByCorreo(authentication.getName());*/
        Optional<Stylevistas> style = stylevistasRepository.findById(4);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorDoctor", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Doctor doctor1 = doctorRepository.findByCorreo(userEmail);
        session.setAttribute("doctor", doctor1);
/*
        Doctor doctor1 = doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor", doctor1);
*/

        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        if (optionalDoctor.isPresent()) {
            doctor = optionalDoctor.get();
            List<Especialidad> especialidadList = especialidadRepository.findAll();
            model.addAttribute("doctor", doctor1);
            model.addAttribute("especialidadList", especialidadList);
            return "doctor/DoctorPerfilEdit";
        }
        return "redirect:/doctor/perfil";
    }

    @PostMapping("/perfil/guardar")
    public String guardarPerfilDoctor(@ModelAttribute("doctor") @Valid Doctor doctor,
                                      BindingResult bindingResult,
                                      @RequestParam(name = "archivo") MultipartFile file,
                                      RedirectAttributes attr,
                                      Model model, HttpSession session, Authentication authentication) {

        /*Doctor doctor_session = doctorRepository.findByCorreo(authentication.getName());*/
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Doctor doctor_session = doctorRepository.findByCorreo(userEmail);
        session.setAttribute("doctor", doctor_session);

        String fileName = file.getOriginalFilename();

        if (fileName.contains("..") || fileName.contains(" ")) {
            attr.addFlashAttribute("msgError", "El archivo contiene caracteres inválidos");
            return "redirect:/doctor/perfil?id=" + doctor.getId_doctor();
        }
        if (bindingResult.hasErrors()) {
            Optional<Stylevistas> style = stylevistasRepository.findById(4);
            if (style.isPresent()) {
                Stylevistas styleActual = style.get();

                model.addAttribute("headerColorDoctor", styleActual.getHeader());
            } else {
                // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
            }
            model.addAttribute("especialidadList", especialidadRepository.findAll());
            return "doctor/DoctorPerfilEdit";
        } else {
            if (file.isEmpty()) {
                Optional<Doctor> optionalDoctor = doctorRepository.findById(doctor.getId_doctor());
                Doctor d = optionalDoctor.get();
                doctor.setFoto(d.getFoto());
                doctor.setFotoname(d.getFotoname());
                doctor.setFotocontenttype(d.getFotocontenttype());
            } else {
                try {
                    doctor.setFoto(file.getBytes());
                    doctor.setFotoname(fileName);
                    doctor.setFotocontenttype(file.getContentType());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                doctorRepository.save(doctor);
            } catch (Exception e) {
                e.printStackTrace();
                attr.addFlashAttribute("msgError", "No se puede subir la imagen");
                return "redirect:/doctor/perfil?id=" + doctor.getId_doctor();
            }
            attr.addFlashAttribute("msgActualizacion", "Perfil actualizado correctamente");
            return "redirect:/doctor/perfil?id=" + doctor.getId_doctor();
        }
    }


    @GetMapping("/imageDoctor")
    public ResponseEntity<byte[]> mostrarImagenDoctor(@RequestParam("idDoctor") String id) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);

        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            byte[] imagenComoBytes = doctor.getFoto();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(
                    MediaType.parseMediaType(doctor.getFotocontenttype()));
            return new ResponseEntity<>(
                    imagenComoBytes,
                    httpHeaders,
                    HttpStatus.OK);
        } else {
            return null;
        }
    }

    @GetMapping("/perfil/quitarFoto")
    public String quitarFoto(@RequestParam(name = "id") String idDoctor,
                             RedirectAttributes attr) {

        Optional<Doctor> optionalDoctor = doctorRepository.findById(idDoctor);
        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            doctorRepository.quitarFoto(doctor.getId_doctor());
            return "redirect:/doctor/perfil";
        }

        return "redirect:/dctor/perfil";
    }

    @GetMapping("/config/actualizarSede")
    public String guardarSedeDoctor(@RequestParam("idDoctor") String idDoctor, @RequestParam("sedeSeleccionada") int sedeId, RedirectAttributes attr) {

        Optional<Sede> optionalSede = sedeRepository.findById(sedeId);
        if (optionalSede.isPresent()) {
            doctorRepository.actualizarSede(sedeId, idDoctor);
        }
        attr.addFlashAttribute("msgActualizacion", "Sede actualizada correctamente");
        return "redirect:/doctor/configuracion?success";
    }

    @GetMapping("/perfil/cambiarContrasena")
    public String cambiarContrasena(Model model, HttpSession session, Authentication authentication) {

        Optional<Stylevistas> style = stylevistasRepository.findById(4);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorDoctor", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        /*session.setAttribute("doctor", doctorRepository.findByCorreo(authentication.getName()));*/
        // Obtener el correo electrónico del doctor a "impersonar" desde la sesión
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

        // Buscar el doctor por correo electrónico
        Doctor doctor_session = doctorRepository.findByCorreo(userEmail);

        // Set the doctor in the session
        session.setAttribute("doctor", doctor_session);

        return "doctor/perfilContrasena";
    }

    /* @PostMapping("/perfil/guardarContrasena")
     public String guardarContrasena(@RequestParam("actual") String contrasenaActual,
                                     @RequestParam("nueva1") String contrasenaNueva1,
                                     @RequestParam("nueva2") String contrasenaNueva2,
                                     RedirectAttributes attr, Authentication authentication) {

         Doctor doctor = doctorRepository.findByCorreo(authentication.getName());

         PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

         Credenciales credenciales = credencialesRepository.buscarPorId(doctor.getId_doctor());
         Credenciales nuevasCredenciales = new Credenciales(credenciales.getId_credenciales(), credenciales.getCorreo(), passwordEncoder.encode(contrasenaNueva1));

         if (passwordEncoder.matches(contrasenaActual, credenciales.getContrasena())) {
             if (contrasenaNueva1.equals(contrasenaNueva2)) {
                 credencialesRepository.save(nuevasCredenciales);
                 attr.addFlashAttribute("msgActualizacion", "Contraseña actualizada correctamente");
             } else {
                 System.out.println("Contraseñas nuevas no coinciden");
             }
         } else {
             System.out.println("Contraseña actual no coincide");
         }

         return "redirect:/doctor/perfil";
     }*/
    @PostMapping("/perfil/guardarContrasena")
    public String guardarContrasena(@RequestParam("actual") String contrasenaActual,
                                    @RequestParam("nueva1") String contrasenaNueva1,
                                    @RequestParam("nueva2") String contrasenaNueva2,
                                    RedirectAttributes attr,
                                    Authentication authentication,
                                    HttpSession session) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

        Doctor doctor = doctorRepository.findByCorreo(userEmail);

        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        Credenciales credenciales = credencialesRepository.buscarPorId(doctor.getId_doctor());
        Credenciales nuevasCredenciales = new Credenciales(credenciales.getId_credenciales(), credenciales.getCorreo(), passwordEncoder.encode(contrasenaNueva1));

        if (passwordEncoder.matches(contrasenaActual, credenciales.getContrasena())) {
            if (contrasenaNueva1.equals(contrasenaNueva2)) {
                credencialesRepository.save(nuevasCredenciales);
                attr.addFlashAttribute("msgActualizacion", "Contraseña actualizada correctamente");
            } else {
                System.out.println("Contraseñas nuevas no coinciden");
            }
        } else {
            System.out.println("Contraseña actual no coincide");
        }

        return "redirect:/doctor/perfil";
    }


    @GetMapping("/historialClinico")
    public String hClinico(Model model, @RequestParam("id") String id, HttpSession session, Authentication authentication) {
        /*Doctor doctor_session= doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor",doctor_session);*/
        /*Doctor doctor_session = doctorRepository.findByCorreo(authentication.getName());*/
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Doctor doctor_session = doctorRepository.findByCorreo(userEmail);
        session.setAttribute("doctor", doctor_session);

        List<Alergia> alergiaList = alergiaRepository.buscarPorPacienteId(id);
        List<TratamientoDTO> tratamientoList = citaRepository.listarTratamientos(id,4);
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(id);
        List<ListaBuscadorDoctor> listProxCita = citaRepository.listarPorPacienteProxCitas(id);
        List<EncuestaDoctorDTO> fechaEncuesta = citaRepository.listarFechaEncuesta(id,4);
        List<Cita> citaList=citaRepository.listarExamenes(id);

        if (optionalPaciente.isPresent()) {
            Optional<Stylevistas> style = stylevistasRepository.findById(4);
            if (style.isPresent()) {
                Stylevistas styleActual = style.get();

                model.addAttribute("headerColorDoctor", styleActual.getHeader());
            } else {
                // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
            }
            Optional<Doctor> doctorOptional = doctorRepository.findById(doctor_session.getId_doctor());
            Doctor doctor = doctorOptional.get();
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("doctor", doctor);
            model.addAttribute("paciente", paciente);
            model.addAttribute("alergiaList", alergiaList);
            model.addAttribute("ListaTratamiento", tratamientoList);
            model.addAttribute("lisProxCitas", listProxCita);
            model.addAttribute("listCitas",citaList);
            model.addAttribute("listEncuesta",fechaEncuesta);
            return "doctor/DoctorHistorialClinico";
        } else {
            return "redirect:/";
        }
    }
    /*@GetMapping ("/docPaciente/{id}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable("id") int id) {
        Optional<Cita> opt = citaRepository.findById (id);
        if (opt.isPresent()) {
            Cita cita = opt.get();

            byte[] pdfComoBytes = cita.getExamendoc();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType (

                    MediaType. parseMediaType(cita.getExamencontenttype()));

            return new ResponseEntity<> (
                    pdfComoBytes,
                    httpHeaders,
                    HttpStatus. OK) ;
        } else {

            return null;
        }
    }*/
    @GetMapping("/downloadFile/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("id") int id) {
        Optional<Cita> optionalCita = citaRepository.findById(id);

        if (optionalCita.isPresent()) {
            Cita c = optionalCita.get();

            if (c.getExamendoc() != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType(c.getExamencontenttype()));
                headers.setContentDispositionFormData("attachment", c.getExamenname());
                headers.setCacheControl("no-cache, no-store, must-revalidate");
                headers.setPragma("no-cache");
                headers.setExpires(0L);

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(new ByteArrayResource(c.getExamendoc()));
            }
        }

        return ResponseEntity.notFound().build();
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

    public int getCitaIdLink() {
        return CitaIdLink;
    }

    public void setCitaIdLink(int citaIdLink) {
        CitaIdLink = citaIdLink;
    }


}
