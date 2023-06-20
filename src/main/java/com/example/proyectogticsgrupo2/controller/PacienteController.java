package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.config.SecurityConfig;
import com.example.proyectogticsgrupo2.dto.HorarioOcupadoDTO;
import com.example.proyectogticsgrupo2.dto.TorreYPisoDTO;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/Paciente")
public class PacienteController {

    final PacienteRepository pacienteRepository;
    final SedeRepository sedeRepository;
    final EspecialidadRepository especialidadRepository;
    final AlergiaRepository alergiaRepository;
    final SeguroRepository seguroRepository;
    final DistritoRepository distritoRepository;
    final DoctorRepository doctorRepository;
    final PacientePorConsentimientoRepository pacientePorConsentimientoRepository;
    final CitaRepository citaRepository;
    final CitaTemporalRepository citaTemporalRepository;
    final PagoRepository pagoRepository;
    final HorarioRepository horarioRepository;
    final CredencialesRepository credencialesRepository;
    final CuestionarioPorCitaRepository cuestionarioPorCitaRepository;
    final CuestionarioRepository cuestionarioRepository;
    final AdministrativoPorEspecialidadPorSedeRepository administrativoPorEspecialidadPorSedeRepository;
    final SecurityConfig securityConfig;

    public PacienteController(PacienteRepository pacienteRepository, EspecialidadRepository especialidadRepository,
                              SedeRepository sedeRepository, AlergiaRepository alergiaRepository, SeguroRepository seguroRepository,
                              DistritoRepository distritoRepository, DoctorRepository doctorRepository,
                              PacientePorConsentimientoRepository pacientePorConsentimientoRepository, CitaRepository citaRepository,
                              PagoRepository pagoRepository, CitaTemporalRepository citaTemporalRepository,
                              HorarioRepository horarioRepository, CredencialesRepository credencialesRepository,
                              CuestionarioPorCitaRepository cuestionarioPorCitaRepository, CuestionarioRepository cuestionarioRepository,
                              AdministrativoPorEspecialidadPorSedeRepository administrativoPorEspecialidadPorSedeRepository,
                              SecurityConfig securityConfig) {

        this.pacienteRepository = pacienteRepository;
        this.especialidadRepository = especialidadRepository;
        this.sedeRepository = sedeRepository;
        this.alergiaRepository = alergiaRepository;
        this.seguroRepository = seguroRepository;
        this.distritoRepository = distritoRepository;
        this.doctorRepository = doctorRepository;
        this.pacientePorConsentimientoRepository = pacientePorConsentimientoRepository;
        this.citaRepository = citaRepository;
        this.citaTemporalRepository = citaTemporalRepository;
        this.pagoRepository = pagoRepository;
        this.horarioRepository = horarioRepository;
        this.credencialesRepository = credencialesRepository;
        this.cuestionarioPorCitaRepository = cuestionarioPorCitaRepository;
        this.cuestionarioRepository = cuestionarioRepository;
        this.administrativoPorEspecialidadPorSedeRepository = administrativoPorEspecialidadPorSedeRepository;
        this.securityConfig = securityConfig;
    }

