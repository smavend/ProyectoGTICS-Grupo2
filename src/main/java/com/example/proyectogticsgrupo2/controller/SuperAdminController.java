package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.dto.*;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import com.example.proyectogticsgrupo2.service.SuperAdminService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

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

    public SuperAdminController(PacienteRepository pacienteRepository,
                                AdministrativoRepository administrativoRepository,
                                AdministradorRepository administradorRepository,
                                DoctorRepository doctorRepository,
                                ClinicaRepository clinicaRepository,
                                SedeRepository sedeRepository,
                                EspecialidadRepository especialidadRepository,
                                SuperAdminService superAdminService,
                                AdministrativoPorEspecialidadPorSedeRepository administrativoPorEspecialidadPorSedeRepository) {
        this.pacienteRepository = pacienteRepository;
        this.administradorRepository = administradorRepository;
        this.administrativoRepository = administrativoRepository;
        this.doctorRepository = doctorRepository;
        this.clinicaRepository = clinicaRepository;
        this.sedeRepository = sedeRepository;
        this.especialidadRepository = especialidadRepository;
        this.superAdminService = superAdminService;
        this.administrativoPorEspecialidadPorSedeRepository = administrativoPorEspecialidadPorSedeRepository;
    }

    @GetMapping("")
    public String HomePageSuperAdmin(Model model) {
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
        return "superAdmin/superadmin_Dashboard";
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

    @GetMapping("/selectClinica")
    public String GestionarUIUX() {
        return "superAdmin/Gestionar_UIUX";
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

    @GetMapping("/SelectClinica")
    public String SelectClinica() {
        return "superAdmin/Gestionar_UIUX";
    }

    @GetMapping("/CrearUsuario")
    public String CrearUsuario(Model model) {
        userform_superadmin userform = new userform_superadmin();
        model.addAttribute("userform_superadmin", userform);
        List<Sede> listaSedes = sedeRepository.findAll();
        List<Especialidad> listaEspecialidades = especialidadRepository.findAll();
        model.addAttribute("listaSedes", listaSedes);
        model.addAttribute("listaEspecialidades", listaEspecialidades);
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

        Sede existingSede = sedeRepository.buscarPorNombreDeSede(sede);
        if (existingSede != null && selectUsuario.equals("administrador")) {
            result.rejectValue("sede", "", "Ya existe un administrador para esta sede, cambie.");
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
        }else if (selectUsuario.equals("administrativo")) {
            Administrativo administrativonuevo = new Administrativo();
            administrativonuevo.setIdAdministrativo(dni);
            administrativonuevo.setNombre(nombres);
            administrativonuevo.setApellidos(apellidos);
            administrativonuevo.setCorreo(correoUser);
            administrativoRepository.save(administrativonuevo);
//            Clinica clinica_enviar = clinicaRepository.buscarClinicaPorNombre(clinica.getNombre());
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
        }
            // ... (por ejemplo, guarda el usuario en la base de datos)
        return "redirect:/SuperAdminHomePage";

    }


}