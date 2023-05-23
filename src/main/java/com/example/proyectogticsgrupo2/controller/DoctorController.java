package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.dto.*;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
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

import javax.print.Doc;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    private final EspecialidadRepository especialidadRepository;




    public DoctorController(DoctorRepository doctorRepository, PacienteRepository pacienteRepository, CitaRepository citaRepository,
                            AlergiaRepository alergiaRepository,
                            EspecialidadRepository especialidadRepository) {
        this.doctorRepository = doctorRepository;
        this.pacienteRepository = pacienteRepository;
        this.citaRepository = citaRepository;
        this.alergiaRepository = alergiaRepository;
        this.especialidadRepository = especialidadRepository;
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
    public String calendario(Model model){

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
        List<Alergia> alergiaList= alergiaRepository.buscarPorPacienteId(id);



        if (optionalPaciente.isPresent() & optionalCita.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            Cita cita = optionalCita.get();


            String alergias="";

            for (int i = 0; i < alergiaList.size(); i++) {
                if (i == alergiaList.size() - 1) {
                    Alergia alergiaIterador = alergiaList.get(i);
                    alergias = alergias +" "+alergiaIterador.getNombre();
                } else {
                    Alergia alergiaIterador = alergiaList.get(i);
                    alergias = alergias +" "+ alergiaIterador.getNombre() + ",";
                }
            }

            System.out.println(alergias);


            Optional<Doctor> doctorOptional=doctorRepository.findById("10304011");
            Doctor doctor= doctorOptional.get();

            model.addAttribute("doctor", doctor);
            model.addAttribute("paciente",paciente);
            model.addAttribute("cita",cita);
            model.addAttribute("alergias",alergias);

            return "doctor/DoctorReporteSesion";
        } else {
            return "redirect:/doctor/dashboard";
        }

    }

    @GetMapping("/verCuestionario")
    public String verCuestionario(Model model, @RequestParam("id") int id){
        Optional<Doctor> doctorOptional=doctorRepository.findById("10304011");
        Optional<CuestionarioxDoctorDTO> cuestionarioxDoctorDTOS=citaRepository.verCuestionario(id);



        if (doctorOptional.isPresent() && cuestionarioxDoctorDTOS.isPresent()  ) {
            CuestionarioxDoctorDTO lista1 = cuestionarioxDoctorDTOS.orElse(null);
            Doctor doctor= doctorOptional.get();
            model.addAttribute("doctor", doctor);
            model.addAttribute("cuestionario", lista1);

            return "doctor/DoctorVerCuestionario";
        } else {
            return "redirect:/doctor/dashboard";
        }



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
            cita.setEstado(4);
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
            List<Especialidad> especialidadList=especialidadRepository.findAll();
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
                return "redirect:/doctor/perfil"+ doctor.getId_doctor();
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
