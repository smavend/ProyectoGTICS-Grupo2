package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.dto.*;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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


    public DoctorController(DoctorRepository doctorRepository, PacienteRepository pacienteRepository, CitaRepository citaRepository,
                            AlergiaRepository alergiaRepository,
                            EspecialidadRepository especialidadRepository,
                            SedeRepository sedeRepository, HorarioRepository horarioRepository) {
        this.doctorRepository = doctorRepository;
        this.pacienteRepository = pacienteRepository;
        this.citaRepository = citaRepository;
        this.alergiaRepository = alergiaRepository;
        this.especialidadRepository = especialidadRepository;
        this.sedeRepository = sedeRepository;
        this.horarioRepository = horarioRepository;
    }

 /*   @GetMapping(value = {"/dashboard", "/", ""})
    public String dashboard(Model model, HttpSession session) {

        Doctor doctor_session = (Doctor) session.getAttribute("doctor");

        List<ListaBuscadorDoctor> optionalCita = citaRepository.listarPorDoctorProxCitas(doctor_session.getId_doctor()); //CAMBIAR POR ID SESION
        System.out.println("SI ENTRA");
        List<ListaBuscadorDoctor> optionalCita2 = citaRepository.listarPorDoctorListaPacientes(doctor_session.getId_doctor()); //CAMBIAR POR ID SESION
        ArrayList<String> listaHorarios = new ArrayList<>();
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctor_session.getId_doctor());
        Doctor doctor = doctorOptional.get();


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
        model.addAttribute("listaHorarios", listaHorarios);
        model.addAttribute("listaCitas", optionalCita);
        model.addAttribute("listaPacientes", optionalCita2);


        return "doctor/DoctorDashboard";
    }*/
 @GetMapping(value = {"/dashboard", "/", ""})
 public String dashboard(@RequestParam(value = "id", required = false) String doctorId, Model model, HttpSession session) {
     Doctor doctor_session = (Doctor) session.getAttribute("doctor");

     // Si no se proporcionó un doctorId en la URL, usa el id del doctor en la sesión.
     if (doctorId == null && doctor_session != null) {
         doctorId = doctor_session.getId_doctor();
     }

     // Si aún no se ha establecido doctorId (porque no se proporcionó en la URL y no hay doctor en la sesión), redirige a una página de error o realiza otra acción apropiada.
     if (doctorId == null) {
         return "error-page"; // Cambia esto según tus necesidades
     }

     // Verifica si el doctor de la sesión coincide con el doctor seleccionado
     if (doctor_session != null && doctor_session.getId_doctor().equals(doctorId)) {
         // Realiza las operaciones necesarias para mostrar el dashboard del doctor seleccionado
         List<ListaBuscadorDoctor> optionalCita = citaRepository.listarPorDoctorProxCitas(doctorId);
         List<ListaBuscadorDoctor> optionalCita2 = citaRepository.listarPorDoctorListaPacientes(doctorId);
         ArrayList<String> listaHorarios = new ArrayList<>();
         Optional<Doctor> doctorOptional = doctorRepository.findById(doctorId);

         if (doctorOptional.isPresent()) {
             Doctor doctor = doctorOptional.get();

             // Transformar LocalDateTime a LocalDate
             optionalCita.forEach(cita -> {
                 LocalDateTime fechaHora = cita.getInicio();
                 String hora1 = fechaHora.toLocalTime().toString();

                 LocalDateTime fechaHora2 = cita.getFin();
                 String hora2 = fechaHora2.toLocalTime().toString();

                 String horaFinal = hora1 + " - " + hora2;
                 listaHorarios.add(horaFinal);
             });

             model.addAttribute("doctor", doctor);
             model.addAttribute("listaHorarios", listaHorarios);
             model.addAttribute("listaCitas", optionalCita);
             model.addAttribute("listaPacientes", optionalCita2);

             return "doctor/DoctorDashboard";
         }
     }

     // Si el doctor seleccionado no coincide con el doctor de la sesión o no se encuentra en la base de datos, redirige a una página de error o realiza otra acción apropiada.
     return "error-page";
 }


    @GetMapping("/recibo")
    public String recibo(Model model, HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        Doctor doctor_session = (Doctor) httpSession.getAttribute("doctor");

        List<ListaRecibosDTO> optionalCita = citaRepository.listarRecibos(doctor_session.getId_doctor());
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctor_session.getId_doctor());
        Doctor doctor = doctorOptional.get();
        model.addAttribute("doctor", doctor);
        model.addAttribute("listaRecibos", optionalCita);//CAMBIAR POR ID SESION

        return "doctor/DoctorRecibos";
    }


    @GetMapping("/verRecibo")
    public String verRecibo(Model model, @RequestParam("id") int id_cita, @RequestParam("id2") String id_doctor) {

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
    public String calendario(Model model, HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        Doctor doctor_session = (Doctor) httpSession.getAttribute("doctor");

        Optional<Doctor> doctorOptional = doctorRepository.findById(doctor_session.getId_doctor());
        Doctor doctor = doctorOptional.get();

        model.addAttribute("doctor", doctor);

        return "doctor/DoctorCalendario";
    }

    @PostMapping("/guardarHorario")
    public String guardarHorario(HttpServletRequest request, Model model, Horario horario) {
        //Poner @valid
        HttpSession httpSession = request.getSession();
        Doctor doctor_session = (Doctor) httpSession.getAttribute("doctor");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        horario.setDisponibilidad_inicio(LocalTime.parse(request.getParameter("disponibilidad_inicio"), formatter));
        horario.setDisponibilidad_fin(LocalTime.parse(request.getParameter("disponibilidad_fin"), formatter));
        horario.setComida_inicio(LocalTime.parse(request.getParameter("comida_inicio"), formatter));

        // Guardar el objeto Horario antes de asignarlo al Doctor
        Horario horarioGuardado = horarioRepository.save(horario);

        doctor_session.setDuracion_cita_minutos(Integer.parseInt(request.getParameter("duracion_cita_minutos")));
        doctor_session.setHorario(horarioGuardado);

        doctorRepository.save(doctor_session);

        return "redirect:/doctor/calendario";


    }

    @GetMapping("/reporte")
    public String reporte(Model model, @RequestParam("id") String id, @RequestParam("id2") int id2, HttpServletRequest request) {
        setIdPaciente(id);
        setIdCita(id2);
        HttpSession httpSession = request.getSession();
        Doctor doctor_session = (Doctor) httpSession.getAttribute("doctor");

        Optional<Cita> optionalCita = citaRepository.findById(id2);
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(id);
        List<Alergia> alergiaList = alergiaRepository.buscarPorPacienteId(id);


        if (optionalPaciente.isPresent() & optionalCita.isPresent()) {
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
    public String verCuestionario(Model model, @RequestParam("id") int id, HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        Doctor doctor_session = (Doctor) httpSession.getAttribute("doctor");

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
    public String mensajeria(Model model, HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        Doctor doctor_session = (Doctor) httpSession.getAttribute("doctor");

        List<ListaBuscadorDoctor> citaList = citaRepository.listarPorDoctorProxCitas(doctor_session.getId_doctor()); //CAMBIAR CON ID DE SESION
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctor_session.getId_doctor());
        Doctor doctor = doctorOptional.get();
        model.addAttribute("doctor", doctor);
        model.addAttribute("listaCitas", citaList);
        return "doctor/DoctorMensajería";
    }

    @PostMapping("/guardarReporte")
    public String guardarReporte(HttpServletRequest request, Model model, @Valid Cita cita, BindingResult bindingResult) {
        HttpSession httpSession = request.getSession();
        Doctor doctor_session = (Doctor) httpSession.getAttribute("doctor");

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
    public String hClinico(Model model, @RequestParam("id") String id, HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        Doctor doctor_session = (Doctor) httpSession.getAttribute("doctor");

        List<Alergia> alergiaList = alergiaRepository.buscarPorPacienteId(id);
        List<TratamientoDTO> tratamientoList = citaRepository.listarTratamientos(id);
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(id);
        List<ListaBuscadorDoctor> listProxCita = citaRepository.listarPorPacienteProxCitas(id);


        if (optionalPaciente.isPresent()) {
            Optional<Doctor> doctorOptional = doctorRepository.findById(doctor_session.getId_doctor());
            Doctor doctor = doctorOptional.get();
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("doctor", doctor);
            model.addAttribute("paciente", paciente);
            model.addAttribute("alergiaList", alergiaList);
            model.addAttribute("ListaTratamiento", tratamientoList);
            model.addAttribute("lisProxCitas", listProxCita);
            return "doctor/DoctorHistorialClinico";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/configuracion")
    public String configuracion(Model model, HttpServletRequest request) {

        HttpSession httpSession = request.getSession();
        Doctor doctor_session = (Doctor) httpSession.getAttribute("doctor");

        Optional<Doctor> doctorOptional = doctorRepository.findById(doctor_session.getId_doctor());
        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            model.addAttribute("doctor", doctor);
            List<Sede> sedeList = sedeRepository.findAll();
            model.addAttribute("sedeList", sedeList);
        }
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
    public String perfilDoctor(Model model, @RequestParam("id") String id) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            model.addAttribute("doctor", doctor);
        }
        return "doctor/DoctorPerfil";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfilDoctor(@ModelAttribute("doctor") Doctor doctor,
                                     @RequestParam(name = "id") String id,
                                     Model model) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        if (optionalDoctor.isPresent()) {
            doctor = optionalDoctor.get();
            List<Especialidad> especialidadList = especialidadRepository.findAll();
            model.addAttribute("doctor", doctor);
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
                                      Model model) {

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
                doctorRepository.save(doctor);
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
    public String quitarFotoDoctor(@RequestParam(name = "idDocF") String id,
                                   RedirectAttributes attr) {

        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            try {
                File foto = new File("src/main/resources/static/assets/img/userPorDefecto.jpg");
                FileInputStream input = new FileInputStream(foto);
                byte[] bytes;
                try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = input.read(buffer)) != -1) {
                        output.write(buffer, 0, length);
                    }
                    input.close();
                    output.close();
                    bytes = output.toByteArray();
                }

                doctor.setFoto(bytes);
                doctor.setFotoname("userPorDefecto.jpg");
                doctor.setFotocontenttype("image/jpg");

                doctorRepository.save(doctor);
                return "redirect:/doctor/perfil";
            } catch (IOException e) {
                e.printStackTrace();
                attr.addFlashAttribute("msgError", "Ocurrió un error al subir el archivo");
                return "redirect:/doctor/perfil";
            }
        }

        return "redirect:/doctor/perfil";
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
