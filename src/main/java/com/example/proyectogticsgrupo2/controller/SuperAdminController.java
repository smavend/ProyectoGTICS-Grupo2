package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.dto.AdministradorDTO_superadmin;
import com.example.proyectogticsgrupo2.dto.AdministrativoDTO_superadmin;
import com.example.proyectogticsgrupo2.dto.DoctorDTO_superadmin;
import com.example.proyectogticsgrupo2.dto.PacienteDTO_superadmin;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import com.example.proyectogticsgrupo2.service.SuperAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    public SuperAdminController(PacienteRepository pacienteRepository,
                                AdministrativoRepository administrativoRepository,
                                AdministradorRepository administradorRepository,
                                DoctorRepository doctorRepository,
                                ClinicaRepository clinicaRepository,
                                SedeRepository sedeRepository,
                                EspecialidadRepository especialidadRepository,
                                SuperAdminService superAdminService) {
        this.pacienteRepository = pacienteRepository;
        this.administradorRepository = administradorRepository;
        this.administrativoRepository = administrativoRepository;
        this.doctorRepository = doctorRepository;
        this.clinicaRepository = clinicaRepository;
        this.sedeRepository = sedeRepository;
        this.especialidadRepository = especialidadRepository;
        this.superAdminService = superAdminService;
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
        List<AdministrativoDTO_superadmin> listaFiltrada = superAdminService.filtrarAdministrativos(clinicaId, sedeId, especialidadId,nombre);
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
        List<PacienteDTO_superadmin> listaFiltrada = superAdminService.filtrarPacientes(clinicaId, sedeId,nombre);
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
        List<AdministradorDTO_superadmin> listaFiltrada = superAdminService.filtrarAdministradores(clinicaId, sedeId,nombre);
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
        List<Clinica> listaClinicas = clinicaRepository.findAll();
        model.addAttribute("listaClinicas", listaClinicas);
        return "superAdmin/Crear_Usuario";
    }
    @PostMapping("/getSedesByClinica")
    public String getSedesByClinica(@RequestParam("clinica") String clinicaId, Model model) {
        Clinica clinicafound = clinicaRepository.buscarClinicaPorNombre(clinicaId);
        int clinica_id = clinicafound.getIdClinica();
        List<Sede> listaSedes = sedeRepository.EncontrarListaPorId(clinica_id);
        model.addAttribute("listaSedes", listaSedes);
        return "superAdmin/_sede_select_options";
    }
    @PostMapping("/SaveUser")
    public String saveUser(@RequestParam("selectUsuario") String selectUsuario,
                           @RequestParam("dni") String dni,
                           @RequestParam("nombres") String nombres,
                           @RequestParam("apellidos") String apellidos,
                           @RequestParam("clinica") String clinica,
                           @RequestParam(value = "otraClinica", required = false) String otraClinica,
                           @RequestParam(value = "otraSede", required = false) String otraSede,
                           @RequestParam(value = "sede", required = false) String sede) {
        if (selectUsuario.equals("administrador")) {
            // Procesa los datos para un usuario administrador
            if (clinica.equals("otro")) {
                clinicaRepository.insertarClinica(otraClinica);
                Clinica clinicanueva = clinicaRepository.buscarClinicaPorNombre(otraClinica);
                sedeRepository.insertarSede(otraSede, clinicanueva.getIdClinica());
                Sede sedenueva_id = sedeRepository.buscarPorSedeId(String.valueOf(clinicanueva.getIdClinica()));
                administradorRepository.insertarAdministrador(dni,nombres,apellidos,Integer.parseInt(sedenueva_id.getIdSede()));
                // Utiliza los valores de 'otraClinica' y 'otraSede'
            } else {
                // Utiliza el valor de 'clinica'
            }
            // ... (por ejemplo, guarda el usuario en la base de datos)
        } else if (selectUsuario.equals("administrativo")) {
            // Procesa los datos para un usuario administrativo
            if (clinica.equals("otro")) {
                // Utiliza los valores de 'otraClinica' y 'otraSede'
            } else {
                // Utiliza el valor de 'clinica' y, si corresponde, el valor de 'sede'
            }
            // ... (por ejemplo, guarda el usuario en la base de datos)
        }
        return "redirect:/SuperAdminHomePage";

    }


}