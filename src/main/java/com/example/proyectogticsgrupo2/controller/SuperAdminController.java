package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.dto.*;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import com.example.proyectogticsgrupo2.service.CorreoServiceSuperAdmin;
import com.example.proyectogticsgrupo2.service.SuperAdminService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.proyectogticsgrupo2.config.SecurityConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                                CredencialesRepository credencialesRepository, StylevistasRepository stylevistasRepository) {
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
    }

        @GetMapping("")
        public String HomePageSuperAdmin(Model model) throws IOException {
            List<AdministrativoDTO_superadmin> listaAdministrativoDTO_superadmin = superAdminService.obtenerTodosLosAdministrativosDTO();
            List<PacienteDTO_superadmin> listaPacienteDTO_superadmin = superAdminService.obtenerTodosLosPacientesDTO();
            List<DoctorDTO_superadmin> listaDoctorDTO_superadmin = superAdminService.obtenerTodosLosDoctoresDTO();
            List<AdministradorDTO_superadmin> listaAdministradoresDTO_superadmin = superAdminService.obtenerTodosLosAdministradoresDTO();
            List<Clinica> listaClinicas = clinicaRepository.findAll();
            List<Sede> listaSedes = sedeRepository.findAll();
            List<Especialidad> listaEspecialidad = especialidadRepository.findAll();
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
    public String verforms() {
        return "superAdmin/list_forms";
    }

    @GetMapping("/verReportes")
    public String ReportList() {
        return "superAdmin/list_reportes";
    }

    @GetMapping("/crearformulario")
    public String createForm() {
        return "superAdmin/crear_form";
    }

    @GetMapping("/crearReporte")
    public String crearReporte() {
        return "superAdmin/crear_reporte";
    }

    @GetMapping("/SelectClinica")
    public String GestionarUIUX(Model model) {
        List<Stylevistas> listaStylevistas = stylevistasRepository.findAll();
        if(listaStylevistas.isEmpty()) {
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
            model.addAttribute("headerColor", styleActual   .getHeader());
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
            model.addAttribute("headerColor", styleActual   .getHeader());
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
        System.out.println("el valor de sede a buscar es : "+sede);

        List<Administrador> listaAdministrador2 = administradorRepository.findAll();
        boolean sedeIsNull = false;
        for (Administrador administrador : listaAdministrador2) {
            int existingSede2 = administrador.getSede().getIdSede();
            Sede existingSede = sedeRepository.buscarPorNombreDeSede(sede);
            if(existingSede == null){
                sedeIsNull = true;
                break;
            } else {
                if((existingSede.getIdSede() == existingSede2) && selectUsuario.equals("administrador")){
                    result.rejectValue("sede", "", "Ya existe administrador para esta sede.");
                    break;
                }
            }
        }
        List<Credenciales> listaCredenciales = credencialesRepository.findAll();
        for (Credenciales credencial : listaCredenciales){
            String existingDni2 = credencial.getId_credenciales();
            if(dni.equals(existingDni2)){
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
            return "SuperAdmin/Crear_Usuario";
        }



        if (selectUsuario.equals("administrador")) {
            //Clinica clinica_enviar = clinicaRepository.buscarClinicaPorNombre(clinica.getNombre());
            Sede sede_enviar = sedeRepository.buscarPorNombreDeSede(sede);
            administradorRepository.insertarAdministrador(dni, nombres, apellidos, sede_enviar.getIdSede(), correoUser);
            Optional<Administrador> administrador = administradorRepository.findById(dni);
            String passRandom= securityConfig.generateRandomPassword();
            PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
            // Ahora puedes usar el passwordEncoder para codificar una contraseña
            String encodedPassword = passwordEncoder.encode(passRandom);
            credencialesRepository.crearCredenciales(administrador.get().getIdAdministrador(),administrador.get().getCorreo(),encodedPassword);
            CorreoServiceSuperAdmin correoService = new CorreoServiceSuperAdmin();
            correoService.props(administrador.get().getCorreo(),passRandom);


        }else if (selectUsuario.equals("administrativo")) {
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

            String passRandom= securityConfig.generateRandomPassword();
            PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
            // Ahora puedes usar el passwordEncoder para codificar una contraseña
            String encodedPassword = passwordEncoder.encode(passRandom);
            credencialesRepository.crearCredenciales(administrativonuevo.getIdAdministrativo(),administrativonuevo.getCorreo(),encodedPassword);
            CorreoServiceSuperAdmin correoService = new CorreoServiceSuperAdmin();
            correoService.props(administrativonuevo.getCorreo(),passRandom);


        }
            // ... (por ejemplo, guarda el usuario en la base de datos)
        return "redirect:/SuperAdminHomePage";

    }


}