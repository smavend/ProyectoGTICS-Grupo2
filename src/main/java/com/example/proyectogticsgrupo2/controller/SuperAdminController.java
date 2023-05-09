package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.dto.AdministradorDTO_superadmin;
import com.example.proyectogticsgrupo2.dto.AdministrativoDTO_superadmin;
import com.example.proyectogticsgrupo2.dto.DoctorDTO_superadmin;
import com.example.proyectogticsgrupo2.dto.PacienteDTO_superadmin;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import com.example.proyectogticsgrupo2.service.SuperAdminService;
import jakarta.validation.constraints.Size;
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
    @PostMapping("/SaveUser")
    public String saveUser(@RequestParam("selectUsuario") String selectUsuario,
                           @RequestParam("dni") String dni,
                           @RequestParam("nombres") String nombres,
                           @RequestParam("apellidos") String apellidos,
                           @RequestParam("clinica") String clinica,
                           //falta llenar este campo CorreoUser
                           @RequestParam("correoUser") String correoUser,
                           //----------------------->>>>>>>>>>>>>
                           @RequestParam(value = "otraClinica", required = false) String otraClinica,
                           //faltan estos 2 parámetros en el html ------>>>>>>>>
                           @RequestParam(value = "correo_nueva_clinica", required = false) String correo_nueva_clinica,
                           @RequestParam(value = "telefono_nueva_clinica", required = false) String telefono_nueva_clinica,
                           @RequestParam(value = "sede_nueva_direccion", required = false) String sede_nueva_direccion,
                           //-------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                           @RequestParam(value = "otraSede", required = false) String otraSede,
                           @RequestParam(value = "sede", required = false) String sede,
                           @RequestParam(value = "especialidad", required = false) String especialidad,
                           @RequestParam(value = "otraSede4", required = false) String otraSede4,
                           @RequestParam(value = "sede_nueva4_direccion", required = false) String sede_nueva4_direccion,
                           Model model) {
//
//        boolean hasErrors = false;
//
//        if (selectUsuario == null || selectUsuario.isEmpty() || selectUsuario.equals("Seleccionar Usuario")) {
//            model.addAttribute("selectUsuarioError", "*Debe seleccionar un usuario*");
//            hasErrors = true;
//        }
//        if (dni.isEmpty() || !dni.matches("^[0-9]{8}$")) {
//            model.addAttribute("dniError", "*El número de DNI debe tener 8 dígitos y ser numérico*");
//            hasErrors = true;
//        }
//        if (nombres.isEmpty() || !nombres.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")) {
//            model.addAttribute("nombresError", "*El campo Nombres debe contener solo letras*");
//            hasErrors = true;
//        }
//        if (apellidos.isEmpty() || !apellidos.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")) {
//            model.addAttribute("apellidosError", "*El campo Apellidos debe contener solo letras*");
//            hasErrors = true;
//        }
//        if (correoUser.isEmpty() || !correoUser.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
//            model.addAttribute("CorreoError", "*El campo Correo debe contener una dirección de correo electrónico válida*");
//            hasErrors = true;
//        }
//        if (clinica == null || clinica.isEmpty() || clinica.equals("Seleccionar Clínica")) {
//            List<Clinica> listaClinicas = clinicaRepository.findAll();
//            model.addAttribute("listaClinicas", listaClinicas);
//            model.addAttribute("clinicaError", "*Debe seleccionar una clínica*");
//            hasErrors = true;
//        }
//        if (otraClinica.isEmpty() || !otraClinica.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")) {
//            model.addAttribute("otraClinicaError", "*El campo Nombre de la nueva Clínica debe contener solo letras*");
//            hasErrors = true;
//        }
//        if (correo_nueva_clinica.isEmpty() || !correo_nueva_clinica.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
//            model.addAttribute("correo_nueva_clinicaError", "*El campo Correo debe contener una dirección de correo electrónico válida*");
//            hasErrors = true;
//        }
//        if (telefono_nueva_clinica.isEmpty() || !telefono_nueva_clinica.matches("^[0-9]{7}$")) {
//            model.addAttribute("telefono_nueva_clinicaValueError", "*El número de Teléfono debe tener 9 dígitos y ser numérico*");
//            hasErrors = true;
//        }
//        if (otraSede.isEmpty() || !otraSede.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")) {
//            model.addAttribute("otraSedeValueError", "*El campo Nueva Sede debe contener solo letras*");
//            hasErrors = true;
//        }
//        if (sede_nueva_direccion.isEmpty() || !sede_nueva_direccion.matches("^[a-zA-Z0-9\\s°]*$")) {
//            model.addAttribute("sede_nueva_direccionError", "*El campo Dirección de la nueva Sede debe contener solo letras, números o signos");
//            hasErrors = true;
//        }
//        if (sede == null || sede.isEmpty() || sede.equals("Seleccionar Sede")) {
//            List<Clinica> listaClinicas = clinicaRepository.findAll();
//            model.addAttribute("listaClinicas", listaClinicas);
//            model.addAttribute("clinicaError", "*Seleccione una clínica*");
//            model.addAttribute("sedeError", "*Debe seleccionar una Sede*");
//            hasErrors = true;
//        }
//        if (otraSede4.isEmpty() || !otraSede4.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")) {
//            model.addAttribute("otraSede4Error", "*El campo Nombre de la nueva Sede debe contener solo letras*");
//            hasErrors = true;
//        }
//        if (sede_nueva4_direccion.isEmpty() || !sede_nueva4_direccion.matches("^[a-zA-Z0-9\\s°]*$")) {
//            model.addAttribute("sede_nueva4_direccionError", "*El campo Dirección de la nueva Sede debe contener solo letras, números o signos");
//            hasErrors = true;
//        }
//        if (especialidad == null || especialidad.isEmpty() || especialidad.equals("Seleccionar Especialidad")) {
//            List<Especialidad> listaEspecialidades = especialidadRepository.findAll();
//            List<Clinica> listaClinicas = clinicaRepository.findAll();
//            model.addAttribute("listaClinicas", listaClinicas);
//            model.addAttribute("listaEspecialidades",listaEspecialidades);
//            model.addAttribute("clinicaError", "*Seleccione una clínica*");
//            model.addAttribute("sedeError", "*Debe seleccionar una Sede*");
//            model.addAttribute("especialidadError", "*Debe seleccionar una Especialidad*");
//            hasErrors = true;
//        }
//
//        if(hasErrors){
//            model.addAttribute("selectUsuarioValue", selectUsuario);
//            model.addAttribute("dniValue", dni);
//            model.addAttribute("nombresValue", nombres);
//            model.addAttribute("apellidosValue", apellidos);
//            model.addAttribute("correoUserValue", correoUser);
//            model.addAttribute("otraClinicaValue", otraClinica);
//            model.addAttribute("correo_nueva_clinicaValue", correo_nueva_clinica);
//            model.addAttribute("telefono_nueva_clinicaValue", telefono_nueva_clinica);
//            model.addAttribute("sede_nueva_direccionValue", sede_nueva_direccion);
//            model.addAttribute("otraSedeValue", otraSede);
//            ////
//            model.addAttribute("sedeValue", sede);
//            model.addAttribute("especialidadValue", especialidad);
//            model.addAttribute("otraSede4Value", otraSede4);
//            model.addAttribute("sede_nueva4_direccionValue", sede_nueva4_direccion);
//            return "superAdmin/Crear_Usuario";
//        }
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
                    administradorRepository.insertarAdministrador(dni, nombres, apellidos, sede_enviar.getIdSede(),correoUser);

                } else{
                    Clinica clinica_enviar = clinicaRepository.buscarClinicaPorNombre(clinica);
                    Sede sede_enviar = sedeRepository.buscarPorNombreDeSede(sede, clinica_enviar.getIdClinica());
                    administradorRepository.insertarAdministrador(dni, nombres, apellidos, sede_enviar.getIdSede(),correoUser);
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
                //administrativoPorEspecialidadPorSede.setTorre_piso("Por Asignar");
                String torrePiso = "Por Asignar";
                Especialidad especialidad_enviar = especialidadRepository.findByNombre(especialidad);
                administrativoPorEspecialidadPorSede.setEspecialidadId(especialidad_enviar);
                administrativoPorEspecialidadPorSedeRepository.insertarTablaAdministrativoXEspecialidadXSede(sede_enviar.getIdSede(),administrativonuevo.getIdAdministrativo(),String.valueOf(especialidad_enviar.getIdEspecialidad()),torrePiso);
//                administrativoPorEspecialidadPorSedeRepository.insertarTablaAdministrativoXEspecialidadXSede(sede_enviar.getIdSede(),dni);
                // Utiliza el valor de 'clinica' y, si corresponde, el valor de 'sede'
            }
            // ... (por ejemplo, guarda el usuario en la base de datos)
        }
        return "redirect:/SuperAdminHomePage";

    }


}