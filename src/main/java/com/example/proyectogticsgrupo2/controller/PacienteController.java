package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.config.SecurityConfig;
import com.example.proyectogticsgrupo2.dto.HorariosDisponiblesDTO;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/Paciente")
public class PacienteController {

    String idPrueba = "10203010";

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
    final SecurityConfig securityConfig;

    public PacienteController(PacienteRepository pacienteRepository, EspecialidadRepository especialidadRepository, SedeRepository sedeRepository, AlergiaRepository alergiaRepository, SeguroRepository seguroRepository, DistritoRepository distritoRepository, DoctorRepository doctorRepository, PacientePorConsentimientoRepository pacientePorConsentimientoRepository, CitaRepository citaRepository, PagoRepository pagoRepository, CitaTemporalRepository citaTemporalRepository, HorarioRepository horarioRepository, CredencialesRepository credencialesRepository, SecurityConfig securityConfig) {
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
        this.securityConfig = securityConfig;
    }

    /* INICIO */
    @GetMapping(value = {"", "/", "/index"})
    public String index(Model model) {

        Paciente paciente = pacienteRepository.findById(idPrueba).get();
        model.addAttribute("paciente", paciente);

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
                              Model model) {

        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }

        model.addAttribute("sedeList", sedeRepository.findAll());
        model.addAttribute("especialidadList", especialidadRepository.findAll());
        return "paciente/reservar1";
    }

    @PostMapping("/reservar1")
    public String reservar1Post(@ModelAttribute("citaTemporal") @Valid CitaTemporal citaTemporal, BindingResult bindingResult,
                                Model model) {

        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        model.addAttribute("paciente", optionalPaciente.get());

        List<Doctor> doctoresDisponibles;

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
                                Model model) {

        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        model.addAttribute("paciente", optionalPaciente.get());

        if (bindingResult.hasErrors()) {

            System.out.println("Ha ocurrido un error");
            List<Doctor> doctoresDisponibles = buscarDoctores(citaTemporal.getModalidad(), citaTemporal.getIdSede(), citaTemporal.getIdEspecialidad());
            model.addAttribute("doctoresDisponibles", doctoresDisponibles);

            return "paciente/reservar2";

        } else if (buscarDoctores(citaTemporal.getModalidad(), citaTemporal.getIdSede(), citaTemporal.getIdEspecialidad()).size() == 0) {
            // * Validar lo que ocurre si no hay doctores disponibles para ciertos casos
            return "paciente/reservar2";

        } else {

            HorariosDisponiblesDTO horariosDisponibles = horarioRepository.buscarHorariosDisponibles(citaTemporal.getIdDoctor());
            model.addAttribute("horariosDisponibles", horariosDisponibles);

            return "paciente/reservar3";
        }
    }

    @PostMapping("/reservar3")
    public String reservar(@ModelAttribute("citaTempora") CitaTemporal citaTemporal) {

        String inicioString = citaTemporal.getFecha().toString() + ' ' + citaTemporal.getHora().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime inicio = LocalDateTime.parse(inicioString, formatter);
        LocalDateTime fin = inicio.plusHours(1);

        citaRepository.reservarCita(citaTemporal.getIdPaciente(), citaTemporal.getIdDoctor(), inicio, fin, citaTemporal.getModalidad(), citaTemporal.getIdSede());
        pagoRepository.nuevoPago(citaRepository.obtenerUltimoId());

        return "redirect:/Paciente/confirmacion";
    }

    /* SECCIÓN PERFIL */
    @GetMapping("/perfil")
    public String perfil(Model model) {
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            List<Alergia> alergiasPaciente = alergiaRepository.buscarPorPacienteId(idPrueba);
            List<Cita> historialCitas = citaRepository.buscarHistorialDeCitas(idPrueba);
            model.addAttribute("alergiasPaciente", alergiasPaciente);
            model.addAttribute("paciente", paciente);
            model.addAttribute("historialCitas", historialCitas);
        }
        return "paciente/perfil";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil(@ModelAttribute("paciente") Paciente paciente,
                               @RequestParam(name = "id") String idPaciente,
                               Model model) {
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPaciente);
        if (optionalPaciente.isPresent()) {
            paciente = optionalPaciente.get();
            List<Seguro> seguroList = seguroRepository.findAll();
            List<Alergia> alergiasPaciente = alergiaRepository.buscarPorPacienteId(idPrueba);
            List<Distrito> distritoList = distritoRepository.findAll();

            model.addAttribute("seguroList", seguroList);
            model.addAttribute("alergiasPaciente", alergiasPaciente);
            model.addAttribute("paciente", paciente);
            model.addAttribute("distritoList", distritoList);
            return "paciente/perfilEditar";
        }
        return "redirect:/Paciente/perfil";
    }

    @PostMapping("/perfil/guardar")
    public String guardarPerfil(@ModelAttribute("paciente") @Valid Paciente paciente,
                                BindingResult bindingResult,
                                @RequestParam(name = "archivo") MultipartFile file,
                                RedirectAttributes attr,
                                Model model) {

        Optional<Paciente> optionalPaciente = pacienteRepository.findById(paciente.getIdPaciente());
        Paciente p = optionalPaciente.get();

        String fileName = file.getOriginalFilename();

        if (fileName.contains("..") || fileName.contains(" ")) {
            attr.addFlashAttribute("msgError", "El archivo contiene caracteres inválidos");
            return "redirect:/Paciente/perfil/editar?idPaciente=" + paciente.getIdPaciente();
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
                    credencialesRepository.actualizarCorreo(paciente.getIdPaciente(), paciente.getCorreo());
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
    }

    @PostMapping("/perfil/guardarAlergia")
    public String guardarAlergia(Alergia alergia) {
        alergiaRepository.save(alergia);
        return "redirect:/Paciente/perfil/editar?idPaciente=" + alergia.getPaciente().getIdPaciente();
    }

    @GetMapping("/perfil/borrarAlergia")
    public String borrarAlergia(@RequestParam(name = "idPaciente") String idPaciente,
                                @RequestParam(name = "idAlergia") int idAlergia) {
        Optional<Alergia> optionalAlergia = alergiaRepository.findById(idAlergia);
        if (optionalAlergia.isPresent()) {
            alergiaRepository.deleteById(idAlergia);
        }
        return "redirect:/Paciente/perfil/editar?idPaciente=" + idPaciente;
    }

    @GetMapping("/perfil/quitarFoto")
    public String quitarFoto(@RequestParam(name = "id") String idPaciente,
                             RedirectAttributes attr) {

        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPaciente);
        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            try {
                File foto = new File("src/main/resources/static/assets/img/userPorDefecto.jpg");
                FileInputStream input = new FileInputStream(foto);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) != -1) {
                    output.write(buffer, 0, length);
                }
                input.close();
                output.close();
                byte[] bytes = output.toByteArray();

                paciente.setFoto(bytes);
                paciente.setFotoname("userPorDefecto.jpg");
                paciente.setFotocontenttype("image/jpg");

                pacienteRepository.save(paciente);
                return "redirect:/Paciente/perfil";
            } catch (IOException e) {
                e.printStackTrace();
                attr.addFlashAttribute("msgError", "Ocurrió un error al subir el archivo");
                return "redirect:/Paciente/perfil";
            }
        }

        return "redirect:/Paciente/perfil";
    }

    @GetMapping("/perfil/cambiarContrasena")
    public String cambiarContrasena(@RequestParam("id") String idPaciente,
                                    Model model) {
        Paciente paciente = pacienteRepository.findById(idPrueba).get();
        model.addAttribute("paciente", paciente);
        return "paciente/perfilContrasena";
    }

    @PostMapping("/perfil/guardarContrasena")
    public String guardarContrasena(@RequestParam("actual") String actual,
                                    @RequestParam("nueva1") String nueva1,
                                    @RequestParam("nueva2") String nueva2,
                                    RedirectAttributes attr) {

        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String contrasenaHasheada = passwordEncoder.encode(actual);
        Credenciales credenciales = credencialesRepository.findByContrasena(contrasenaHasheada);

        if (credenciales != null && nueva1.equals(nueva2)) {
            credencialesRepository.actualizarContrasena(idPrueba, passwordEncoder.encode(nueva1));
            attr.addFlashAttribute("msgActualizacion", "Contraseña actualizada correctamente");
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
                              Model model) {
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }

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
    public String verPerfilDoctor(Model model,
                                  @RequestParam("doc") String idDoctor) {
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        Paciente paciente = optionalPaciente.get();
        model.addAttribute("paciente", paciente);

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
                                  Model model) {

        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        Paciente paciente = optionalPaciente.get();
        model.addAttribute("paciente", paciente);

        return "paciente/reservarDoctor1";
    }

    @PostMapping("/reservarDoctor2")
    public String reservarDoctor2(@ModelAttribute("citaTemporal") @Valid CitaTemporal citaTemporal, BindingResult bindingResult,
                                  Model model) {

        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        Paciente paciente = optionalPaciente.get();
        model.addAttribute("paciente", paciente);

        if (bindingResult.hasErrors()) {
            return "paciente/reservarDoctor1";
        } else {

            String inicioString = citaTemporal.getFecha().toString() + ' ' + citaTemporal.getHora().toString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime inicio = LocalDateTime.parse(inicioString, formatter);
            LocalDateTime fin = inicio.plusHours(1);

            citaRepository.reservarCita(citaTemporal.getIdPaciente(), citaTemporal.getIdDoctor(), inicio, fin, citaTemporal.getModalidad(), citaTemporal.getIdSede());
            pagoRepository.nuevoPago(citaRepository.obtenerUltimoId());

            return "redirect:/Paciente/confirmacion";
        }

    }

    @GetMapping("/confirmacion")
    public String confirmarReserva(Model model) {
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/confirmacion";
    }

    @GetMapping("/sesionVirtual")
    public String sesionVirtual(Model model) {
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/sesionVirtual";
    }

    /* SECCIÓN CITAS */
    @GetMapping("citas")
    public String verCitas(Model model) {
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            List<Cita> proximasCitas = citaRepository.buscarProximasCitas(idPrueba);

            model.addAttribute("proximasCitas", proximasCitas);
            model.addAttribute("paciente", paciente);
        }
        return "paciente/citas";
    }

    /* SECCIÓN PAGOS */
    @GetMapping("/pagos")
    public String pagos(Model model) {
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        List<Pago> pagoList = pagoRepository.findAll();
        model.addAttribute("pagoList", pagoList);
        return "paciente/pagos";
    }

    @GetMapping("/filtrarPagos")
    public String filtrarPagos(@RequestParam("filtro") int filtro, Model model) {
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        List<Pago> pagoList = pagoRepository.findAll();
        model.addAttribute("pagoList", pagoList);
        model.addAttribute("filtro", filtro);
        return "paciente/pagos";
    }

    @PostMapping("/guardarPago")
    public String guardarPago(@RequestParam("idPago") int idPago) {
        pagoRepository.guardarPago(idPago);
        return "redirect:/Paciente/pagos";
    }

    @GetMapping("/recibo")
    public String verReciboPago(@RequestParam("idPago") int idPago,
                                Model model) {
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        Optional<Pago> optionalPago = pagoRepository.findById(idPago);
        if (optionalPago.isPresent()) {
            Pago pago = optionalPago.get();
            model.addAttribute("pago", pago);
        }
        return "paciente/recibo";
    }

    /* SECCIÓN CUESTIONARIOS */
    @GetMapping("/cuestionarios")
    public String cuestionarios(Model model) {
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/cuestionarios";
    }

    @GetMapping("/completarCuestionario")
    public String completarCuestionario(Model model) {
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/completarCuestionario";
    }

    /* SECCIÓN CONSENTIMIENTOS */
    @GetMapping("/consentimientos")
    public String consentimientos(Model model) {
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            List<PacientePorConsentimiento> consentimientos = pacientePorConsentimientoRepository.findByIdIdPaciente(idPrueba);

            model.addAttribute("consentimientos", consentimientos);
            model.addAttribute("paciente", paciente);
        }
        return "paciente/consentimientos";
    }

    @PostMapping("/consentimientos/actualizar")
    public String actualizarConsentimientos(@RequestParam Map<String, Boolean> consentimientos,
                                            Model model,
                                            RedirectAttributes attr) {

        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        Paciente paciente = optionalPaciente.get();

        pacientePorConsentimientoRepository.actualizarConsentimientosANegativo(idPrueba);
        for (String key : consentimientos.keySet()) {
            if (key.length() == 1) {
                pacientePorConsentimientoRepository.actualizarConsentimientoAPositivo(idPrueba, key);
            }
        }

        attr.addFlashAttribute("msgActualizacion", "Consentimientos actualizados correctamente");
        model.addAttribute("paciente", paciente);

        return "redirect:/Paciente/consentimientos";
    }

    /* SECCIÓN MENSAJERÍA */

    @GetMapping("/mensajeria")
    public String mensajeria(Model model) {
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();

            model.addAttribute("paciente", paciente);
        }
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