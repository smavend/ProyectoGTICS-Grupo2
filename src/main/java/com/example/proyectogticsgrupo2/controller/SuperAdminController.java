package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.dto.*;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import com.example.proyectogticsgrupo2.service.CorreoService;
import com.example.proyectogticsgrupo2.service.CorreoServiceSuperAdmin;
import com.example.proyectogticsgrupo2.service.SuperAdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.proyectogticsgrupo2.config.SecurityConfig;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin(origins = "http://localhost:8080")
@Controller
@RequestMapping("/SuperAdminHomePage")
public class SuperAdminController {
    final PacienteRepository pacienteRepository;
    final AdministrativoRepository administrativoRepository;
    final AdministradorRepository administradorRepository;
    final DoctorRepository doctorRepository;
    final ClinicaRepository clinicaRepository;
    final SedeRepository sedeRepository;
    final EspecialidadRepository especialidadRepository;
    final SuperAdminService superAdminService;
    final AdministrativoPorEspecialidadPorSedeRepository administrativoPorEspecialidadPorSedeRepository;
    final SecurityConfig securityConfig;
    final CredencialesRepository credencialesRepository;
    final StylevistasRepository stylevistasRepository;

    final FormularioJsonRepository formularioJsonRepository;

    @Autowired
    private UserDetailsService userDetailsService;

/*
    final FormDinamicoRepository formDinamicoRepository;
*/

    public SuperAdminController(PacienteRepository pacienteRepository,
                                AdministrativoRepository administrativoRepository,
                                AdministradorRepository administradorRepository,
                                DoctorRepository doctorRepository,
                                ClinicaRepository clinicaRepository,
                                SedeRepository sedeRepository,
                                EspecialidadRepository especialidadRepository,
                                SuperAdminService superAdminService,
                                AdministrativoPorEspecialidadPorSedeRepository administrativoPorEspecialidadPorSedeRepository,
                                SecurityConfig securityConfig,
                                CredencialesRepository credencialesRepository, StylevistasRepository stylevistasRepository,
                                FormularioJsonRepository formularioJsonRepository,
                                UserDetailsService userDetailsService) {
        this.pacienteRepository = pacienteRepository;
        this.administradorRepository = administradorRepository;
        this.administrativoRepository = administrativoRepository;
        this.doctorRepository = doctorRepository;
        this.clinicaRepository = clinicaRepository;
        this.sedeRepository = sedeRepository;
        this.especialidadRepository = especialidadRepository;
        this.superAdminService = superAdminService;
        this.administrativoPorEspecialidadPorSedeRepository = administrativoPorEspecialidadPorSedeRepository;
        this.securityConfig = securityConfig;
        this.credencialesRepository = credencialesRepository;
        this.stylevistasRepository = stylevistasRepository;
        this.formularioJsonRepository = formularioJsonRepository;
        this.userDetailsService = userDetailsService;

    }

    @GetMapping("")
    public String HomePageSuperAdmin(Model model) throws IOException {
        List<Credenciales> credencialesDoctorIds = credencialesRepository.findAll();
        List<String> listaCredenciales = new ArrayList<>();
        for (Credenciales credencial : credencialesDoctorIds) {
            listaCredenciales.add(credencial.getId_credenciales());
        }

        List<AdministrativoDTO_superadmin> listaAdministrativoDTO_superadmin = superAdminService.obtenerTodosLosAdministrativosDTO();

        List<PacienteDTO_superadmin> listaPacienteDTO_superadmin = superAdminService.obtenerTodosLosPacientesDTO();
        List<DoctorDTO_superadmin> listaDoctorDTO_superadmin = superAdminService.obtenerTodosLosDoctoresDTO();
        List<AdministradorDTO_superadmin> listaAdministradoresDTO_superadmin = superAdminService.obtenerTodosLosAdministradoresDTO();
        List<Clinica> listaClinicas = clinicaRepository.findAll();
        List<Sede> listaSedes = sedeRepository.findAll();
        List<Especialidad> listaEspecialidad = especialidadRepository.findAll();

        // Para cada DoctorDTO_superadmin, comprueba si su id está en listaCredenciales y establece showLoguearButton en consecuencia
        for (DoctorDTO_superadmin doctorDTO : listaDoctorDTO_superadmin) {
            doctorDTO.setShowLoguearButton(listaCredenciales.contains(doctorDTO.getIdDoctor()));
        }
        for (AdministradorDTO_superadmin administradoresDTO : listaAdministradoresDTO_superadmin){
            administradoresDTO.setShowLoguearButton(listaCredenciales.contains(administradoresDTO.getIdAdministrador()));
        }
        for (AdministrativoDTO_superadmin administrativoDTO : listaAdministrativoDTO_superadmin){
            administrativoDTO.setShowLoguearButton(listaCredenciales.contains(administrativoDTO.getIdAdministrativo()));
        }
        for (PacienteDTO_superadmin pacienteDTO : listaPacienteDTO_superadmin){
            pacienteDTO.setShowLoguearButton(listaCredenciales.contains(pacienteDTO.getIdPaciente()));
        }
        model.addAttribute("listaClinicas", listaClinicas);
        model.addAttribute("listaSedes", listaSedes);
        model.addAttribute("listaEspecialidad", listaEspecialidad);

        model.addAttribute("listaAdministrativoDTO_superadmin", listaAdministrativoDTO_superadmin);

        model.addAttribute("listaPacienteDTO_superadmin", listaPacienteDTO_superadmin);
        model.addAttribute("listaDoctorDTO_superadmin", listaDoctorDTO_superadmin);
        model.addAttribute("listaAdministradoresDTO_superadmin", listaAdministradoresDTO_superadmin);

        Optional<Stylevistas> style = stylevistasRepository.findById(1);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();
            System.out.println("El color del encabezado es: " + styleActual.getHeader());
            model.addAttribute("headerColor", styleActual.getHeader());
            model.addAttribute("backgroundColor", styleActual.getBackground());
        } else {
            System.out.println("No se encontró stylevistas con el id proporcionado");
        }

