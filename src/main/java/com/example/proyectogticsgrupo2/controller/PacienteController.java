package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.config.SecurityConfig;
import com.example.proyectogticsgrupo2.dto.TorreYPisoDTO;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    final NotificacionRepository notificacionRepository;
    final SecurityConfig securityConfig;
    final StylevistasRepository stylevistasRepository;

    public PacienteController(PacienteRepository pacienteRepository, EspecialidadRepository especialidadRepository,
                              SedeRepository sedeRepository, AlergiaRepository alergiaRepository, SeguroRepository seguroRepository,
                              DistritoRepository distritoRepository, DoctorRepository doctorRepository, PacientePorConsentimientoRepository pacientePorConsentimientoRepository,
                              CitaRepository citaRepository, PagoRepository pagoRepository, CitaTemporalRepository citaTemporalRepository, HorarioRepository horarioRepository,
                              CredencialesRepository credencialesRepository, CuestionarioPorCitaRepository cuestionarioPorCitaRepository, CuestionarioRepository cuestionarioRepository,
                              AdministrativoPorEspecialidadPorSedeRepository administrativoPorEspecialidadPorSedeRepository, NotificacionRepository notificacionRepository, SecurityConfig securityConfig,
                              StylevistasRepository stylevistasRepository) {

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
        this.notificacionRepository = notificacionRepository;
        this.securityConfig = securityConfig;
        this.stylevistasRepository = stylevistasRepository;
    }

    /* INICIO */
    @GetMapping(value = {"", "/", "/index"})
    public String index(Model model, Authentication authentication, HttpSession session, RedirectAttributes attr) {
/*
        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
*/
        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        String impersonatedUser = (String) session.getAttribute("impersonatedUser");
        Boolean superAdminLogueadoComoPaciente = (Boolean) session.getAttribute("superAdminLogueadoComoPaciente");
        if (superAdminLogueadoComoPaciente == null) {
            superAdminLogueadoComoPaciente = false;
        }
        model.addAttribute("superAdminLogueadoComoPaciente", superAdminLogueadoComoPaciente);
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente;

        if (impersonatedUser != null) {
            // Si hay un usuario "impersonado", buscar al paciente por ese correo electrónico
            paciente = pacienteRepository.findByCorreo(impersonatedUser);
        } else {
            paciente = pacienteRepository.findByCorreo(authentication.getName());
            if (paciente == null) {
                return "redirect:/error";
            }
        }

        session.setAttribute("paciente", paciente);
        List<Sede> sedeList = sedeRepository.findAll();
        model.addAttribute("sedeList", sedeList);

        return "paciente/index";
    }

    @GetMapping("/imageSede")
    public ResponseEntity<byte[]> mostrarImagenSede(@RequestParam("idSede") int idSede) {

        Optional<Sede> optionalSede = sedeRepository.findById(idSede);
        pacienteRepository.anularCitaNoCancelada();
        if (optionalSede.isPresent()) {
            Sede sede = optionalSede.get();
            byte[] imagenComoBytes = sede.getFoto();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType(sede.getFotocontenttype()));
            return new ResponseEntity<>(imagenComoBytes, httpHeaders, HttpStatus.OK);
        } else {
            return null;
        }
    }

    /* SECCIÓN RESERVAR CITA */

    @GetMapping("/reservarTipo")
    public String reservar(HttpSession session, Authentication authentication) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        return "paciente/reservar0";
    }

    @GetMapping("/reservar")
    public String reservarGet(@ModelAttribute("citaTemporal") CitaTemporal citaTemporal, Model model, HttpSession session, Authentication authentication) {
        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();


            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
/*
        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
*/

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        model.addAttribute("sedeList", sedeRepository.findAll());

        return "paciente/reservar1";
    }

    @PostMapping("/reservar1")
    public String reservar1Post(@Validated(CitaTemporal.validacion1.class) @ModelAttribute("citaTemporal") CitaTemporal citaTemporal, BindingResult bindingResult,
                                Model model, HttpSession session, Authentication authentication) {

        List<Doctor> doctoresDisponibles;

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        if (bindingResult.hasErrors()) {
            Optional<Stylevistas> style = stylevistasRepository.findById(5);
            if (style.isPresent()) {
                Stylevistas styleActual = style.get();

                model.addAttribute("headerColorPaciente", styleActual.getHeader());
            } else {
                // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
            }
            System.out.println("Error validacion: " + bindingResult.getAllErrors());
            model.addAttribute("sedeList", sedeRepository.findAll());
            model.addAttribute("especialidadList", especialidadRepository.findAll());
            return "paciente/reservar1";

        } else {
            Optional<Stylevistas> style = stylevistasRepository.findById(5);
            if (style.isPresent()) {
                Stylevistas styleActual = style.get();

                model.addAttribute("headerColorPaciente", styleActual.getHeader());
            } else {
                // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
            }

            doctoresDisponibles = buscarDoctores(citaTemporal.getModalidad(), citaTemporal.getIdSede(), citaTemporal.getIdEspecialidad());
            model.addAttribute("doctoresDisponibles", doctoresDisponibles);

            return "paciente/reservar2";
        }
    }

    @PostMapping("/reservar2")
    public String reservar2Post(@ModelAttribute("citaTemporal") @Valid CitaTemporal citaTemporal, BindingResult bindingResult,
                                Model model, HttpSession session, Authentication authentication) {
        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);

        session.setAttribute("paciente", paciente);

        if (bindingResult.hasErrors()) {
            Optional<Stylevistas> style2 = stylevistasRepository.findById(5);
            if (style2.isPresent()) {
                Stylevistas styleActual = style2.get();

                model.addAttribute("headerColorPaciente", styleActual.getHeader());
            } else {
                // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
            }
            System.out.println("Error validacion: " + bindingResult.getAllErrors());
            List<Doctor> doctoresDisponibles = buscarDoctores(citaTemporal.getModalidad(), citaTemporal.getIdSede(), citaTemporal.getIdEspecialidad());
            model.addAttribute("doctoresDisponibles", doctoresDisponibles);

            return "paciente/reservar2";

        } else {
            Optional<Stylevistas> style3 = stylevistasRepository.findById(5);
            if (style3.isPresent()) {
                Stylevistas styleActual = style3.get();

                model.addAttribute("headerColorPaciente", styleActual.getHeader());
            } else {
                // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
            }
            Doctor doctor = doctorRepository.findById(citaTemporal.getIdDoctor()).get();

            model.addAttribute("sede", sedeRepository.findById(citaTemporal.getIdSede()).get());
            model.addAttribute("especialidad", especialidadRepository.findById(citaTemporal.getIdEspecialidad()).get());
            model.addAttribute("doctor", doctor);
            Float precioBase = administrativoPorEspecialidadPorSedeRepository.buscarPorSedeYEspecialidad(citaTemporal.getIdSede(), citaTemporal.getIdEspecialidad()).getPrecio_cita();
            model.addAttribute("precio", precioBase * paciente.getSeguro().getCoaseguro());

            citaTemporal.setFin(citaTemporal.getInicio().plusMinutes(doctor.getDuracion_cita_minutos()));

            return "paciente/reservar3";
        }
    }

    @PostMapping("/reservar3")
    public String reservar4(@ModelAttribute("citaTemporal") CitaTemporal citaTemporal,

                            @RequestParam(name = "citaPendiente", required = false) Boolean citaPendiente,
                            @RequestParam(name = "examenPendiente", required = false) Boolean examenPendiente,
                            @RequestParam(name = "codigoRecibo") String codigoRecibo,
                            Model model, HttpSession session, Authentication authentication,
                            HttpServletRequest request) {

        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        String tipoPago;
        if (citaTemporal.getModalidad() == 0) {
            tipoPago = "Efectivo";
        } else {
            tipoPago = "Tarjeta";
        }

        Doctor doctor = doctorRepository.findById(citaTemporal.getIdDoctor()).get();

        String inicioString = citaTemporal.getFecha().toString() + ' ' + citaTemporal.getInicio().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime inicio = LocalDateTime.parse(inicioString, formatter);
        LocalDateTime fin = inicio.plusMinutes(doctor.getDuracion_cita_minutos());

        Especialidad especialidad = especialidadRepository.findById(citaTemporal.getIdEspecialidad()).get();

        // VALIDAR LO QUE OCURRE EN CASO SE ESTE RECIBIENDO UNA CITA PENDIENTE
        Cita cita;
        if (citaPendiente != null & examenPendiente == null) {

            citaRepository.reservarCitaPendiente(doctor.getId_doctor(), inicio, fin, citaTemporal.getModalidad(), citaTemporal.getId());
            pagoRepository.nuevoPagoPagado(citaTemporal.getId(), tipoPago, codigoRecibo);
            cita = citaRepository.findById(citaTemporal.getId()).get();

        } else if (examenPendiente != null & citaPendiente == null) {

            citaRepository.reservarExamenPendiente(doctor.getId_doctor(), inicio, fin, citaTemporal.getId());
            pagoRepository.nuevoPago(citaTemporal.getId(), tipoPago, codigoRecibo);
            cita = citaRepository.findById(citaTemporal.getId()).get();

        } else {

            citaRepository.reservarCita(paciente.getIdPaciente(), citaTemporal.getIdDoctor(), inicio, fin, citaTemporal.getModalidad(), citaTemporal.getIdSede(), paciente.getSeguro().getIdSeguro(), especialidad.getIdEspecialidad());
            int idCita = citaRepository.obtenerUltimoId();
            pagoRepository.nuevoPago(idCita, tipoPago, codigoRecibo);
            cita = citaRepository.findById(idCita).get();

        }

        // Enviar correo al paciente - inhabilidado para que no demore tanto xd

        /*
        CorreoCitaRegistrada correo = new CorreoCitaRegistrada(administrativoPorEspecialidadPorSedeRepository);
        String host = request.getServerName()+":"+request.getLocalPort();
        correo.props(host, paciente.getCorreo(), cita);
         */


        model.addAttribute("sede", sedeRepository.findById(citaTemporal.getIdSede()).get());
        model.addAttribute("especialidad", especialidad);
        model.addAttribute("doctor", doctorRepository.findById(citaTemporal.getIdDoctor()).get());
        //model.addAttribute("precio", administrativoPorEspecialidadPorSedeRepository.buscarPorSedeYEspecialidad(citaTemporal.getIdSede(), citaTemporal.getIdEspecialidad()).getPrecio_cita());
        Float precioBase = administrativoPorEspecialidadPorSedeRepository.buscarPorSedeYEspecialidad(citaTemporal.getIdSede(), citaTemporal.getIdEspecialidad()).getPrecio_cita();
        model.addAttribute("precio", precioBase * paciente.getSeguro().getCoaseguro());

        return "paciente/confirmacion";
    }

    @GetMapping("/pendientes")
    public String citasPendientes(Model model, HttpSession session, Authentication authentication) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        List<Cita> citasPendientes = citaRepository.buscarCitasPendientes(paciente.getIdPaciente());
        List<Date> fechasLimite = citaRepository.buscarFechasLimitesDeCitasPendientes(paciente.getIdPaciente());

        model.addAttribute("citasPendientes", citasPendientes);
        model.addAttribute("fechasLimite", fechasLimite);

        return "paciente/pendientes";
    }

    @GetMapping("/reservaPendiente")
    public String reservarPendiente1(@RequestParam("c") Integer idCita,
                                     @ModelAttribute("citaTemporal") CitaTemporal citaTemporal,
                                     Model model, HttpSession session, Authentication authentication) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        Cita cita = citaRepository.buscarPorId(idCita);

        if (cita != null) {

            ZoneId zoneId = ZoneId.of("America/Lima");
            Date fechaLimiteDate = citaRepository.buscarFechaLimiteDeCitaPendiente(cita.getId_cita());
            LocalDate fechaLimite = Instant.ofEpochMilli(fechaLimiteDate.getTime()).atZone(zoneId).toLocalDate();

            citaTemporal.setId(cita.getId_cita());
            citaTemporal.setModalidad(cita.getModalidad());
            citaTemporal.setIdSede(cita.getSede().getIdSede());
            citaTemporal.setIdEspecialidad(cita.getEspecialidad().getIdEspecialidad());

            model.addAttribute("cita", cita);
            if (cita.getEspecialidad().getEs_examen() == 1) {
                return "paciente/reservarExamenPendiente";
            } else {
                citaTemporal.setIdDoctor(cita.getDoctor().getId_doctor());
                model.addAttribute("fechaLimite", fechaLimite);
                return "paciente/reservarCitaPendiente";
            }

        } else {
            return "redirect:/Paciente/pendientes";
        }
    }

    @PostMapping("/reservarPendiente")
    public String reservarPendiente2(@ModelAttribute("citaTemporal") @Valid CitaTemporal citaTemporal, BindingResult bindingResult,
                                     Model model, HttpSession session, Authentication authentication) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);

        session.setAttribute("paciente", paciente);

        Cita cita = citaRepository.buscarPorId(citaTemporal.getId());

        ZoneId zoneId = ZoneId.of("America/Lima");
        Date fechaLimiteDate = citaRepository.buscarFechaLimiteDeCitaPendiente(cita.getId_cita());
        LocalDate fechaLimite = Instant.ofEpochMilli(fechaLimiteDate.getTime()).atZone(zoneId).toLocalDate();

        if (bindingResult.hasErrors()) {
            System.out.println("Error validacion: " + bindingResult.getAllErrors());
            model.addAttribute("cita", cita);
            if (cita.getEspecialidad().getEs_examen() == 1) {
                return "paciente/reservarExamenPendiente";
            } else {
                model.addAttribute("fechaLimite", fechaLimite);
                return "paciente/reservarCitaPendiente";
            }
        } else {

            Doctor doctor = doctorRepository.findById(citaTemporal.getIdDoctor()).get();

            model.addAttribute("sede", sedeRepository.findById(citaTemporal.getIdSede()).get());
            model.addAttribute("especialidad", especialidadRepository.findById(citaTemporal.getIdEspecialidad()).get());
            model.addAttribute("doctor", doctor);
            Float precioBase = administrativoPorEspecialidadPorSedeRepository.buscarPorSedeYEspecialidad(citaTemporal.getIdSede(), citaTemporal.getIdEspecialidad()).getPrecio_cita();

            if (cita.getEspecialidad().getEs_examen() == 1) {
                model.addAttribute("precio", precioBase * paciente.getSeguro().getCoaseguro());
                model.addAttribute("examenPendiente", true);
            } else {

                // validar lo que ocurre si reserva fuera del plazo de 7 días
                if (citaTemporal.getFecha().isAfter(fechaLimite)) {
                    model.addAttribute("precio", precioBase * paciente.getSeguro().getCoaseguro());
                } else {
                    model.addAttribute("precio", 0);
                    model.addAttribute("citaPendiente", true); // el valor de citaPendiente es utilizado para validar el descuento
                }
            }

            citaTemporal.setFin(citaTemporal.getInicio().plusMinutes(doctor.getDuracion_cita_minutos()));

            return "paciente/reservar3";

        }

    }

    @GetMapping("/cancelarCita")
    public String cancelarCita(@RequestParam("idCita") int idCita,
                               RedirectAttributes attr) {

        Optional<Cita> optionalCita = citaRepository.findById(idCita);
        pacienteRepository.anularCitaNoCancelada();
        if (optionalCita.isPresent()) {
            Pago pago = pagoRepository.buscarPorCita(idCita);

            if (pago.getEstadoPago() == 0) {
                pagoRepository.deleteById(pago.getId());
                citaRepository.deleteById(idCita);
                attr.addFlashAttribute("msg", "Cita cancelada correctamente");
            } else {
                attr.addFlashAttribute("msg", "Cancelación de cita inválida");
            }
        } else {
            attr.addFlashAttribute("msg", "Ocurrió un error al cancelar la cita");
        }

        return "redirect:/Paciente/citas";
    }

    /* SECCIÓN PERFIL */
    @GetMapping("/perfil")
    public String perfil(Model model, HttpSession session, Authentication authentication) {

        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        List<Alergia> alergiasPaciente = alergiaRepository.buscarPorPacienteId(paciente.getIdPaciente());
        List<Cita> historialCitas = citaRepository.buscarHistorialDeCitas(paciente.getIdPaciente());
        List<TorreYPisoDTO> torresPisosPrecio = citaRepository.buscarTorresPisosPrecioHistorialCitas(paciente.getIdPaciente());

        model.addAttribute("alergiasPaciente", alergiasPaciente);
        model.addAttribute("paciente", paciente);
        model.addAttribute("historialCitas", historialCitas);
        model.addAttribute("torresPisosPrecio", torresPisosPrecio);

        return "paciente/perfil";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil(Model model, HttpSession session, Authentication authentication) {

        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
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
    public String guardarPerfil(@ModelAttribute("paciente") @Valid Paciente paciente, BindingResult bindingResult,
                                @RequestParam(name = "archivo") MultipartFile file, RedirectAttributes attr,
                                Model model, Authentication authentication, HttpSession session,
                                HttpServletRequest request, HttpServletResponse response) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente p = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", p);

        String fileName = file.getOriginalFilename();

        if (p.getIdPaciente().equals(paciente.getIdPaciente())) {

            if (fileName.contains("..") || fileName.contains(" ")) {
                attr.addFlashAttribute("msgError", "El archivo contiene caracteres inválidos");
                return "redirect:/Paciente/perfil/editar";
            }

            if (bindingResult.hasErrors()) {
                System.out.println("Error: " + bindingResult.getAllErrors());
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
                        pacienteRepository.save(paciente);

                        logout(request, response); // borrando cookies

                        return "redirect:/login";
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
    public String guardarAlergia(Alergia alergia, RedirectAttributes attr, Authentication authentication, HttpSession session) {

        /*  Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());*/
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        if (paciente.getIdPaciente().equals(alergia.getPaciente().getIdPaciente())) {
            alergiaRepository.save(alergia);
            return "redirect:/Paciente/perfil/editar";
        } else {
            attr.addFlashAttribute("msgError", "Ocurrió un error al actualizar el perfil");
            return "redirect:/Paciente/perfil";
        }
    }

    @GetMapping("/perfil/borrarAlergia")
    public String borrarAlergia(@RequestParam(name = "idAlergia") int idAlergia, Authentication authentication, HttpSession session) {

        /*Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());
        session.setAttribute("paciente", paciente);*/

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        Optional<Alergia> optionalAlergia = alergiaRepository.findById(idAlergia);
        if (optionalAlergia.isPresent()) {
            alergiaRepository.deleteById(idAlergia);
        }
        return "redirect:/Paciente/perfil/editar";
    }

    @GetMapping("/perfil/quitarFoto")
    public String quitarFoto(HttpSession session, Authentication authentication) {

        /*  String idPaciente = pacienteRepository.findByCorreo(authentication.getName()).getIdPaciente();*/
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        String idPaciente = pacienteRepository.findByCorreo(userEmail).getIdPaciente();

        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPaciente);
        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            pacienteRepository.quitarFoto(paciente.getIdPaciente());
            return "redirect:/Paciente/perfil";
        }

        return "redirect:/Paciente/perfil";
    }

    @GetMapping("/perfil/cambiarContrasena")
    public String cambiarContrasena(Model model, HttpSession session, Authentication authentication) {

        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();


            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        return "paciente/perfilContrasena";
    }

    @PostMapping("/perfil/guardarContrasena")
    public String guardarContrasena(@RequestParam("actual") String contrasenaActual,
                                    @RequestParam("nueva1") String contrasenaNueva1,
                                    @RequestParam("nueva2") String contrasenaNueva2,
                                    Model model, RedirectAttributes attr, Authentication authentication,
                                    HttpSession session) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);

        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        Credenciales credenciales = credencialesRepository.buscarPorId(paciente.getIdPaciente());
        Credenciales nuevasCredenciales = new Credenciales(credenciales.getId_credenciales(), credenciales.getCorreo(), passwordEncoder.encode(contrasenaNueva1));

        if (passwordEncoder.matches(contrasenaActual, credenciales.getContrasena())) {
            if (contrasenaNueva1.equals(contrasenaNueva2)) {

                if (!contrasenaNueva1.equals("")) {

                    if (contrasenaNueva1.length()>=6){
                        credencialesRepository.save(nuevasCredenciales);
                        attr.addFlashAttribute("msgActualizacion", "Contraseña actualizada correctamente");
                        attr.addFlashAttribute("pass", contrasenaNueva1);
                        return "redirect:/Paciente/perfil";
                    }
                    else{
                        attr.addFlashAttribute("error2", "La contraseña debe tener como mínimo 6 dígitos");
                    }

                } else {
                    attr.addFlashAttribute("error2", "Ingrese una nueva contraseña válida");
                }

            } else {
                attr.addFlashAttribute("error2", "Las contraseñas deben coincidir");
            }
        } else {
            attr.addFlashAttribute("error1", "La contraseña actual es incorrecta");
        }
        return "redirect:/Paciente/perfil/cambiarContrasena";
    }

    @GetMapping("/imagePaciente")
    public ResponseEntity<byte[]> mostrarImagenPaciente(@RequestParam("idPaciente") String idPaciente) {

        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPaciente);
        pacienteRepository.anularCitaNoCancelada();
        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            byte[] imagenComoBytes = paciente.getFoto();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType(paciente.getFotocontenttype()));
            return new ResponseEntity<>(imagenComoBytes, httpHeaders, HttpStatus.OK);
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


        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

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

        return "paciente/doctores";

    }

    @GetMapping("/imageDoctor")
    public ResponseEntity<byte[]> mostrarImagenDoctor(@RequestParam("idDoctor") String idDoctor) {

        Optional<Doctor> optionalDoctor = doctorRepository.findById(idDoctor);
        pacienteRepository.anularCitaNoCancelada();
        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            byte[] imagenComoBytes = doctor.getFoto();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType(doctor.getFotocontenttype()));
            return new ResponseEntity<>(imagenComoBytes, httpHeaders, HttpStatus.OK);
        } else {
            return null;
        }
    }

    @GetMapping("/perfilDoctor")
    public String verPerfilDoctor(@RequestParam("doc") String idDoctor,
                                  Model model, HttpSession session, Authentication authentication) {


        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);
        Optional<Doctor> optionalDoctor = doctorRepository.findById(idDoctor);
        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            model.addAttribute("doctor", doctor);
        } else {
            return "redirect:/Paciente/doctores";
        }
        return "paciente/doctorPerfil";
    }

    @PostMapping("/reservarDoctor")
    public String reservarDoctor1(@ModelAttribute("citaTemporal") CitaTemporal citaTemporal,
                                  Model model, HttpSession session, Authentication authentication) {


        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();


            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }


        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

        pacienteRepository.anularCitaNoCancelada();

        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        Doctor doctor = doctorRepository.findById(citaTemporal.getIdDoctor()).get();

        model.addAttribute("sede", sedeRepository.findById(citaTemporal.getIdSede()).get());
        model.addAttribute("especialidad", especialidadRepository.findById(citaTemporal.getIdEspecialidad()).get());
        model.addAttribute("doctor", doctor);
        Float precioBase = administrativoPorEspecialidadPorSedeRepository.buscarPorSedeYEspecialidad(citaTemporal.getIdSede(), citaTemporal.getIdEspecialidad()).getPrecio_cita();
        model.addAttribute("precio", precioBase * paciente.getSeguro().getCoaseguro());

        citaTemporal.setFin(citaTemporal.getInicio().plusMinutes(doctor.getDuracion_cita_minutos()));

        return "paciente/reservar3";
    }

    @GetMapping("/confirmacion")
    public String confirmarReserva(HttpSession session, Authentication authentication, Model model) {

        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }


        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);
        model.addAttribute("activarModal", true);

        return "paciente/confirmacion";
    }

    @GetMapping("/sesionVirtual")
    public String sesion(@RequestParam("c") Integer idCita,
                         Model model, HttpSession session, Authentication authentication) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        Cita cita = citaRepository.findById(idCita).get();

        model.addAttribute("link", cita.getLink());

        return "paciente/sesionVirtual";
    }

    /* SECCIÓN CITAS */
    @GetMapping("/citas")
    public String verCitas(Model model, HttpSession session, Authentication authentication) {


        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();


            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        List<Cita> proximasCitas = citaRepository.buscarProximasCitas(paciente.getIdPaciente());
        List<TorreYPisoDTO> torresPisosPrecio = citaRepository.buscarTorresPisosPrecioProximasCitas(paciente.getIdPaciente());

        model.addAttribute("proximasCitas", proximasCitas);
        model.addAttribute("torresPisosPrecio", torresPisosPrecio);
        model.addAttribute("momentoActual", LocalDateTime.now());

        return "paciente/citas";
    }

    /* SECCIÓN PAGOS */
    @GetMapping("/pagos")
    public String pagos(@ModelAttribute("tarjetaPago") TarjetaPago tarjetaPago, Model model, HttpSession session, Authentication authentication) {


        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);
        model.addAttribute("coaseguro", paciente.getSeguro().getCoaseguro());

        List<Pago> pagoList = pagoRepository.pagosValidosPorPaciente(paciente.getIdPaciente());

        model.addAttribute("pagoList", pagoList);
        return "paciente/pagos";
    }

    @GetMapping("/pagar")
    public String pagar(@ModelAttribute("tarjetaPago") TarjetaPago tarjetaPago, @RequestParam("idPago") Integer idPago, Model model, HttpSession session, Authentication authentication) {

/*
        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
*/

        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        List<Pago> pagoList = pagoRepository.findAll();
        model.addAttribute("idPagar", idPago);
        model.addAttribute("pagoList", pagoList);
        //model.addAttribute("activarModal", true);
        return "paciente/pagos";
    }

    @PostMapping("/guardarPago")
    public String guardarPago(@RequestParam("idPago") int idPago, @RequestParam("confirmado") String confirmado,
                              @RequestParam("idCita") int idCita,
                              Model model, RedirectAttributes attr, HttpSession session, Authentication authentication) {

        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);
        pagoRepository.guardarPago(idPago);

        Integer idCitaPrevia = citaRepository.buscarIdCitaPrevia(idCita);
        if (idCitaPrevia == null) {
            citaRepository.actualizarEstadoEnEspera(1, idCita);
        } else {
            citaRepository.actualizarEstadoEnEspera(5, idCita);
        }
        List<Pago> pagoList = pagoRepository.findAll();
        model.addAttribute("pagoList", pagoList);
        model.addAttribute("activarModalPagado", true);
        attr.addFlashAttribute("msg", "Pago realizado");
        return "redirect:/Paciente/pagos";
    }

    @GetMapping("/recibo")
    public String verReciboPago(@RequestParam("idPago") int idPago, Model model, HttpSession session, Authentication authentication) {

        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);
        model.addAttribute("coaseguro", paciente.getSeguro().getCoaseguro());

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

       /* Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());
        session.setAttribute("paciente", paciente);*/


        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);


        List<CuestionarioPorCita> cuestionarioPorCitaList = cuestionarioPorCitaRepository.buscarPorPaciente(paciente.getIdPaciente());

        model.addAttribute("cuestionarios", cuestionarioPorCitaList);

        return "paciente/cuestionarios";
    }

    @GetMapping("/cuestionario")
    public String completarCuestionario(@RequestParam("cues") Integer idCuestionario, @RequestParam("cita") Integer idCita, @ModelAttribute("cuestionario") CuestionarioPorCita cuestionario, Model model, HttpSession session, Authentication authentication) {

        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        model.addAttribute("cuestionario", cuestionarioPorCitaRepository.findByIdIdCuestionarioAndIdIdCita(idCuestionario, idCita));
        model.addAttribute("preguntas", cuestionarioRepository.buscarPorId(idCuestionario));

        return "paciente/cuestionariosCompletar";
    }

    @PostMapping("/guardarCuestionario")
    public String guardarCuestionario(@ModelAttribute("cuestionario") @Valid CuestionarioPorCita cuestionario, BindingResult bindingResult, Model model, HttpSession session, Authentication authentication) {

        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
        pacienteRepository.anularCitaNoCancelada();
        if (bindingResult.hasErrors()) {
            model.addAttribute("preguntas", cuestionarioRepository.buscarPorId(cuestionario.getCuestionario().getId_cuestionario()));
            return "paciente/cuestionariosCompletar";
        }

        cuestionarioPorCitaRepository.guardarCuestionario(cuestionario.getR1(), cuestionario.getR2(), cuestionario.getR3(), cuestionario.getR4(), cuestionario.getR5(), cuestionario.getR6(), cuestionario.getR7(), cuestionario.getR8(), cuestionario.getR9(), cuestionario.getR10(), cuestionario.getR11(), cuestionario.getCuestionario().getId_cuestionario(), cuestionario.getCita().getId_cita());

        return "redirect:/Paciente/cuestionarios";
    }

    /* SECCIÓN CONSENTIMIENTOS */
    @GetMapping("/consentimientos")
    public String consentimientos(Model model, HttpSession session, Authentication authentication) {

        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        /*Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());
        session.setAttribute("paciente", paciente);*/
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);
        List<PacientePorConsentimiento> consentimientos = pacientePorConsentimientoRepository.findByIdIdPaciente(paciente.getIdPaciente());

        model.addAttribute("consentimientos", consentimientos);
        return "paciente/consentimientos";
    }

    @PostMapping("/consentimientos/actualizar")
    public String actualizarConsentimientos(@RequestParam Map<String, Boolean> consentimientos,
                                            RedirectAttributes attr, Authentication authentication,
                                            HttpSession session, Model model) {

        /*Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());*/

        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);

        session.setAttribute("paciente", paciente);


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
    public String mensajeria(HttpSession session, Authentication authentication, Model model) {
/*
        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
*/
        Optional<Stylevistas> style = stylevistasRepository.findById(5);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorPaciente", styleActual.getHeader());
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        pacienteRepository.anularCitaNoCancelada();
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        return "paciente/mensajeria";
    }

    // SECCIÓN NOTIFICACIONES
    @GetMapping(value = {"/notificacionNoVistos"})
    @ResponseBody
    public Integer notificacionNoVistos(Model model, HttpSession session, Authentication authentication) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        List<Notificacion> listaNotificaciones = notificacionRepository.buscarNotificacionesNoLeidas(paciente.getIdPaciente());
        List<Integer> listaNoVistos = new ArrayList<>();

        /*List<SpringSession> sesiones=springSessionRepository.buscarSesiones();*/

        for (int i = 0; i < listaNotificaciones.size(); i++) {
            listaNoVistos.add(listaNotificaciones.get(i).getRevisado());
        }
        //verificar cuando es nulo

        return listaNoVistos.size();
    }

    @GetMapping(value = {"/notificacionListaTitulo"})
    @ResponseBody
    public List<String> notificacionLista(Model model, HttpSession session, Authentication authentication) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        List<Notificacion> listaNotificaciones = notificacionRepository.buscarNotificaciones(paciente.getIdPaciente());
        List<String> listaTitulos = new ArrayList<>();

        for (int i = 0; i < listaNotificaciones.size(); i++) {
            listaTitulos.add(listaNotificaciones.get(i).getTitulo());
            System.out.println(listaNotificaciones.get(i).getTitulo());
        }

        return listaTitulos;
    }

    @GetMapping(value = {"/notificacionListaDescripcion"})
    @ResponseBody
    public List<String> notificacionListaDescripcion(Model model, HttpSession session, Authentication authentication) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        List<Notificacion> listaNotificaciones = notificacionRepository.buscarNotificaciones(paciente.getIdPaciente());
        List<String> listaTitulos = new ArrayList<>();

        for (int i = 0; i < listaNotificaciones.size(); i++) {
            listaTitulos.add(listaNotificaciones.get(i).getDescripcion());
            System.out.println(listaNotificaciones.get(i).getTitulo());
        }

        return listaTitulos;
    }

    public String obtenerTiempoTranscurrido(int segundos) {
        if (segundos < 60) {
            return "Hace " + segundos + " segundos";
        } else if (segundos < 3600) {
            int minutos = segundos / 60;
            return "Hace " + minutos + " minutos";
        } else if (segundos < 86400) {
            int horas = segundos / 3600;
            return "Hace " + horas + " horas";
        } else {
            int dias = segundos / 86400;
            return "Hace " + dias + " días";
        }
    }

    @GetMapping(value = {"/notificacionListaHora"})
    @ResponseBody
    public List<String> notificacionListaHora(Model model, HttpSession session, Authentication authentication) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        List<Notificacion> listaNotificaciones = notificacionRepository.buscarNotificaciones(paciente.getIdPaciente());
        System.out.println(listaNotificaciones.size());

        List<String> listaTiempo = new ArrayList<>();
        Integer diferencia = 0;
        for (int i = 0; i < listaNotificaciones.size(); i++) {
            diferencia = notificacionRepository.fechaActual(listaNotificaciones.get(i).getFecha());
            System.out.println(diferencia);
            listaTiempo.add(obtenerTiempoTranscurrido(diferencia));
        }

        return listaTiempo;
    }

    @GetMapping(value = {"/SetearRevisadoA1"})
    @ResponseBody
    void SetearRevisadoA1(Model model, HttpSession session, Authentication authentication) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        List<Notificacion> listaNotificaciones = notificacionRepository.buscarNotificacionesNoLeidas(paciente.getIdPaciente());

        for (int i=0; i<listaNotificaciones.size(); i++) {
            notificacionRepository.SetearA1(listaNotificaciones.get(i).getId_notificacion());
        }

    }
    private boolean tieneCuestionarioCitaImportante(List<Notificacion> notificaciones) {
        for (Notificacion notificacion : notificaciones) {
            if (Objects.equals(notificacion.getTitulo(), "Cuestionario de cita (IMPORTANTE)")) {
                return true;
            }
        }
        return false;
    }
    @GetMapping(value = {"/notificacionCuestionario"})
    @ResponseBody
    public List<Integer> notificacionCuestionario(Model model, HttpSession session, Authentication authentication) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        List<CuestionarioPorCita> cuestionarioPorCitaList = cuestionarioPorCitaRepository.buscarPorPaciente(paciente.getIdPaciente());
        List<Notificacion> notificacionList = notificacionRepository.buscarNotificaciones(paciente.getIdPaciente());
        int verificar = 0;
        int idCuestionario = 0;
        int idCita = 0;
        List<Integer> ListaIdCitayIdCuestionario = new ArrayList<>();

        for (int i = 0; i < cuestionarioPorCitaList.size(); i++) {
            if (cuestionarioPorCitaList.get(i).getOpcion_inicio_sesion() == 0) {
                verificar = 1;
                idCuestionario = cuestionarioPorCitaList.get(i).getCuestionario().getId_cuestionario();
                idCita = cuestionarioPorCitaList.get(i).getCita().getId_cita();

                if (notificacionList == null || notificacionList.isEmpty() || !tieneCuestionarioCitaImportante(notificacionList)) {
                    notificacionRepository.crearNotificacionDeCuestionario(paciente.getIdPaciente());
                }

                cuestionarioPorCitaRepository.actualizarOpcionSesion(idCita, idCuestionario);

                ListaIdCitayIdCuestionario.add(idCuestionario);
                ListaIdCitayIdCuestionario.add(idCita);
                ListaIdCitayIdCuestionario.add(verificar);
            }
        }

        return ListaIdCitayIdCuestionario;
    }


    @GetMapping(value = {"/eliminarNotificacionCuestionario"})
    @ResponseBody
    void eliminarNotificacionDeCuestionario(Model model, HttpSession session, Authentication authentication) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);
        List<Notificacion> notificacionList=notificacionRepository.BuscarporTipoNoti();

        for (int i = 0; i < notificacionList.size(); i++) {
            notificacionRepository.eliminarNotificacionDeCuestionario(notificacionList.get(0).getId_notificacion());
        }


    }
    //Fin notificaciones

    /* FUNCIONES UTILIZADAS */
    List<Doctor> buscarDoctores(int modalidad, int idSede, int idEspecialidad) {
        if (modalidad == 0) {
            return doctorRepository.findBySede_IdSedeAndEspecialidad_IdEspecialidad(idSede, idEspecialidad);
        } else {
            return doctorRepository.buscarVirtualesPorSedeYEspecialidad(idSede, idEspecialidad);
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        boolean isSecure = false;
        String contextPath = null;
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            isSecure = request.isSecure();
            contextPath = request.getContextPath();
        }
        SecurityContext context = SecurityContextHolder.getContext();
        SecurityContextHolder.clearContext();
        context.setAuthentication(null);
        if (response != null) {
            Cookie cookie = new Cookie("JSESSIONID", null);
            String cookiePath = StringUtils.hasText(contextPath) ? contextPath : "/";
            cookie.setPath(cookiePath);
            cookie.setMaxAge(0);
            cookie.setSecure(isSecure);
            response.addCookie(cookie);
        }
    }
}