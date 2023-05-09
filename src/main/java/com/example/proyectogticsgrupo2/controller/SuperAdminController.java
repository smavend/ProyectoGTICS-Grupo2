package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.dto.*;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import com.example.proyectogticsgrupo2.service.SuperAdminService;
import jakarta.validation.Valid;
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

        List<Clinica> listaClinicas = clinicaRepository.findAll();
        List<Especialidad> listaEspecialidades = especialidadRepository.findAll();
        model.addAttribute("listaClinicas", listaClinicas);
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

    //    public String saveUser(@RequestParam("selectUsuario") String selectUsuario,
//                           @RequestParam("dni") String dni,
//                           @RequestParam("nombres") String nombres,
//                           @RequestParam("apellidos") String apellidos,
//                           @RequestParam("clinica") String clinica,
//                           //falta llenar este campo CorreoUser
//                           @RequestParam("correoUser") String correoUser,
//                           //----------------------->>>>>>>>>>>>>
//                           @RequestParam(value = "otraClinica", required = false) String otraClinica,
//                           //faltan estos 2 parámetros en el html ------>>>>>>>>
//                           @RequestParam(value = "correo_nueva_clinica", required = false) String correo_nueva_clinica,
//                           @RequestParam(value = "telefono_nueva_clinica", required = false) String telefono_nueva_clinica,
//                           @RequestParam(value = "sede_nueva_direccion", required = false) String sede_nueva_direccion,
//                           //-------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//                           @RequestParam(value = "otraSede", required = false) String otraSede,
//                           @RequestParam(value = "sede", required = false) String sede,
//                           @RequestParam(value = "especialidad", required = false) String especialidad,
//                           @RequestParam(value = "otraSede4", required = false) String otraSede4,
//                           @RequestParam(value = "sede_nueva4_direccion", required = false) String sede_nueva4_direccion,
//                           Model model) {
    @PostMapping("/SaveUser")
    public String saveUser(@ModelAttribute("userform_superadmin") @Valid userform_superadmin userform, BindingResult result, Model model,
                           RedirectAttributes redirectAttributes) {


        String selectUsuario = userform.getSelectUsuario();
        String clinica = userform.getClinica();
        String sede = userform.getSede();
        String otraClinica = userform.getOtraClinica();

        // Validación personalizada para el campo 'sede'
        if (selectUsuario.equals("administrador") && clinica.equals("otro")) {
            // En este caso, permitir que el campo 'sede' esté vacío
        } else {
            if (sede == "" || sede.trim().isEmpty()) {
                result.rejectValue("sede", "", "El campo Sede no puede estar vacío.");
            }
        }
        Clinica existingClinica = clinicaRepository.buscarClinicaPorNombre(otraClinica);
        if (existingClinica != null) {
            result.rejectValue("otraClinica", "", "Nombre de Clínica ya utilizada, cambie.");
        }

        if (result.hasErrors()) {
            List<Clinica> listaClinicas = clinicaRepository.findAll();
            List<Especialidad> listaEspecialidades = especialidadRepository.findAll();
            model.addAttribute("listaClinicas", listaClinicas);
            model.addAttribute("listaEspecialidades", listaEspecialidades);
            // Si hay errores de validación, volver a mostrar el formulario con los mensajes de error
            return "SuperAdmin/Crear_Usuario";
        }

        String dni = userform.getDni();
        String nombres = userform.getNombres();
        String apellidos = userform.getApellidos();
        String correoUser = userform.getCorreoUser();
        String correo_nueva_clinica = userform.getCorreo_nueva_clinica();
        String telefono_nueva_clinica = userform.getTelefono_nueva_clinica();
        String otraSede = userform.getOtraSede();
        String sede_nueva_direccion = userform.getSede_nueva_direccion();
        String otraSede4 = userform.getOtraSede4();
        String sede_nueva4_direccion = userform.getSede_nueva4_direccion();
        String especialidad = userform.getEspecialidad();


        if (selectUsuario.equals("administrador")) {
            // Procesa los datos para un usuario administrador
            // ... (por ejemplo, guarda el usuario en la base de datos)
            if (clinica.equals("otro")) {
                /*clinicaRepository.insertarClinica(otraClinica);
                Clinica clinicanueva = clinicaRepository.buscarClinicaPorNombre(otraClinica);
                sedeRepository.insertarSede(otraSede, clinicanueva.getIdClinica());
                Sede sedenueva_id = sedeRepository.buscarPorClinicaId(String.valueOf(clinicanueva.getIdClinica()));
                administradorRepository.insertarAdministrador(dni,nombres,apellidos,sedenueva_id.getIdSede());*/
                Clinica clinicanueva = new Clinica();
                //Parámetros que sí obtengo mediante el post
                clinicanueva.setNombre(otraClinica);
                clinicanueva.setCorreo(correo_nueva_clinica);
                clinicanueva.setTelefono(telefono_nueva_clinica);
                clinicanueva.setTyc("TerminosX");
                clinicanueva.setColor("#FFFFFF");
                clinicaRepository.save(clinicanueva);
                //Buscando clinica nueva
                Clinica clinicaEncontrada = clinicaRepository.buscarClinicaPorNombre(clinicanueva.getNombre());
                Sede sedeNueva = new Sede();
                sedeNueva.setNombre(otraSede);
                sedeNueva.setClinica(clinicaEncontrada);
                sedeNueva.setDireccion(sede_nueva_direccion);
                sedeRepository.save(sedeNueva);
                Sede sedeEncontrada = sedeRepository.buscarPorNombreDeSede(otraSede, clinicaEncontrada.getIdClinica());
                System.out.println(sedeEncontrada.getIdSede());
                System.out.println(sedeEncontrada.getNombre());
                System.out.println("hola!!!!");
                Administrador administradorNuevo = new Administrador();
                administradorNuevo.setIdAdministrador(dni);
                administradorNuevo.setNombre(nombres);
                administradorNuevo.setApellidos(apellidos);
                administradorNuevo.setEstado(0);
                administradorNuevo.setCorreo(correoUser);
                administradorNuevo.setSede(sedeEncontrada);
                administradorRepository.save(administradorNuevo);
                // Utiliza los valores de 'otraClinica' y 'otraSede'
            } else {
                if (sede.equals("otro")) {
                    Clinica clinica_enviar = clinicaRepository.buscarClinicaPorNombre(clinica);
                    Sede sedeNueva = new Sede();
                    sedeNueva.setNombre(otraSede4);
                    sedeNueva.setClinica(clinica_enviar);
                    sedeNueva.setDireccion(sede_nueva4_direccion);
                    sedeRepository.save(sedeNueva);
                    Sede sede_enviar = sedeRepository.buscarPorNombreDeSede(otraSede4, clinica_enviar.getIdClinica());
                    administradorRepository.insertarAdministrador(dni, nombres, apellidos, sede_enviar.getIdSede(), correoUser);

                } else {
                    Clinica clinica_enviar = clinicaRepository.buscarClinicaPorNombre(clinica);
                    Sede sede_enviar = sedeRepository.buscarPorNombreDeSede(sede, clinica_enviar.getIdClinica());
                    administradorRepository.insertarAdministrador(dni, nombres, apellidos, sede_enviar.getIdSede(), correoUser);
                    // Utiliza el valor de 'clinica'
                }
            }
        } else if (selectUsuario.equals("administrativo")) {

            // Procesa los datos para un usuario administrativo
            if (clinica.equals("otro")) {
                //
                // Utiliza los valores de 'otraClinica' y 'otraSede'
            } else {
                //el campo estado
//                administrativoRepository.insertarAdministrativo(dni,nombres,apellidos);
                Administrativo administrativonuevo = new Administrativo();
                administrativonuevo.setIdAdministrativo(dni);
                administrativonuevo.setNombre(nombres);
                administrativonuevo.setApellidos(apellidos);
                administrativonuevo.setCorreo(correoUser);
                administrativoRepository.save(administrativonuevo);
                Clinica clinica_enviar = clinicaRepository.buscarClinicaPorNombre(clinica);
                Sede sede_enviar = sedeRepository.buscarPorNombreDeSede(sede, clinica_enviar.getIdClinica());
                //falta añadir el parámetro Especialidad ( se añadirá como uno por defecto pero se tiene que elegir )
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
//                administrativoPorEspecialidadPorSedeRepository.insertarTablaAdministrativoXEspecialidadXSede(sede_enviar.getIdSede(),dni);
                // Utiliza el valor de 'clinica' y, si corresponde, el valor de 'sede'
            }
            // ... (por ejemplo, guarda el usuario en la base de datos)
        }
        return "redirect:/SuperAdminHomePage";

    }


}