        return "superAdmin/superadmin_Dashboard";
    }


    @GetMapping("/administradores")
    @ResponseBody
    public List<AdministradorDTO_superadmin> obtenerAdministradores() {
        return superAdminService.obtenerTodosLosAdministradoresDTO();
    }

    @GetMapping("/administrativos")
    @ResponseBody
    public List<AdministrativoDTO_superadmin> obtenerAdministrativos() {
        return superAdminService.obtenerTodosLosAdministrativosDTO();
    }

    @GetMapping("/pacientes")
    @ResponseBody
    public List<PacienteDTO_superadmin> obtenerPacientes() {
        return superAdminService.obtenerTodosLosPacientesDTO();
    }

    @GetMapping("/doctores")
    @ResponseBody
    public List<DoctorDTO_superadmin> obtenerDoctores() {
        return superAdminService.obtenerTodosLosDoctoresDTO();
    }

    @PostMapping("/filtrar")
    public String filtrarAdministrativos(@RequestParam("clinica") String clinicaId,
                                         @RequestParam("sede") String sedeId,
                                         @RequestParam("especialidad") String especialidadId,
                                         @RequestParam("nombre") String nombre,
                                         Model model) {
        // Aquí puedes agregar tu lógica para filtrar los administrativos según los parámetros recibidos

        // Ejemplo:
        List<AdministrativoDTO_superadmin> listaFiltrada = superAdminService.filtrarAdministrativos(clinicaId, sedeId, especialidadId, nombre);
        model.addAttribute("listaAdministrativoDTO_superadmin", listaFiltrada);

        // Retorna solo el fragmento HTML que contiene la tabla con los administrativos filtrados
        return "superAdmin/_administrativos_table";
    }

    @PostMapping("/filtrarPaciente")
    public String filtrarPacientes(@RequestParam("clinica") String clinicaId,
                                   @RequestParam("sede") String sedeId,
                                   @RequestParam("nombre") String nombre,
                                   Model model) {
        // Aquí puedes agregar tu lógica para filtrar los administrativos según los parámetros recibidos

        // Ejemplo:
        List<PacienteDTO_superadmin> listaFiltrada = superAdminService.filtrarPacientes(clinicaId, sedeId, nombre);
        model.addAttribute("listaPacienteDTO_superadmin", listaFiltrada);

        // Retorna solo el fragmento HTML que contiene la tabla con los administrativos filtrados
        return "superAdmin/_pacientes_table";
    }

    @PostMapping("/filtrarDoctores")
    public String filtrarDoctores(@RequestParam("clinica") String clinicaId,
                                  @RequestParam("sede") String sedeId,
                                  @RequestParam("nombre") String nombre,
                                  @RequestParam("especialidad") String especialidadId,
                                  Model model) {
        // Aquí puedes agregar tu lógica para filtrar los administrativos según los parámetros recibidos

        // Ejemplo:
        List<DoctorDTO_superadmin> listaFiltrada = superAdminService.filtrarDoctores(clinicaId, sedeId, nombre, especialidadId);
        model.addAttribute("listaDoctorDTO_superadmin", listaFiltrada);

        // Retorna solo el fragmento HTML que contiene la tabla con los administrativos filtrados
        return "superAdmin/_doctores_table";
    }

    @PostMapping("/filtrarAdministradores")
    public String filtrarAdministradores(@RequestParam("clinica") String clinicaId,
                                         @RequestParam("sede") String sedeId,
                                         @RequestParam("nombre") String nombre,
                                         Model model) {
        // Aquí puedes agregar tu lógica para filtrar los administrativos según los parámetros recibidos

        // Ejemplo:
        List<AdministradorDTO_superadmin> listaFiltrada = superAdminService.filtrarAdministradores(clinicaId, sedeId, nombre);
        model.addAttribute("listaAdministradoresDTO_superadmin", listaFiltrada);

        // Retorna solo el fragmento HTML que contiene la tabla con los administrativos filtrados
        return "superAdmin/_administradores_table";
    }


    @GetMapping("/verforms")
    public String verforms(Model model) {
        Optional<Stylevistas> style = stylevistasRepository.findById(1);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();
            System.out.println("El color del encabezado es: " + styleActual.getHeader());
            model.addAttribute("headerColor", styleActual.getHeader());
            model.addAttribute("backgroundColor", styleActual.getBackground());
        } else {
            System.out.println("No se encontró stylevistas con el id proporcionado");
        }
        List<FormularioJson> formularios = formularioJsonRepository.findAll();
        model.addAttribute("formularios", formularios);

        return "superAdmin/list_forms";
    }


    @PostMapping("/eliminarFormulario/{id}")
    public String eliminarFormulario(
            @PathVariable("id") Integer id,
            Model model) {
        try {
            formularioJsonRepository.deleteById(id);
            model.addAttribute("message", "Formulario eliminado con éxito");
        } catch (Exception e) {
            model.addAttribute("message", "Error al eliminar el formulario: " + e.getMessage());
            return "errorPage";  // cambiar a la página de error que tenga configurada.
        }

        return "redirect:/SuperAdminHomePage/verforms";
    }
    @PostMapping("/guardarFormulario")
    public String guardarFormulario(
            @RequestParam("titulo") String titulo,
            @RequestParam("estructura_formulario") String estructuraFormulario,
            @RequestParam("rutaController") String rutaController,  // Nuevo parámetro
            Model model) {

        FormularioJson formularioJson = new FormularioJson();
        formularioJson.setTitulo(titulo);
        formularioJson.setEstructura_formulario(estructuraFormulario);
        formularioJson.setRutaController(rutaController);  // Añade la ruta del controlador
        formularioJson.setSent(0); // Seteamos el valor en 1 siempre.

        try {
            formularioJsonRepository.save(formularioJson);
            model.addAttribute("message", "Formulario guardado con éxito");
        } catch (Exception e) {
            model.addAttribute("message", "Error al guardar el formulario: " + e.getMessage());
            return "errorPage";  // cambiar a la página de error que tenga configurada.
        }

        return "redirect:/SuperAdminHomePage/verforms";
    }
    @GetMapping("/ShowToEditForm/{id}")
    public String mostrarFormulario(
            @PathVariable("id") Integer id,
            Model model) {
        Optional<Stylevistas> style = stylevistasRepository.findById(1);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();
            System.out.println("El color del encabezado es: " + styleActual.getHeader());
            model.addAttribute("headerColor", styleActual.getHeader());
            model.addAttribute("backgroundColor", styleActual.getBackground());
        } else {
            System.out.println("No se encontró stylevistas con el id proporcionado");
        }
        FormularioJson formularioJson = formularioJsonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid formulario Id:" + id));

        // Agregar el formularioJson al modelo
        model.addAttribute("formulario", formularioJson);
        System.out.println("El ID del formulario es: " + formularioJson.getId());

        return "superAdmin/EditForm";
    }
    @GetMapping("/TareaPacientes")
    public String registro(Model model){
        List<Paciente> listaPaciente = pacienteRepository.buscarPorEstado(3);
        model.addAttribute("listaPaciente",listaPaciente);
        Optional<Stylevistas> style = stylevistasRepository.findById(1);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();
            System.out.println("El color del encabezado es: " + styleActual.getHeader());  // Esto imprimirá el valor en tu consola
            model.addAttribute("headerColor", styleActual.getHeader());
            /*model.addAttribute("sidebarColor", styleActual.getSidebar());*/
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
            System.out.println("No se encontró stylevistas con el id proporcionado");
        }
        return "superAdmin/Tareas";
    }
    @GetMapping("/impersonarDoctor/{idDoctor}")
    public String impersonarDoctor(@PathVariable("idDoctor") String idDoctor, HttpSession session) {
        System.out.println("Esta leyendo esto");
        System.out.println("Buscando al doctor con ID: " + idDoctor);

        // Obtén la autenticación actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            System.out.println("Authenticación actual: " + auth.toString());
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("superadmin"))) {
                // El usuario actualmente autenticado es un superadministrador.

                // Busca las credenciales en la base de datos
                Optional<Credenciales> optionalCredenciales = credencialesRepository.findById(idDoctor);
                if (optionalCredenciales.isPresent()) {
                    System.out.println("encontro credenciales");
                    Credenciales credenciales = optionalCredenciales.get();
                    System.out.println("Credenciales encontradas: " + credenciales.toString());

                    // Crea una lista de authorities basada en el rol del doctor y agrega "impersonar"
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority("doctor"));
                    authorities.add(new SimpleGrantedAuthority("impersonar"));

                    System.out.println("Lista de authorities creada: " + authorities.toString());

                    // Busca al doctor por su ID
                    Optional<Doctor> doctorOptional = doctorRepository.findById(credenciales.getId_credenciales());
                    if (doctorOptional.isPresent()) {
                        Doctor doctor = doctorOptional.get();
                        // Aquí puedes imprimir los datos del doctor en la consola
                        System.out.println("Datos del doctor: " + doctor.toString());

                        // Establece la autenticación en el contexto de seguridad
                        UsernamePasswordAuthenticationToken doctorAuth =
                                new UsernamePasswordAuthenticationToken(credenciales.getCorreo(), null, authorities);

                        System.out.println("Objeto de autenticación del doctor creado: " + doctorAuth.toString());

                        // Establece el doctor en la sesión
                        session.setAttribute("doctor", doctor);

                        System.out.println("Autenticación establecida en el contexto de seguridad");

                        System.out.println("redirige aqui?");
                        session.setAttribute("doctor", doctor);

                        // Redirige al usuario a la ruta del controlador de dashboard del doctor
                        return "redirect:/doctor/dashboard";
                    } else {
                        // No se encontró el doctor en la base de datos
                        System.out.println("No se encontró el doctor con ID: " + credenciales.getId_credenciales());
                        return "redirect:/SuperAdminHomePage";
                    }
                } else {
                    // Las credenciales no se encontraron en la base de datos
                    System.out.println("No se encontraron las credenciales con ID: " + idDoctor);
                    return "redirect:/SuperAdminHomePage";
                }
            } else {
                // El usuario no está autenticado o no es un superadministrador
                System.out.println("El usuario no está autenticado o no es un superadministrador");
                return "redirect:/login";
            }
        } else {
            System.out.println("Auth es null");
            return "redirect:/login";
        }
    }


    // Switchs hacia los otros roles
    @PostMapping("/switchDoctor/{id_doctor}")
    public String cambiarRolADoctor(@PathVariable String id_doctor, HttpSession session, Authentication authentication) {
        Doctor doctor = doctorRepository.findById(id_doctor).orElse(null);

        if (doctor == null) {
            return "redirect:/error";
        }
        // Establecer un atributo de sesión para indicar que el superadmin está logueado como doctor
        session.setAttribute("superAdminLogueadoComoDoctor", true);
        // Establecer un atributo de sesión para indicar el correo del doctor que está siendo "impersonado"
        session.setAttribute("impersonatedUser", doctor.getCorreo());
        // Redirigir al dashboard del doctor
        return "redirect:/doctor/dashboard";
    }

    @PostMapping("/switchAdministrador/{id}")
    public String cambiarARolAdministrador(@PathVariable("id") String administradorId, Authentication authentication, HttpSession session) {
        Administrador administrador = administradorRepository.findById(administradorId).orElse(null);
        if (administrador == null){
            return "redirect:/error";
        }
        session.setAttribute("superAdminLogueadoComoAdministrador", true);
        session.setAttribute("impersonatedUser", administrador.getCorreo());
        return "redirect:/administrador/dashboard";
    }
    @PostMapping("/switchAdministrativo/{id}")
    public String cambiarARolAdministrativo(@PathVariable("id") String administrativoId, Authentication authentication, HttpSession session) {
        Administrativo administrativo = administrativoRepository.findById(administrativoId).orElse(null);
        if (administrativo == null){
            return "redirect:/error";
        }
        session.setAttribute("superAdminLogueadoComoAdministrativo", true);
        session.setAttribute("impersonatedUser", administrativo.getCorreo());
        return "redirect:/administrativo";
    }

    @PostMapping("/switchPaciente/{id}")
    public String cambiarARolPaciente(@PathVariable("id") String pacienteId, Authentication authentication, HttpSession session) {
        Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
        if (paciente == null){
            return "redirect:/error";
        }
        session.setAttribute("superAdminLogueadoComoPaciente", true);
        session.setAttribute("impersonatedUser", paciente.getCorreo());
        return "redirect:/Paciente";
    }

    ////////////////// returns desde los otros roles
    @PostMapping("/returnToSuperAdmin")
    public String returnToSuperAdmin(HttpSession session) {
        session.removeAttribute("doctor");  // Elimina el atributo de sesión del doctor
        session.removeAttribute("superAdminLogueadoComoAdministrador");  // Añade esta línea

        return "redirect:/SuperAdminHomePage";  // Redirige al SuperAdminHomePage
    }
    @PostMapping("/returnToSuperAdmin_being_Administrador")
    public String returnToSuperAdmin_being_Administrador(HttpSession session) {
        session.removeAttribute("administrador");  // Elimina el atributo de sesión del doctor
        session.removeAttribute("superAdminLogueadoComoDoctor");  // Añade esta línea

        return "redirect:/SuperAdminHomePage";  // Redirige al SuperAdminHomePage
    }

    @PostMapping("/returnToSuperAdmin_being_Administrativo")
    public String returnToSuperAdmin_being_Administrativo(HttpSession session) {
        session.removeAttribute("administrativo");  // Elimina el atributo de sesión del doctor
        session.removeAttribute("superAdminLogueadoComoAdministrativo");  // Añade esta línea
        return "redirect:/SuperAdminHomePage";  // Redirige al SuperAdminHomePage
    }
    @PostMapping("/returnToSuperAdmin_being_Paciente")
    public String returnToSuperAdmin_being_Paciente(HttpSession session) {
        session.removeAttribute("paciente");  // Elimina el atributo de sesión del doctor
        session.removeAttribute("superAdminLogueadoComoPaciente");  // Añade esta línea

        return "redirect:/SuperAdminHomePage";  // Redirige al SuperAdminHomePage
    }


    @PostMapping("/cambioDisponibilidad/{id}")
    public String cambioDisponibilidad(
            @PathVariable("id") Integer id,
            Model model) {
        FormularioJson formularioJson = formularioJsonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid formulario Id:" + id));

        // Cambia el valor de 'sent' a su opuesto (1 si es 0, 0 si es 1)
        formularioJson.setSent(formularioJson.getSent() == 0 ? 1 : 0);

        try {
            formularioJsonRepository.save(formularioJson);
            model.addAttribute("message", "Disponibilidad del formulario actualizada con éxito");
        } catch (Exception e) {
            model.addAttribute("message", "Error al actualizar la disponibilidad del formulario: " + e.getMessage());
            return "errorPage";
        }
        return "redirect:/SuperAdminHomePage/verforms";
    }
    @PostMapping("/actualizarFormulario/{id}")
    public String actualizarFormulario(
            @PathVariable("id") Integer id,
            @RequestParam("titulo") String titulo,
            @RequestParam("estructura_formulario") String estructuraFormulario,
            @RequestParam("rutaController") String rutaController,  // Nuevo parámetro
            Model model) {
        FormularioJson formularioJson = formularioJsonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid formulario Id:" + id));
        formularioJson.setTitulo(titulo);
        formularioJson.setEstructura_formulario(estructuraFormulario);
        formularioJson.setRutaController(rutaController);  // Añade la ruta del controlador
        formularioJson.setSent(0); // Seteamos el valor en 0 siempre.

        try {
            formularioJsonRepository.save(formularioJson);
            model.addAttribute("message", "Formulario actualizado con éxito");
        } catch (Exception e) {
            model.addAttribute("message", "Error al actualizar el formulario: " + e.getMessage());
            return "errorPage";
        }
        return "redirect:/SuperAdminHomePage/verforms";
    }

    @PostMapping("/GuardarPacientes")
    public String guardarPacientes(@RequestParam("pacientes") List<String> pacientesIds,HttpServletRequest request, RedirectAttributes attr) {
        List<HashMap<String, String>> credenciales = new ArrayList<>();
        for(String id : pacientesIds) {
            pacienteRepository.findById(id).ifPresent(paciente -> {
                HashMap<String, String> user = new HashMap<>();

                paciente.setEstado(1);
                pacienteRepository.save(paciente);
                String passRandom = securityConfig.generateRandomPassword();
                PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
                String encodedPassword = passwordEncoder.encode(passRandom);
                // Asegúrate de tener los métodos getIdPaciente y getCorreo en tu clase Paciente
                credencialesRepository.crearCredenciales(paciente.getIdPaciente(), paciente.getCorreo(), encodedPassword);
                CorreoService correoService = new CorreoService();
                InetAddress address = null;
                try {
                    address = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
                String domain = request.getServerName();
                byte[] bIPAddress = address.getAddress();
                String sIPAddress = "";
                for (int i = 0; i < bIPAddress.length; i++){
                    if (i>0) {
                        sIPAddress += ".";
                    }
                    int unsignedByte = bIPAddress[i] & 0xFF;
                    sIPAddress += unsignedByte;
                }
                String link = request.getServerName()+":"+request.getLocalPort();
                System.out.println(link);
                System.out.println("servername:"+domain);
                correoService.props(paciente.getCorreo(),passRandom, link);

                user.put("correo", paciente.getCorreo());
                user.put("pass", passRandom);
                credenciales.add(user);

            });
        }

        attr.addFlashAttribute("credenciales", credenciales);

        return "redirect:/SuperAdminHomePage/TareaPacientes";
    }



    public ResponseEntity<String> guardarFormulario(@RequestBody FormularioJson formularioJson) {

        try {
            formularioJsonRepository.save(formularioJson);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al guardar el formulario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Formulario guardado con éxito", HttpStatus.OK);
    }


    @GetMapping("/verReportes")
    public String ReportList() {
        return "superAdmin/list_reportes";
    }

    @GetMapping("/getFormularios")
    public ResponseEntity<List<FormularioJson>> getFormularios() {
        try {
            List<FormularioJson> formularios = formularioJsonRepository.findAll();
            if (formularios.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(formularios, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/crearformulario")
    public String createForm(Model model) {
        Optional<Stylevistas> style = stylevistasRepository.findById(1);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();
            System.out.println("El color del encabezado es: " + styleActual.getHeader());  // Esto imprimirá el valor en tu consola
/*
                System.out.println("El color del Sidebar es: " + styleActual.getSidebar());  // Esto imprimirá el valor en tu consola
*/
/*
                System.out.println("El color del Background es: " + styleActual.getSidebar());  // Esto imprimirá el valor en tu consola
*/

            model.addAttribute("headerColor", styleActual.getHeader());
/*
                model.addAttribute("sidebarColor", styleActual.getSidebar());
*/
            model.addAttribute("backgroundColor", styleActual.getBackground());

        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
            System.out.println("No se encontró stylevistas con el id proporcionado");
        }

        return "superAdmin/crear_form";
    }

    @GetMapping("/crearReporte")
    public String crearReporte() {
        return "superAdmin/crear_reporte";
    }

    @GetMapping("/SelectClinica")
    public String GestionarUIUX(Model model) {
        List<Stylevistas> listaStylevistas = stylevistasRepository.findAll();
        if (listaStylevistas.isEmpty()) {
            System.out.println("La lista de Stylevistas está vacía.");
        } else {
            System.out.println("La lista de Stylevistas contiene elementos. Primer elemento: " + listaStylevistas.get(0));
        }
        model.addAttribute("listaStylevistas", listaStylevistas);

        Optional<Stylevistas> style = stylevistasRepository.findById(1);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();
            System.out.println("El color del encabezado es: " + styleActual.getHeader());  // Esto imprimirá el valor en tu consola
            model.addAttribute("headerColor", styleActual.getHeader());
/*
            model.addAttribute("sidebarColor", styleActual.getSidebar());
*/

        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
            System.out.println("No se encontró stylevistas con el id proporcionado");
        }
        return "superAdmin/Gestionar_UIUX";
    }

    @GetMapping("/SelectTablaEstilos")
    @ResponseBody
    public List<Stylevistas> obtenerEstilos() {
        return stylevistasRepository.findAll();
    }


    @GetMapping("/perfil")
    public String profile() {
        return "superAdmin/perfil";
    }

    @GetMapping("/Settings")
    public String settings() {
        return "superAdmin/settings";
    }

    @GetMapping("/SignOut")
    public String signout() {
        return "general/home";
    }


    @GetMapping("/EditarEstilo/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Stylevistas stylevistas = stylevistasRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid style ID:" + id));
        model.addAttribute("stylevistas", stylevistas);
        Optional<Stylevistas> style = stylevistasRepository.findById(1);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();
            System.out.println("El color del encabezado es: " + styleActual.getHeader());  // Esto imprimirá el valor en tu consola
/*
            System.out.println("El color del Sidebar es: " + styleActual.getSidebar());  // Esto imprimirá el valor en tu consola
*/

            model.addAttribute("headerColor", styleActual.getHeader());
/*
            model.addAttribute("sidebarColor", styleActual.getSidebar());
*/
            model.addAttribute("backgroundColor", styleActual.getBackground());

        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
            System.out.println("No se encontró stylevistas con el id proporcionado");
        }

        return "superAdmin/EditarEstilo";
    }

    @PostMapping("/EditarEstilo")
    public String updateStylevistas(@ModelAttribute("stylevistas") Stylevistas stylevistas) {
        // Actualiza el registro en la base de datos
        stylevistasRepository.save(stylevistas);
        // Redirige de nuevo a la página que muestra la lista de Stylevistas
        return "redirect:/SuperAdminHomePage/SelectClinica";
    }

    @GetMapping("/CrearUsuario")
    public String CrearUsuario(Model model) {
        userform_superadmin userform = new userform_superadmin();
        model.addAttribute("userform_superadmin", userform);
        List<Sede> listaSedes = sedeRepository.findAll();
        List<Especialidad> listaEspecialidades = especialidadRepository.findAll();
        model.addAttribute("listaSedes", listaSedes);
        model.addAttribute("listaEspecialidades", listaEspecialidades);
        Optional<Stylevistas> style = stylevistasRepository.findById(1);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();
            System.out.println("El color del encabezado es: " + styleActual.getHeader());  // Esto imprimirá el valor en tu consola
            model.addAttribute("headerColor", styleActual.getHeader());
/*
            model.addAttribute("sidebarColor", styleActual.getSidebar());
*/

        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
            System.out.println("No se encontró stylevistas con el id proporcionado");
        }
        return "superAdmin/Crear_Usuario";
    }

    @PostMapping("/getSedesByClinica")
    public String getSedesByClinica(@RequestParam("clinica") String clinicaId, Model model) {
        Clinica clinicafound = clinicaRepository.buscarClinicaPorNombre(clinicaId);
        int clinica_id = clinicafound.getIdClinica();
        List<Sede> listaSedes = sedeRepository.EncontrarListaPorId(clinica_id);

        List<Integer> sedesConAdministrador = administradorRepository.findSedesConAdministrador();

        model.addAttribute("listaSedes", listaSedes);
        model.addAttribute("sedesConAdministrador", sedesConAdministrador);
        return "superAdmin/_sede_select_options";
    }

    @PostMapping("/SaveUser")
    public String saveUser(@ModelAttribute("userform_superadmin") @Valid userform_superadmin userform, BindingResult result, Model model,
                           RedirectAttributes redirectAttributes) {
        model.addAttribute("submitAttempted", true);

        Clinica clinica = clinicaRepository.findAll().stream().findFirst().orElse(null);

        String selectUsuario = userform.getSelectUsuario();
        String sede = userform.getSede();
        String dni = userform.getDni();
        String nombres = userform.getNombres();
        String apellidos = userform.getApellidos();
        String correoUser = userform.getCorreoUser();
        String especialidad = userform.getEspecialidad();

        Optional<Stylevistas> style = stylevistasRepository.findById(1);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();
            System.out.println("El color del encabezado es: " + styleActual.getHeader());  // Esto imprimirá el valor en tu consola
            model.addAttribute("headerColor", styleActual.getHeader());
/*
            model.addAttribute("sidebarColor", styleActual.getSidebar());
*/

        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
            System.out.println("No se encontró stylevistas con el id proporcionado");
        }


        // Valida que los nombres solo contengan caracteres alfabéticos
        if (nombres != null && !nombres.trim().isEmpty() && !nombres.matches("^[a-zA-Z]+(\\s+[a-zA-Z]+)*$")) {
            result.rejectValue("nombres", "", "El campo nombres solo puede contener caracteres alfabéticos.");
        }

        // Valida que los apellidos solo contengan caracteres alfabéticos
        if (apellidos != null && !apellidos.trim().isEmpty() && !apellidos.matches("^[a-zA-Z]+(\\s+[a-zA-Z]+)*$")) {
            result.rejectValue("apellidos", "", "El campo apellidos solo puede contener caracteres alfabéticos.");
        }

        // Valida el tamaño del DNI
        if (dni != null && !dni.trim().isEmpty() && dni.length() != 8) {
            result.rejectValue("dni", "", "DNI debe contener 8 dígitos.");
        }
        // Valida que el DNI solo contenga dígitos
        else if (dni != null && !dni.trim().isEmpty() && !dni.matches("\\d+")) {
            result.rejectValue("dni", "", "DNI debe contener sólo dígitos.");
        }
        System.out.println("el valor de sede a buscar es : " + sede);

        List<Administrador> listaAdministrador2 = administradorRepository.findAll();
        boolean sedeIsNull = false;
        for (Administrador administrador : listaAdministrador2) {
            int existingSede2 = administrador.getSede().getIdSede();
            Sede existingSede = sedeRepository.buscarPorNombreDeSede(sede);
            if (existingSede == null) {
                sedeIsNull = true;
                break;
            } else {
                if ((existingSede.getIdSede() == existingSede2) && selectUsuario.equals("administrador")) {
                    result.rejectValue("sede", "", "Ya existe administrador para esta sede.");
                    break;
                }
            }
        }
        List<Credenciales> listaCredenciales = credencialesRepository.findAll();
        for (Credenciales credencial : listaCredenciales) {
            String existingDni2 = credencial.getId_credenciales();
            if (dni.equals(existingDni2)) {
                result.rejectValue("dni", "", "Ya existe un usuario con esta credencial.");
            }
        }
        List<Administrador> listaAdministrador = administradorRepository.findAll();
        for (Administrador administrador : listaAdministrador) {
            String existingCorreo = administrador.getCorreo();
            String existingDni = administrador.getIdAdministrador(); // Asegúrate de tener este método en tu clase Administrador
            if (correoUser.contains(existingCorreo) && selectUsuario.equals("administrador")) {
                // El correo está duplicado, realiza la acción necesaria
                // Por ejemplo, puedes lanzar una excepción, mostrar un mensaje de error, etc.
                result.rejectValue("correoUser", "", "El correo está duplicado.");
            }
            if (dni.equals(existingDni) && selectUsuario.equals("administrador")) {
                // El DNI está duplicado, realiza la acción necesaria
                result.rejectValue("dni", "", "El DNI está duplicado.");
            }
        }
        List<Administrativo> listaAdministrativo = administrativoRepository.findAll();
        for (Administrativo administrativo : listaAdministrativo) {
            String existingCorreo = administrativo.getCorreo();
            String existingDni = administrativo.getIdAdministrativo(); // Asegúrate de tener este método en tu clase Administrador

            if (correoUser.contains(existingCorreo) && selectUsuario.equals("administrativo")) {
                // El correo está duplicado, realiza la acción necesaria
                // Por ejemplo, puedes lanzar una excepción, mostrar un mensaje de error, etc.
                result.rejectValue("correoUser", "", "El correo está duplicado.");
            }
            if (dni.equals(existingDni) && selectUsuario.equals("administrativo")) {
                // El DNI está duplicado, realiza la acción necesaria
                result.rejectValue("dni", "", "El DNI está duplicado.");
            }
        }


        if (result.hasErrors()) {
            List<Sede> listaSedes = sedeRepository.findAll();
            List<Especialidad> listaEspecialidades = especialidadRepository.findAll();
            model.addAttribute("listaSedes", listaSedes);
            model.addAttribute("listaEspecialidades", listaEspecialidades);
            // Si hay errores de validación, volver a mostrar el formulario con los mensajes de error
            return "superAdmin/Crear_Usuario";
        }


        if (selectUsuario.equals("administrador")) {
            //Clinica clinica_enviar = clinicaRepository.buscarClinicaPorNombre(clinica.getNombre());
            Sede sede_enviar = sedeRepository.buscarPorNombreDeSede(sede);
            administradorRepository.insertarAdministrador(dni, nombres, apellidos, sede_enviar.getIdSede(), correoUser);
            Optional<Administrador> administrador = administradorRepository.findById(dni);
            String passRandom = securityConfig.generateRandomPassword();
            PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
            // Ahora puedes usar el passwordEncoder para codificar una contraseña
            String encodedPassword = passwordEncoder.encode(passRandom);
            credencialesRepository.crearCredenciales(administrador.get().getIdAdministrador(), administrador.get().getCorreo(), encodedPassword);
            CorreoServiceSuperAdmin correoService = new CorreoServiceSuperAdmin();
            correoService.props(administrador.get().getCorreo(), passRandom);

            List<HashMap<String, String>> credenciales = new ArrayList<>();
            HashMap<String, String> user = new HashMap<>();
            user.put("correo", administrador.get().getCorreo());
            user.put("pass", passRandom);
            credenciales.add(user);

            redirectAttributes.addFlashAttribute("credenciales", credenciales);


        } else if (selectUsuario.equals("administrativo")) {
            Administrativo administrativonuevo = new Administrativo();
            administrativonuevo.setIdAdministrativo(dni);
            administrativonuevo.setNombre(nombres);
            administrativonuevo.setApellidos(apellidos);
            administrativonuevo.setCorreo(correoUser);
            administrativonuevo.setEstado(1);
            administrativoRepository.save(administrativonuevo);
//          Clinica clinica_enviar = clinicaRepository.buscarClinicaPorNombre(clinica.getNombre());
            Sede sede_enviar = sedeRepository.buscarPorNombreDeSede(sede);
            AdministrativoPorEspecialidadPorSede administrativoPorEspecialidadPorSede = new AdministrativoPorEspecialidadPorSede();
            administrativoPorEspecialidadPorSede.setSedeId(sede_enviar);
            administrativoPorEspecialidadPorSede.setAdministrativoId(administrativonuevo);
            administrativoPorEspecialidadPorSede.setTorre("Por Asignar");
            administrativoPorEspecialidadPorSede.setPiso("Por Asignar");
            String torre = "N.D";
            String piso = "N.D";
            Especialidad especialidad_enviar = especialidadRepository.findByNombre(especialidad);
            administrativoPorEspecialidadPorSede.setEspecialidadId(especialidad_enviar);
            administrativoPorEspecialidadPorSedeRepository.insertarTablaAdministrativoXEspecialidadXSede(sede_enviar.getIdSede(), administrativonuevo.getIdAdministrativo(), String.valueOf(especialidad_enviar.getIdEspecialidad()), torre, piso);

            String passRandom = securityConfig.generateRandomPassword();
            PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
            // Ahora puedes usar el passwordEncoder para codificar una contraseña
            String encodedPassword = passwordEncoder.encode(passRandom);
            credencialesRepository.crearCredenciales(administrativonuevo.getIdAdministrativo(), administrativonuevo.getCorreo(), encodedPassword);
            CorreoServiceSuperAdmin correoService = new CorreoServiceSuperAdmin();
            correoService.props(administrativonuevo.getCorreo(), passRandom);

            List<HashMap<String, String>> credenciales = new ArrayList<>();
            HashMap<String, String> user = new HashMap<>();
            user.put("correo", administrativonuevo.getCorreo());
            user.put("pass", passRandom);
            credenciales.add(user);

            redirectAttributes.addFlashAttribute("credenciales", credenciales);

        }

        // ... (por ejemplo, guarda el usuario en la base de datos)
        return "redirect:/SuperAdminHomePage";

    }


}