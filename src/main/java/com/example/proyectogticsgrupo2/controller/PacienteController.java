package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.config.SecurityConfig;
import com.example.proyectogticsgrupo2.dto.TorreYPisoDTO;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import com.example.proyectogticsgrupo2.service.CorreoCitaRegistrada;
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
import java.time.LocalDateTime;
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

    public PacienteController(PacienteRepository pacienteRepository, EspecialidadRepository especialidadRepository, SedeRepository sedeRepository, AlergiaRepository alergiaRepository, SeguroRepository seguroRepository, DistritoRepository distritoRepository, DoctorRepository doctorRepository, PacientePorConsentimientoRepository pacientePorConsentimientoRepository, CitaRepository citaRepository, PagoRepository pagoRepository, CitaTemporalRepository citaTemporalRepository, HorarioRepository horarioRepository, CredencialesRepository credencialesRepository, CuestionarioPorCitaRepository cuestionarioPorCitaRepository, CuestionarioRepository cuestionarioRepository, AdministrativoPorEspecialidadPorSedeRepository administrativoPorEspecialidadPorSedeRepository, SecurityConfig securityConfig) {

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
/*
        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
*/
        String impersonatedUser = (String) session.getAttribute("impersonatedUser");
        Boolean superAdminLogueadoComoPaciente = (Boolean) session.getAttribute("superAdminLogueadoComoPaciente");
        if (superAdminLogueadoComoPaciente == null) {
            superAdminLogueadoComoPaciente = false;
        }
        model.addAttribute("superAdminLogueadoComoPaciente", superAdminLogueadoComoPaciente);

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
    public String reservar(HttpSession session, Authentication authentication){

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        return "paciente/reservar0";
    }

    @GetMapping("/reservar")
    public String reservarGet(@ModelAttribute("citaTemporal") CitaTemporal citaTemporal, Model model, HttpSession session, Authentication authentication) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
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
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        if (bindingResult.hasErrors()) {
            System.out.println("Error validacion: "+bindingResult.getAllErrors());
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

/*
        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
*/

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
        Paciente paciente = pacienteRepository.findByCorreo(userEmail);

        session.setAttribute("paciente", paciente);

        if (bindingResult.hasErrors()) {
            System.out.println("Error validacion: "+bindingResult.getAllErrors());
            List<Doctor> doctoresDisponibles = buscarDoctores(citaTemporal.getModalidad(), citaTemporal.getIdSede(), citaTemporal.getIdEspecialidad());
            model.addAttribute("doctoresDisponibles", doctoresDisponibles);

            return "paciente/reservar2";

        } else {

            Doctor doctor = doctorRepository.findById(citaTemporal.getIdDoctor()).get();

            model.addAttribute("sede", sedeRepository.findById(citaTemporal.getIdSede()).get());
            model.addAttribute("especialidad", especialidadRepository.findById(citaTemporal.getIdEspecialidad()).get());
            model.addAttribute("doctor", doctor);
            Float precioBase = administrativoPorEspecialidadPorSedeRepository.buscarPorSedeYEspecialidad(citaTemporal.getIdSede(), citaTemporal.getIdEspecialidad()).getPrecio_cita();
            model.addAttribute("precio", precioBase*paciente.getSeguro().getCoaseguro());

            citaTemporal.setFin(citaTemporal.getInicio().plusMinutes(doctor.getDuracion_cita_minutos()));

            return "paciente/reservar3";
        }
    }

    @PostMapping("/reservar3")
    public String reservar4(@ModelAttribute("citaTemporal") CitaTemporal citaTemporal,
                            Model model, HttpSession session, Authentication authentication,
                            HttpServletRequest request) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
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

        citaRepository.reservarCita(paciente.getIdPaciente(), citaTemporal.getIdDoctor(), inicio, fin, citaTemporal.getModalidad(), citaTemporal.getIdSede(), paciente.getSeguro().getIdSeguro(), especialidad.getIdEspecialidad());
        int idCita = citaRepository.obtenerUltimoId();
        pagoRepository.nuevoPago(idCita, tipoPago);

        // Enviar correo al paciente - inhabilidado para que no demore tanto xd
        /*Cita cita = citaRepository.findById(idCita).get();
        CorreoCitaRegistrada correo = new CorreoCitaRegistrada(administrativoPorEspecialidadPorSedeRepository);
        String host = request.getServerName()+":"+request.getLocalPort();
        correo.props(host, paciente.getCorreo(), cita);
         */

        model.addAttribute("sede", sedeRepository.findById(citaTemporal.getIdSede()).get());
        model.addAttribute("especialidad", especialidad);
        model.addAttribute("doctor", doctorRepository.findById(citaTemporal.getIdDoctor()).get());
        model.addAttribute("precio", administrativoPorEspecialidadPorSedeRepository.buscarPorSedeYEspecialidad(citaTemporal.getIdSede(), citaTemporal.getIdEspecialidad()).getPrecio_cita());

        return "paciente/confirmacion";
    }

    @GetMapping("/pendientes")
    public String citasPendientes(Model model, HttpSession session, Authentication authentication){

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        List<Cita> citasPendientes = citaRepository.buscarCitasPendientes(paciente.getIdPaciente());

        model.addAttribute("citasPendientes", citasPendientes);

        return "paciente/pendientes";
    }

    /* SECCIÓN PERFIL */
    @GetMapping("/perfil")
    public String perfil(Model model, HttpSession session, Authentication authentication) {

        /*Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());
        session.setAttribute("paciente", paciente);*/

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
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

        /*Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());
        session.setAttribute("paciente", paciente);*/

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
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
        Paciente p = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", p);

        String fileName = file.getOriginalFilename();

        if (p.getIdPaciente().equals(paciente.getIdPaciente())) {

            if (fileName.contains("..") || fileName.contains(" ")) {
                attr.addFlashAttribute("msgError", "El archivo contiene caracteres inválidos");
                return "redirect:/Paciente/perfil/editar";
            }

            if (bindingResult.hasErrors()) {
                System.out.println("Error: "+bindingResult.getAllErrors());
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

        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        if (paciente.getIdPaciente().equals(alergia.getPaciente().getIdPaciente())){
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
    public String cambiarContrasena(HttpSession session, Authentication authentication) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        return "paciente/perfilContrasena";
    }

    @PostMapping("/perfil/guardarContrasena")
    public String guardarContrasena(@RequestParam("actual") String contrasenaActual,
                                    @RequestParam("nueva1") String contrasenaNueva1,
                                    @RequestParam("nueva2") String contrasenaNueva2,
                                    RedirectAttributes attr, Authentication authentication,
                                    HttpSession session) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

        Paciente paciente = pacienteRepository.findByCorreo(userEmail);

        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        Credenciales credenciales = credencialesRepository.buscarPorId(paciente.getIdPaciente());
        Credenciales nuevasCredenciales = new Credenciales(credenciales.getId_credenciales(), credenciales.getCorreo(), passwordEncoder.encode(contrasenaNueva1));

        if (passwordEncoder.matches(contrasenaActual, credenciales.getContrasena())) {
            if (contrasenaNueva1.equals(contrasenaNueva2)) {

                if (!contrasenaNueva1.equals("")){
                    credencialesRepository.save(nuevasCredenciales);
                    attr.addFlashAttribute("msgActualizacion", "Contraseña actualizada correctamente");
                    return "redirect:/Paciente/perfil";
                }
                else {
                    attr.addFlashAttribute("erro2", "Ingrese una nueva contraseña válida");
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
    public String verDoctores(@RequestParam("sede") int idSede, @RequestParam("esp") int idEspecialidad, @RequestParam("pag") int pagina, Model model, HttpSession session, Authentication authentication) {

/*
        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
*/
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }
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

        // Obtener disponibilidad de los próximos dos días

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
            httpHeaders.setContentType(MediaType.parseMediaType(doctor.getFotocontenttype()));
            return new ResponseEntity<>(imagenComoBytes, httpHeaders, HttpStatus.OK);
        } else {
            return null;
        }
    }

    @GetMapping("/perfilDoctor")
    public String verPerfilDoctor(@RequestParam("doc") String idDoctor,
                                  Model model, HttpSession session, Authentication authentication) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

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

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        Doctor doctor = doctorRepository.findById(citaTemporal.getIdDoctor()).get();

        model.addAttribute("sede", sedeRepository.findById(citaTemporal.getIdSede()).get());
        model.addAttribute("especialidad", especialidadRepository.findById(citaTemporal.getIdEspecialidad()).get());
        model.addAttribute("doctor", doctor);
        Float precioBase = administrativoPorEspecialidadPorSedeRepository.buscarPorSedeYEspecialidad(citaTemporal.getIdSede(), citaTemporal.getIdEspecialidad()).getPrecio_cita();
        model.addAttribute("precio", precioBase*paciente.getSeguro().getCoaseguro());

        citaTemporal.setFin(citaTemporal.getInicio().plusMinutes(doctor.getDuracion_cita_minutos()));

        return "paciente/reservar3";
    }

    @GetMapping("/confirmacion")
    public String confirmarReserva(HttpSession session, Authentication authentication, Model model) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);
        model.addAttribute("activarModal", true);

        return "paciente/confirmacion";
    }

    @GetMapping("/sesionVirtual")
    public String sesion(@RequestParam("c") Integer idCita,
                         Model model, HttpSession session, Authentication authentication){

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        Cita cita = citaRepository.findById(idCita).get();

        model.addAttribute("link", cita.getLink());

        return "paciente/sesionVirtual";
    }

    /* SECCIÓN CITAS */
    @GetMapping("/citas")
    public String verCitas(Model model, HttpSession session, Authentication authentication) {

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

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

/*
        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
*/
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);
        model.addAttribute("coaseguro", paciente.getSeguro().getCoaseguro());

        List<Pago> pagoList = pagoRepository.buscarPorPaciente(paciente.getIdPaciente());

        model.addAttribute("pagoList", pagoList);
        return "paciente/pagos";
    }

    @GetMapping("/pagar")
    public String pagar(@ModelAttribute("tarjetaPago") TarjetaPago tarjetaPago, @RequestParam("idPago") Integer idPago, Model model, HttpSession session, Authentication authentication) {

/*
        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
*/
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        List<Pago> pagoList = pagoRepository.findAll();
        model.addAttribute("idPagar", idPago);
        model.addAttribute("pagoList", pagoList);
        //model.addAttribute("activarModal", true);
        return "paciente/pagos";
    }

    @PostMapping("/guardarPago")
    public String guardarPago(@ModelAttribute("tarjetaPago") @Valid TarjetaPago tarjetaPago, BindingResult bindingResult,
                              @RequestParam("idPago") int idPago, @RequestParam("fechaStr") String fechaStr,
                              Model model, RedirectAttributes attr, HttpSession session, Authentication authentication) {

        if (bindingResult.hasErrors()) {
/*
            session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
*/
            String userEmail;
            if (session.getAttribute("impersonatedUser") != null) {
                userEmail = (String) session.getAttribute("impersonatedUser");
            } else {
                userEmail = authentication.getName();
            }

            Paciente paciente = pacienteRepository.findByCorreo(userEmail);
            session.setAttribute("paciente", paciente);
            List<Pago> pagoList = pagoRepository.findAll();
            model.addAttribute("pagoList", pagoList);
            model.addAttribute("idPagar", idPago);
            model.addAttribute("activarModal", true);
            model.addAttribute("pagoFilt",1);
            return "paciente/pagos";
        } else {
/*
            session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
*/
            String userEmail;
            if (session.getAttribute("impersonatedUser") != null) {
                userEmail = (String) session.getAttribute("impersonatedUser");
            } else {
                userEmail = authentication.getName();
            }

            Paciente paciente = pacienteRepository.findByCorreo(userEmail);
            session.setAttribute("paciente", paciente);

            pagoRepository.guardarPago(idPago);
            attr.addFlashAttribute("msg", "Pago realizado exitosamente");
            return "redirect:/Paciente/pagos";
        }
    }

    @GetMapping("/recibo")
    public String verReciboPago(@RequestParam("idPago") int idPago, Model model, HttpSession session, Authentication authentication) {

/*
        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
*/

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

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

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);


        List<CuestionarioPorCita> cuestionarioPorCitaList = cuestionarioPorCitaRepository.buscarPorPaciente(paciente.getIdPaciente());

        model.addAttribute("cuestionarios", cuestionarioPorCitaList);

        return "paciente/cuestionarios";
    }

    @GetMapping("/cuestionario")
    public String completarCuestionario(@RequestParam("cues") Integer idCuestionario, @RequestParam("cita") Integer idCita, @ModelAttribute("cuestionario") CuestionarioPorCita cuestionario, Model model, HttpSession session, Authentication authentication) {

/*
        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
*/

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

        model.addAttribute("cuestionario", cuestionarioPorCitaRepository.findByIdIdCuestionarioAndIdIdCita(idCuestionario, idCita));
        model.addAttribute("preguntas", cuestionarioRepository.buscarPorId(idCuestionario));

        return "paciente/cuestionariosCompletar";
    }

    @PostMapping("/guardarCuestionario")
    public String guardarCuestionario(@ModelAttribute("cuestionario") @Valid CuestionarioPorCita cuestionario, BindingResult bindingResult, Model model, HttpSession session, Authentication authentication) {

        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));

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

        /*Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());
        session.setAttribute("paciente", paciente);*/
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);
        List<PacientePorConsentimiento> consentimientos = pacientePorConsentimientoRepository.findByIdIdPaciente(paciente.getIdPaciente());

        model.addAttribute("consentimientos", consentimientos);
        return "paciente/consentimientos";
    }

    @PostMapping("/consentimientos/actualizar")
    public String actualizarConsentimientos(@RequestParam Map<String, Boolean> consentimientos,
                                            RedirectAttributes attr, Authentication authentication,
                                            HttpSession session) {

        /*Paciente paciente = pacienteRepository.findByCorreo(authentication.getName());*/

        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

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
    public String mensajeria(HttpSession session, Authentication authentication) {
/*
        session.setAttribute("paciente", pacienteRepository.findByCorreo(authentication.getName()));
*/
        String userEmail;
        if (session.getAttribute("impersonatedUser") != null) {
            userEmail = (String) session.getAttribute("impersonatedUser");
        } else {
            userEmail = authentication.getName();
        }

        Paciente paciente = pacienteRepository.findByCorreo(userEmail);
        session.setAttribute("paciente", paciente);

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