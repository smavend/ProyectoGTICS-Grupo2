package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.dto.*;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.proyectogticsgrupo2.config.SecurityConfig;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    private String idPaciente;
    private int idCita;
    private String fecha;
    private final AlergiaRepository alergiaRepository;
    private final EspecialidadRepository especialidadRepository;
    private final SedeRepository sedeRepository;
    private final HorarioRepository horarioRepository;
    private final CredencialesRepository credencialesRepository;
    private final CuestionarioPorCitaRepository cuestionarioPorCitaRepository;
    private final CuestionarioRepository cuestionarioRepository;

    final SecurityConfig securityConfig;



    public DoctorController(DoctorRepository doctorRepository, PacienteRepository pacienteRepository, CitaRepository citaRepository,
                            AlergiaRepository alergiaRepository,
                            EspecialidadRepository especialidadRepository,
                            SedeRepository sedeRepository, HorarioRepository horarioRepository,
                            CredencialesRepository credencialesRepository, CuestionarioPorCitaRepository cuestionarioPorCitaRepository, CuestionarioRepository cuestionarioRepository, SecurityConfig securityConfig) {
        this.doctorRepository = doctorRepository;
        this.pacienteRepository = pacienteRepository;
        this.citaRepository = citaRepository;
        this.alergiaRepository = alergiaRepository;
        this.especialidadRepository = especialidadRepository;
        this.sedeRepository = sedeRepository;
        this.horarioRepository = horarioRepository;
        this.credencialesRepository = credencialesRepository;
        this.cuestionarioPorCitaRepository = cuestionarioPorCitaRepository;
        this.cuestionarioRepository = cuestionarioRepository;
        this.securityConfig = securityConfig;
    }

    @GetMapping(value = {"/dashboard", "/", ""})
    public String dashboard(Model model, HttpSession session, Authentication authentication) {


        Doctor doctor= doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor",doctor);

        List<ListaBuscadorDoctor> optionalCita = citaRepository.listarPorDoctorProxCitas(doctor.getId_doctor()); //CAMBIAR POR ID SESION
        List<ListaBuscadorDoctor> optionalCita2 = citaRepository.listarPorDoctorListaPacientes(doctor.getId_doctor()); //CAMBIAR POR ID SESION
        List<Cuestionario> listaCuestionarios= cuestionarioRepository.findAll();
        ArrayList<String> listaHorarios = new ArrayList<>();
        List<CuestionarioPorCita> cuestionarioPorCitaList=cuestionarioPorCitaRepository.findAll();

        // Transformar LocalDateTime a LocalDate
        optionalCita.forEach(cita -> {
            LocalDateTime fechaHora = cita.getInicio(); // Obtener LocalDateTime
            String hora1 = fechaHora.toLocalTime().toString();

            LocalDateTime fechaHora2 = cita.getFin();
            String hora2 = fechaHora2.toLocalTime().toString();
            // Transformar a LocalDate
            // Actualizar objeto ListaBuscadorDoctor con la fecha

            String horaFinal = hora1 + " - " + hora2;
            listaHorarios.add(horaFinal);
        });


        model.addAttribute("doctor", doctor);
        model.addAttribute("listaCuestionarios", listaCuestionarios);
        model.addAttribute("listaHorarios", listaHorarios);
        model.addAttribute("listaCitas", optionalCita);
        model.addAttribute("listaPacientes", optionalCita2);
        model.addAttribute("listaCuestionarioPorCita", cuestionarioPorCitaList);


        return "doctor/DoctorDashboard";
    }

    @GetMapping("/enviarCuestionario")
    public String enviarCuestionario(HttpSession session, Authentication authentication, Model model,@RequestParam("id") int id) {
        Doctor doctor_session= doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor",doctor_session);

        Optional<Cita> optionalCita = citaRepository.findById(id);
        List<Cuestionario> listaCuestionarios= cuestionarioRepository.findAll();
        Optional<CuestionarioPorCita> optionalCuestionarioPorCita = cuestionarioPorCitaRepository.findByIdIdCita(id);

        if (optionalCita.isPresent()) {

            if (optionalCuestionarioPorCita.isEmpty()){
                Cita cita = optionalCita.get();

                model.addAttribute("doctor", doctor_session);
                model.addAttribute("listaCuestionarios", listaCuestionarios);
                model.addAttribute("cita", cita);

                return "doctor/DoctorEnviarCuestionario";

            }else{

                return "redirect:/doctor/dashboard";
            }

        } else {
            return "redirect:/doctor/dashboard";
        }
    }

    @PostMapping("/guardarCuestionario")
    public String guardaCuestionario(HttpSession session, Authentication authentication, Model model, @Valid CuestionarioPorCita cuestionarioPorCita, BindingResult bindingResult) {
        Doctor doctor_session = doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor", doctor_session);

        if (bindingResult.hasErrors()) {
            return "redirect:/doctor/dashboard";
        } else {
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

            cuestionarioPorCitaRepository.save(cuestionarioPorCita);

            return "redirect:/doctor/dashboard";
        }
    }



    @GetMapping("/recibo")
    public String recibo(Model model, HttpSession session, Authentication authentication) {
        Doctor doctor= doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor",doctor);

        List<ListaRecibosDTO> optionalCita = citaRepository.listarRecibos(doctor.getId_doctor());
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctor.getId_doctor());
        Doctor doctorDeRepositorio = doctorOptional.get();
        model.addAttribute("doctor", doctorDeRepositorio);
        model.addAttribute("listaRecibos", optionalCita);

        return "doctor/DoctorRecibos";
    }


    @GetMapping("/verRecibo")
    public String verRecibo(Model model, @RequestParam("id") int id_cita, @RequestParam("id2") String id_doctor,HttpSession session, Authentication authentication) {

        Doctor doctor_session= doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor",doctor_session);

        Optional<ListaRecibosDTO> optionalListaRecibosDTO = citaRepository.buscarRecibosPorIdCitaIdDoctor(id_doctor, id_cita);
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id_doctor);

        if (optionalDoctor.isPresent() & optionalListaRecibosDTO.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            ListaRecibosDTO listaRecibosDTO = optionalListaRecibosDTO.get();
            model.addAttribute("doctor", doctor);
            model.addAttribute("cita", listaRecibosDTO);

            return "doctor/DoctorVerRecibo";
        } else {
            return "redirect:/doctor/recibo";
        }


    }

    @GetMapping("/calendario")
    public String calendario(Model model, HttpSession session, Authentication authentication, @ModelAttribute("doctor") Doctor doctor) {
        Doctor doctor_session= doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor",doctor_session);

        Optional<Doctor> doctorOptional = doctorRepository.findById(doctor_session.getId_doctor());
        doctor = doctorOptional.get();

        model.addAttribute("doctor", doctor);

        return "doctor/DoctorCalendario";
    }

    @PostMapping("/guardarHorario")
    public String guardarHorario(HttpSession session, Authentication authentication, @ModelAttribute("doctor") @Valid Doctor doctor, BindingResult bindingResult,RedirectAttributes redirectAttributes) {


        Doctor doctor_session= doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor",doctor_session);
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
        }else if (doctor.getHorario().getDisponibilidad_inicio_lunes().isAfter(doctor.getHorario().getDisponibilidad_fin_lunes()) ||
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
                    doctor.getHorario().getComida_inicio_sabado().isBefore(doctor.getHorario().getDisponibilidad_inicio_sabado()) )  {
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
    public String reporte(Model model, @RequestParam("id") String id, @RequestParam("id2") int id2, HttpSession session, Authentication authentication) {
        setIdPaciente(id);
        setIdCita(id2);
        Doctor doctor_session= doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor",doctor_session);

        Optional<Cita> optionalCita = citaRepository.findById(id2);
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(id);
        List<Alergia> alergiaList = alergiaRepository.buscarPorPacienteId(id);
        System.out.println(optionalCita.get().getModalidad());

        if (optionalPaciente.isPresent() & optionalCita.isPresent() && optionalCita.get().getModalidad()==1 && optionalCita.get().getDoctor().getId_doctor()==doctor_session.getId_doctor()) {
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

            System.out.println(alergias);


            Optional<Doctor> doctorOptional = doctorRepository.findById(doctor_session.getId_doctor());
            Doctor doctor = doctorOptional.get();

            model.addAttribute("doctor", doctor);
            model.addAttribute("paciente", paciente);
            model.addAttribute("cita", cita);
            model.addAttribute("alergias", alergias);

            return "doctor/DoctorReporteSesion";
        } else {
            return "redirect:/doctor/dashboard";
        }

    }

    @GetMapping("/verCuestionario")
    public String verCuestionario(Model model, @RequestParam("id") int id, HttpSession session, Authentication authentication) {
        Doctor doctor_session= doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor",doctor_session);

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
        Doctor doctor_session= doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor",doctor_session);

        List<ListaBuscadorDoctor> citaList = citaRepository.listarPorDoctorProxCitas(doctor_session.getId_doctor()); //CAMBIAR CON ID DE SESION
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctor_session.getId_doctor());
        Doctor doctor = doctorOptional.get();
        model.addAttribute("doctor", doctor);
        model.addAttribute("listaCitas", citaList);
        return "doctor/DoctorMensajería";
    }

    @PostMapping("/guardarReporte")
    public String guardarReporte(HttpSession session, Authentication authentication, Model model, @Valid Cita cita, BindingResult bindingResult) {
        Doctor doctor_session= doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor",doctor_session);

        if (bindingResult.hasErrors()) {

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
            return "doctor/DoctorReporteSesion";
        } else {
            cita.setEstado(4);
            citaRepository.save(cita);
            return "redirect:/doctor/dashboard";
        }
    }
    @GetMapping("/historialClinico")
    public String hClinico(Model model, @RequestParam("id") String id, HttpSession session, Authentication authentication) {
        Doctor doctor_session= doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor",doctor_session);

        List<Alergia> alergiaList = alergiaRepository.buscarPorPacienteId(id);
        List<TratamientoDTO> tratamientoList = citaRepository.listarTratamientos(id,4);
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(id);
        List<ListaBuscadorDoctor> listProxCita = citaRepository.listarPorPacienteProxCitas(id);
        List<EncuestaDoctorDTO> fechaEncuesta = citaRepository.listarFechaEncuesta(id,4);

        if (optionalPaciente.isPresent()) {
            Optional<Doctor> doctorOptional = doctorRepository.findById(doctor_session.getId_doctor());
            Doctor doctor = doctorOptional.get();
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("doctor", doctor);
            model.addAttribute("paciente", paciente);
            model.addAttribute("alergiaList", alergiaList);
            model.addAttribute("ListaTratamiento", tratamientoList);
            model.addAttribute("lisProxCitas", listProxCita);
            model.addAttribute("listEncuesta",fechaEncuesta);
            return "doctor/DoctorHistorialClinico";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/configuracion")
    public String configuracion(Model model, HttpSession session, Authentication authentication) {

        Doctor doctor= doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor",doctor);

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
        Doctor doctor= doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor",doctor);

        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctor.getId_doctor());
        if (optionalDoctor.isPresent()) {
            Doctor doctor1 = optionalDoctor.get();
            model.addAttribute("doctor", doctor1);
        }
        return "doctor/DoctorPerfil";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfilDoctor( HttpSession session, Authentication authentication,@ModelAttribute("doctor") Doctor doctor,
                                     @RequestParam(name = "id") String id,
                                     Model model) {

        Doctor doctor_session= doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor",doctor_session);

        Doctor doctor1= doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor",doctor1);

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
                                      Model model,HttpSession session, Authentication authentication) {

        Doctor doctor_session= doctorRepository.findByCorreo(authentication.getName());
        session.setAttribute("doctor",doctor_session);

        String fileName = file.getOriginalFilename();

        if (fileName.contains("..") || fileName.contains(" ")) {
            attr.addFlashAttribute("msgError", "El archivo contiene caracteres inválidos");
            return "redirect:/doctor/perfil/editar?id=" + doctor.getId_doctor();
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("especialidadList", especialidadRepository.findAll());
            return "doctor/DoctorPerfilEdit";
        } else {
            try {
                if (file.isEmpty()) {
                    Optional<Doctor> optionalDoctor = doctorRepository.findById(doctor.getId_doctor());
                    Doctor d = optionalDoctor.get();
                    doctor.setFoto(d.getFoto());
                    doctor.setFotoname(d.getFotoname());
                    doctor.setFotocontenttype(d.getFotocontenttype());
                } else {
                    doctor.setFoto(file.getBytes());
                    doctor.setFotoname(fileName);
                    doctor.setFotocontenttype(file.getContentType());
                }
                try {
                    doctorRepository.save(doctor);
                } catch (Exception e){
                    e.printStackTrace();
                    attr.addFlashAttribute("msgError", "No se puede subir la imagen");
                    return "redirect:/doctor/perfilid="+ doctor.getId_doctor();
                }



                attr.addFlashAttribute("msgActualizacion", "Perfil actualizado correctamente");
                return "redirect:/doctor/perfil?id=" + doctor.getId_doctor();

            } catch (IOException e) {
                e.printStackTrace();
                attr.addFlashAttribute("msgError", "Ocurrió un error al subir el archivo");
                return "redirect:/doctor/perfil" + doctor.getId_doctor();
            }

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
    public String cambiarContrasena(HttpSession session, Authentication authentication) {

        session.setAttribute("doctor", doctorRepository.findByCorreo(authentication.getName()));


        return "doctor/perfilContrasena";
    }

    @PostMapping("/perfil/guardarContrasena")
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