    /* INICIO */
    @GetMapping(value = {"", "/", "/index"})
    public String index(Model model, Authentication authentication, HttpSession session) {
        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));

        List<Sede> sedeList = sedeRepository.findAll();
        model.addAttribute("sedeList", sedeList);

        return "paciente/index";
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

    /* RESERVAR CITA */
    @GetMapping("/reservar")
    public String reservarGet(@ModelAttribute("citaTemporal") CitaTemporal citaTemporal,
                              Model model, HttpSession session, Authentication authentication) {

        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));

        model.addAttribute("sedeList", sedeRepository.findAll());
        model.addAttribute("especialidadList", especialidadRepository.findAll());

        return "paciente/reservar1";
    }

    @PostMapping("/reservar1")
    public String reservar1Post(@ModelAttribute("citaTemporal") @Valid CitaTemporal citaTemporal, BindingResult bindingResult,
                                Model model, HttpSession session, Authentication authentication) {

        List<Doctor> doctoresDisponibles;
        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));

        if (bindingResult.hasFieldErrors("modalidad") || bindingResult.hasFieldErrors("idSede") || bindingResult.hasFieldErrors("idEspecialidad")) {

            model.addAttribute("sedeList", sedeRepository.findAll());
            model.addAttribute("especialidadList", especialidadRepository.findAll());
            return "paciente/reservar1";

        } else {

            doctoresDisponibles = buscarDoctores(citaTemporal.getModalidad(), citaTemporal.getIdSede(), citaTemporal.getIdEspecialidad());
            model.addAttribute("doctoresDisponibles", doctoresDisponibles);

            return "paciente/reservar2";
        }
    }

    @PostMapping("/reservar2")
    public String reservar2Post(@ModelAttribute("citaTemporal") @Valid CitaTemporal citaTemporal, BindingResult bindingResult,
                                Model model, HttpSession session, Authentication authentication) {

        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));

        if (bindingResult.hasErrors()) {

            List<Doctor> doctoresDisponibles = buscarDoctores(citaTemporal.getModalidad(), citaTemporal.getIdSede(), citaTemporal.getIdEspecialidad());
            model.addAttribute("doctoresDisponibles", doctoresDisponibles);

            return "paciente/reservar2";

        } else if (buscarDoctores(citaTemporal.getModalidad(), citaTemporal.getIdSede(), citaTemporal.getIdEspecialidad()).size() == 0) {
            // * Validar lo que ocurre si no hay doctores disponibles para ciertos casos
            return "paciente/reservar2";

        } else {

            // OBTENCIÓN DE HORARIOS TOTALES
            List<LocalTime> horariosTotal = new ArrayList<>();
            Doctor doctor = doctorRepository.findById(citaTemporal.getIdDoctor()).get();
            int duracionCita = doctor.getDuracion_cita_minutos();
            int duracionComida = 60; // minutos
            LocalTime hora = doctor.getHorario().getDisponibilidad_inicio();
            LocalTime horaFin = doctor.getHorario().getDisponibilidad_fin();
            LocalTime horaComida = doctor.getHorario().getComida_inicio();

            while (hora.isBefore(horaFin)) {

                if (hora.isBefore(horaComida) || hora.isAfter(horaComida.plusMinutes(duracionComida - 1))) {
                    horariosTotal.add(hora);
                } else if (hora.isAfter(horaComida)) {
                    hora = horaComida.plusMinutes(duracionComida);
                    continue;
                }

                hora = hora.plusMinutes(duracionCita);
            }

            // OBTENCIÓN DE HORARIOS OCUPADOS
            List<HorarioOcupadoDTO> horariosOcupados = horarioRepository.buscarHorariosOcupados(doctor.getId_doctor(), citaTemporal.getFecha());
            HashMap<LocalTime, String> horarios = new HashMap<>();

            if (horariosOcupados.size() > 0) {
                for (LocalTime horario : horariosTotal) {
                    for (HorarioOcupadoDTO ocupado : horariosOcupados) {
                        if (horario.equals(ocupado.getHorario())) {
                            horarios.put(horario, "Ocupado");
                            break;
                        } else {
                            horarios.put(horario, "Disponible");
                        }
                    }
                }
            } else {
                for (LocalTime horario : horariosTotal) {
                    horarios.put(horario, "Disponible");
                }
            }

            model.addAttribute("horariosDisponibles", horarios);

            return "paciente/reservar3";
        }
    }

    @PostMapping("/reservar3")
    public String reservar3Post(@ModelAttribute("citaTemporal") CitaTemporal citaTemporal,
                                @RequestParam("hora") String hora,
                                Model model, HttpSession session, Authentication authentication) {

        Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());
        session.setAttribute("paciente", paciente);

        String inicioString = citaTemporal.getFecha().toString() + ' ' + hora;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime inicio = LocalDateTime.parse(inicioString, formatter);
        LocalDateTime fin = inicio.plusHours(1);

        citaTemporal.setInicio(inicio);
        citaTemporal.setFin(fin);

        model.addAttribute("sede", sedeRepository.findById(citaTemporal.getIdSede()).get());
        model.addAttribute("especialidad", especialidadRepository.findById(citaTemporal.getIdEspecialidad()).get());
        model.addAttribute("doctor", doctorRepository.findById(citaTemporal.getIdDoctor()).get());
        model.addAttribute("precio", administrativoPorEspecialidadPorSedeRepository.buscarPorSedeYEspecialidad(citaTemporal.getIdSede(), citaTemporal.getIdEspecialidad()).getPrecio_cita());

        return "paciente/reservar4";
    }

    @PostMapping("/reservar4")
    public String reservar4(@ModelAttribute("citaTemporal") CitaTemporal citaTemporal,
                            HttpSession session, Authentication authentication) {

        Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());
        session.setAttribute("paciente", paciente);

        String tipoPago;
        if (citaTemporal.getModalidad() == 0) {
            tipoPago = "Efectivo";
        } else {
            tipoPago = "Tarjeta";
        }

        citaRepository.reservarCita(paciente.getIdPaciente(), citaTemporal.getIdDoctor(), citaTemporal.getInicio(), citaTemporal.getFin(), citaTemporal.getModalidad(), citaTemporal.getIdSede(), paciente.getSeguro().getIdSeguro());
        pagoRepository.nuevoPago(citaRepository.obtenerUltimoId(), tipoPago);

        return "redirect:/Paciente/confirmacion";
    }

    /* SECCIÓN PERFIL */
    @GetMapping("/perfil")
    public String perfil(Model model, HttpSession session, Authentication authentication) {

        Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());
        session.setAttribute("paciente", paciente);

        List<Alergia> alergiasPaciente = alergiaRepository.buscarPorPacienteId(paciente.getIdPaciente());
        List<Cita> historialCitas = citaRepository.buscarHistorialDeCitas(paciente.getIdPaciente());
        model.addAttribute("alergiasPaciente", alergiasPaciente);
        model.addAttribute("paciente", paciente);
        model.addAttribute("historialCitas", historialCitas);

        return "paciente/perfil";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil(Model model, HttpSession session, Authentication authentication) {

        Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());
        session.setAttribute("paciente", paciente);

        List<Seguro> seguroList = seguroRepository.findAll();
        List<Alergia> alergiasPaciente = alergiaRepository.buscarPorPacienteId(paciente.getIdPaciente());
        List<Distrito> distritoList = distritoRepository.findAll();

        model.addAttribute("seguroList", seguroList);
        model.addAttribute("alergiasPaciente", alergiasPaciente);
        model.addAttribute("distritoList", distritoList);
        model.addAttribute("paciente", paciente);

        return "paciente/perfilEditar";
    }

    @PostMapping("/perfil/guardar")
    public String guardarPerfil(@ModelAttribute("paciente") @Valid Paciente paciente,
                                BindingResult bindingResult,
                                @RequestParam(name = "archivo") MultipartFile file,
                                RedirectAttributes attr,
                                Model model, Authentication authentication, HttpSession session) {

        Paciente p = pacienteRepository.findByCorreo(authentication.getName());
        session.setAttribute("paciente", p);
        String fileName = file.getOriginalFilename();

        if (p.getIdPaciente().equals(paciente.getIdPaciente())) {

            if (fileName.contains("..") || fileName.contains(" ")) {
                attr.addFlashAttribute("msgError", "El archivo contiene caracteres inválidos");
                return "redirect:/Paciente/perfil/editar";
            }

            if (bindingResult.hasErrors()) {
                model.addAttribute("seguroList", seguroRepository.findAll());
                model.addAttribute("alergiasPaciente", alergiaRepository.buscarPorPacienteId(paciente.getIdPaciente()));
                model.addAttribute("distritoList", distritoRepository.findAll());
                return "paciente/perfilEditar";
            } else {
                try {
                    if (file.isEmpty()) {
                        paciente.setFoto(p.getFoto());
                        paciente.setFotoname(p.getFotoname());
                        paciente.setFotocontenttype(p.getFotocontenttype());
                    } else {
                        paciente.setFoto(file.getBytes());
                        paciente.setFotoname(fileName);
                        paciente.setFotocontenttype(file.getContentType());
                    }

                    if (!p.getCorreo().equals(paciente.getCorreo())) {
                        Credenciales credenciales = credencialesRepository.buscarPorId(p.getIdPaciente());
                        Credenciales nuevasCredenciales = new Credenciales(p.getIdPaciente(), paciente.getCorreo(), credenciales.getContrasena());
                        credencialesRepository.save(nuevasCredenciales);
                    }

                    pacienteRepository.save(paciente);

                    attr.addFlashAttribute("msgActualizacion", "Su perfil se ha actualizado correctamente");
                    return "redirect:/Paciente/perfil";

                } catch (IOException e) {
                    e.printStackTrace();
                    attr.addFlashAttribute("msgError", "Ocurrió un error al subir el archivo");
                    return "redirect:/Paciente/perfil";
                }

            }

        } else {
            attr.addFlashAttribute("msgError", "Ocurrió un error al actualizar el perfil");
            return "redirect:/Paciente/perfil";
        }
    }

    @PostMapping("/perfil/guardarAlergia")
    public String guardarAlergia(Alergia alergia, RedirectAttributes attr, Authentication authentication) {

        Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());
        if (paciente.getIdPaciente().equals(alergia.getPaciente().getIdPaciente())) {
            alergiaRepository.save(alergia);
            return "redirect:/Paciente/perfil/editar";
        } else {
            attr.addFlashAttribute("msgError", "Ocurrió un error al actualizar el perfil");
            return "redirect:/Paciente/perfil";
        }
    }

    @GetMapping("/perfil/borrarAlergia")
    public String borrarAlergia(@RequestParam(name = "idAlergia") int idAlergia,
                                Authentication authentication, HttpSession session) {

        Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());
        session.setAttribute("paciente", paciente);

        Optional<Alergia> optionalAlergia = alergiaRepository.findById(idAlergia);
        if (optionalAlergia.isPresent()) {
            alergiaRepository.deleteById(idAlergia);
        }
        return "redirect:/Paciente/perfil/editar";
    }

    @GetMapping("/perfil/quitarFoto")
    public String quitarFoto(Authentication authentication) {

        String idPaciente = pacienteRepository.findByCorreo(authentication.getName()).getIdPaciente();

        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPaciente);
        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            pacienteRepository.quitarFoto(paciente.getIdPaciente());
            return "redirect:/Paciente/perfil";
        }

        return "redirect:/Paciente/perfil";
    }

    @GetMapping("/perfil/cambiarContrasena")
    public String cambiarContrasena(HttpSession session, Authentication authentication) {

        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));

        return "paciente/perfilContrasena";
    }

    @PostMapping("/perfil/guardarContrasena")
    public String guardarContrasena(@RequestParam("actual") String contrasenaActual,
                                    @RequestParam("nueva1") String contrasenaNueva1,
                                    @RequestParam("nueva2") String contrasenaNueva2,
                                    RedirectAttributes attr, Authentication authentication) {

        Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());

        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        Credenciales credenciales = credencialesRepository.buscarPorId(paciente.getIdPaciente());
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

        return "redirect:/Paciente/perfil";
    }

    @GetMapping("/imagePaciente")
    public ResponseEntity<byte[]> mostrarImagenPaciente(@RequestParam("idPaciente") String idPaciente) {

        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPaciente);

        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            byte[] imagenComoBytes = paciente.getFoto();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(
                    MediaType.parseMediaType(paciente.getFotocontenttype()));
            return new ResponseEntity<>(
                    imagenComoBytes,
                    httpHeaders,
                    HttpStatus.OK);
        } else {
            return null;
        }
    }

    /* SECCIÓN DOCTORES */
    @GetMapping("/doctores")
    public String verDoctores(@RequestParam("sede") int idSede,
                              @RequestParam("esp") int idEspecialidad,
                              @RequestParam("pag") int pagina,
                              Model model, HttpSession session, Authentication authentication) {

        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));

        List<Doctor> doctorList;
        int totalPaginas;
        int numDoctores;

        if (idEspecialidad == 0) {
            doctorList = doctorRepository.buscarDoctorSedePaginado(idSede, pagina - 1, 8);
            numDoctores = doctorRepository.numDoctoresSede(idSede);
        } else {
            doctorList = doctorRepository.buscarDoctorSedeEspecialidadPaginado(idSede, idEspecialidad, pagina - 1, 8);
            numDoctores = doctorRepository.numDoctoresSedeEspecialidad(idSede, idEspecialidad);
        }

        totalPaginas = (int) Math.ceil(numDoctores / 8.0);

        List<Sede> sedeList = sedeRepository.findAll();
        List<Especialidad> especialidadList = especialidadRepository.findAll();

        if (totalPaginas > 0) {
            List<Integer> paginas = IntStream.rangeClosed(1, totalPaginas).boxed().toList();
            model.addAttribute("paginas", paginas);
            model.addAttribute("paginaActual", pagina);
            model.addAttribute("paginaPrevia", pagina - 1);
            model.addAttribute("paginaSiguiente", pagina + 1);
            model.addAttribute("totalPaginas", totalPaginas);
        }

        model.addAttribute("idSede", idSede);
        model.addAttribute("idEspecialidad", idEspecialidad);
        model.addAttribute("doctorList", doctorList);
        model.addAttribute("sedeList", sedeList);
        model.addAttribute("especialidadList", especialidadList);

        model.addAttribute("dia1", LocalDateTime.now().plusDays(1));
        model.addAttribute("dia2", LocalDateTime.now().plusDays(2));

        return "paciente/doctores";

    }

    @GetMapping("/imageDoctor")
    public ResponseEntity<byte[]> mostrarImagenDoctor(@RequestParam("idDoctor") String idDoctor) {

        Optional<Doctor> optionalDoctor = doctorRepository.findById(idDoctor);

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

    @GetMapping("/perfilDoctor")
    public String verPerfilDoctor(@RequestParam("doc") String idDoctor,
                                  Model model, HttpSession session, Authentication authentication) {

        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));

        Optional<Doctor> optionalDoctor = doctorRepository.findById(idDoctor);
        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            model.addAttribute("doctor", doctor);
            model.addAttribute("dia1", LocalDateTime.now().plusDays(1));
            model.addAttribute("dia2", LocalDateTime.now().plusDays(2));
        }
        return "paciente/doctorPerfil";
    }

    @PostMapping("/reservarDoctor")
    public String reservarDoctor1(@ModelAttribute("citaTemporal") CitaTemporal citaTemporal,
                                  @ModelAttribute("hora") String hora,
                                  HttpSession session, Authentication authentication) {

        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));

        return "paciente/reservarDoctor1";
    }

    @PostMapping("/reservarDoctor2")
    public String reservarDoctor2(@ModelAttribute("citaTemporal") @Valid CitaTemporal citaTemporal, BindingResult bindingResult,
                                  @ModelAttribute("hora") String hora,
                                  Model model, HttpSession session, Authentication authentication) {

        if (bindingResult.hasErrors()) {
            return "paciente/reservarDoctor1";
        } else {

            session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));

            String inicioString = citaTemporal.getFecha().toString() + ' ' + hora;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime inicio = LocalDateTime.parse(inicioString, formatter);
            LocalDateTime fin = inicio.plusHours(1);

            citaTemporal.setInicio(inicio);
            citaTemporal.setFin(fin);

            model.addAttribute("sede", sedeRepository.findById(citaTemporal.getIdSede()).get());
            model.addAttribute("especialidad", especialidadRepository.findById(citaTemporal.getIdEspecialidad()).get());
            model.addAttribute("doctor", doctorRepository.findById(citaTemporal.getIdDoctor()).get());

            return "paciente/reservar4";
        }

    }

    @PostMapping("/reservarDoctor3")
    public String reserarDoctor3(@ModelAttribute("citaTemporal") CitaTemporal citaTemporal,
                                 HttpSession session, Authentication authentication) {

        Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());
        session.setAttribute("paciente", paciente);

        citaRepository.reservarCita(citaTemporal.getIdPaciente(), citaTemporal.getIdDoctor(), citaTemporal.getInicio(), citaTemporal.getFin(), citaTemporal.getModalidad(), citaTemporal.getIdSede(), paciente.getSeguro().getIdSeguro());
        pagoRepository.nuevoPago(citaRepository.obtenerUltimoId(), "Efectivo");

        return "redirect:/Paciente/confirmacion";
    }

    @GetMapping("/confirmacion")
    public String confirmarReserva(HttpSession session, Authentication authentication) {

        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));

        return "paciente/confirmacion";
    }

    @GetMapping("/sesionVirtual")
    public String sesionVirtual(HttpSession session, Authentication authentication) {
        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
        return "paciente/sesionVirtual";
    }

    /* SECCIÓN CITAS */
    @GetMapping("/citas")
    public String verCitas(Model model, HttpSession session, Authentication authentication) {

        Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());
        session.setAttribute("paciente", paciente);

        List<Cita> proximasCitas = citaRepository.buscarProximasCitas(paciente.getIdPaciente());
        List<TorreYPisoDTO> torresYPisos = citaRepository.buscarTorresYPisos(paciente.getIdPaciente());

        model.addAttribute("proximasCitas", proximasCitas);
        model.addAttribute("torresYPisos", torresYPisos);

        return "paciente/citas";
    }

    /* SECCIÓN PAGOS */
    @GetMapping("/pagos")
    public String pagos(@ModelAttribute("tarjetaPago") TarjetaPago tarjetaPago,
                        Model model, HttpSession session, Authentication authentication) {

        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
        List<Pago> pagoList = pagoRepository.findAll();
        model.addAttribute("pagoList", pagoList);
        return "paciente/pagos";
    }

    @GetMapping("/filtrarPagos")
    public String filtrarPagos(@ModelAttribute("tarjetaPago") TarjetaPago tarjetaPago,
                               @RequestParam("filtro") Integer filtro, Model model, HttpSession session, Authentication authentication) {

        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));

        List<Pago> pagoList = pagoRepository.findAll();
        model.addAttribute("pagoList", pagoList);
        model.addAttribute("filtro", filtro);
        return "paciente/pagos";
    }

    @PostMapping("/guardarPago")
    public String guardarPago(@ModelAttribute("tarjetaPago") @Valid TarjetaPago tarjetaPago, BindingResult bindingResult,
                              @RequestParam("idPago") int idPago, @RequestParam("fechaStr") String fechaStr,
                              @RequestParam("filtro") int filtro, Model model, RedirectAttributes attr,
                              HttpSession session, Authentication authentication) {

        if (bindingResult.hasErrors()) {
            session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
            if (filtro == 0) {
                List<Pago> pagoList = pagoRepository.findAll();
                model.addAttribute("pagoList", pagoList);
                model.addAttribute("filtro", filtro);
                model.addAttribute("activarModal", true);

                return "paciente/pagos";
            } else {
                if (filtro == 1) {
                    List<Pago> pagoList = pagoRepository.findAll();
                    model.addAttribute("pagoList", pagoList);
                    model.addAttribute("filtro", filtro);
                    model.addAttribute("activarModal", true);

                    return "paciente/pagos";
                }
            }
        } else {
            session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
            pagoRepository.guardarPago(idPago);
            attr.addFlashAttribute("msg", "Pago realizado exitosamente");
            return "redirect:/Paciente/pagos";
        }
        return "redirect:/Paciente/pagos";
    }

    @GetMapping("/recibo")
    public String verReciboPago(@RequestParam("idPago") int idPago,
                                Model model, HttpSession session, Authentication authentication) {

        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));

        Optional<Pago> optionalPago = pagoRepository.findById(idPago);
        if (optionalPago.isPresent()) {
            Pago pago = optionalPago.get();
            model.addAttribute("pago", pago);
        }
        return "paciente/recibo";
    }

    /* SECCIÓN CUESTIONARIOS */
    @GetMapping("/cuestionarios")
    public String cuestionarios(HttpSession session, Model model, Authentication authentication) {

        Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());
        session.setAttribute("paciente", paciente);

        List<CuestionarioPorCita> cuestionarioPorCitaList = cuestionarioPorCitaRepository.buscarPorPaciente(paciente.getIdPaciente());

        model.addAttribute("cuestionarios", cuestionarioPorCitaList);

        return "paciente/cuestionarios";
    }

    @GetMapping("/cuestionario")
    public String completarCuestionario(@RequestParam("cues") Integer idCuestionario,
                                        @RequestParam("cita") Integer idCita,
                                        @ModelAttribute("cuestionario") CuestionarioPorCita cuestionario,
                                        Model model, HttpSession session, Authentication authentication) {

        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));

        model.addAttribute("cuestionario", cuestionarioPorCitaRepository.findByIdIdCuestionarioAndIdIdCita(idCuestionario, idCita));
        model.addAttribute("preguntas", cuestionarioRepository.buscarPorId(idCuestionario));

        return "paciente/cuestionariosCompletar";
    }

    @PostMapping("/guardarCuestionario")
    public String guardarCuestionario(@ModelAttribute("cuestionario") @Valid CuestionarioPorCita cuestionario, BindingResult bindingResult,
                                      Model model, HttpSession session, Authentication authentication) {

        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));

        if (bindingResult.hasErrors()) {
            model.addAttribute("preguntas", cuestionarioRepository.buscarPorId(cuestionario.getCuestionario().getId_cuestionario()));
            return "paciente/cuestionariosCompletar";
        }

        cuestionarioPorCitaRepository.guardarCuestionario(cuestionario.getR1(), cuestionario.getR2(), cuestionario.getR3(), cuestionario.getR4(), cuestionario.getR5(),
                cuestionario.getR6(), cuestionario.getR7(), cuestionario.getR8(), cuestionario.getR9(), cuestionario.getR10(), cuestionario.getR11(),
                cuestionario.getCuestionario().getId_cuestionario(), cuestionario.getCita().getId_cita());

        return "redirect:/Paciente/cuestionarios";
    }

    /* SECCIÓN CONSENTIMIENTOS */
    @GetMapping("/consentimientos")
    public String consentimientos(Model model, HttpSession session, Authentication authentication) {

        Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());
        session.setAttribute("paciente", paciente);

        List<PacientePorConsentimiento> consentimientos = pacientePorConsentimientoRepository.findByIdIdPaciente(paciente.getIdPaciente());

        model.addAttribute("consentimientos", consentimientos);
        return "paciente/consentimientos";
    }

    @PostMapping("/consentimientos/actualizar")
    public String actualizarConsentimientos(@RequestParam Map<String, Boolean> consentimientos,
                                            RedirectAttributes attr, Authentication authentication) {

        Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());

        pacientePorConsentimientoRepository.actualizarConsentimientosANegativo(paciente.getIdPaciente());
        for (String key : consentimientos.keySet()) {
            if (key.length() == 1) {
                pacientePorConsentimientoRepository.actualizarConsentimientoAPositivo(paciente.getIdPaciente(), key);
            }
        }

        attr.addFlashAttribute("msgActualizacion", "Consentimientos actualizados correctamente");

        return "redirect:/Paciente/consentimientos";
    }

    /* SECCIÓN MENSAJERÍA */

    @GetMapping("/mensajeria")
    public String mensajeria(HttpSession session, Authentication authentication) {
        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
        return "paciente/mensajeria";
    }

    /* FUNCIONES UTILIZADAS */
    List<Doctor> buscarDoctores(int modalidad, int idSede, int idEspecialidad) {
        if (modalidad == 0) {
            return doctorRepository.findBySede_IdSedeAndEspecialidad_IdEspecialidad(idSede, idEspecialidad);
        } else {
            return doctorRepository.buscarVirtualesPorSedeYEspecialidad(idSede, idEspecialidad);
        }
    }
}