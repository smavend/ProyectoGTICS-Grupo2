package com.example.proyectogticsgrupo2.service;
import com.example.proyectogticsgrupo2.dto.AdministradorDTO_superadmin;
import com.example.proyectogticsgrupo2.dto.AdministrativoDTO_superadmin;
import com.example.proyectogticsgrupo2.dto.DoctorDTO_superadmin;
import com.example.proyectogticsgrupo2.dto.PacienteDTO_superadmin;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

@Service
public class SuperAdminService {

    private final AdministrativoRepository administrativoRepository;
    private final ClinicaRepository clinicaRepository;
    private final AdministrativoPorEspecialidadPorSedeRepository administrativoPorEspecialidadPorSedeRepository;
    private final PacienteRepository pacienteRepository;
    private final DoctorRepository doctorRepository;
    private final AdministradorRepository administradorRepository;
    private final SedeRepository sedeRepository;

    public SuperAdminService(AdministrativoRepository administrativoRepository,
                             AdministrativoPorEspecialidadPorSedeRepository administrativoPorEspecialidadPorSedeRepository,
                             ClinicaRepository clinicaRepository,
                             PacienteRepository pacienteRepository,
                             DoctorRepository doctorRepository,
                             AdministradorRepository administradorRepository,
                             SedeRepository sedeRepository) {
        this.administrativoRepository = administrativoRepository;
        this.administrativoPorEspecialidadPorSedeRepository = administrativoPorEspecialidadPorSedeRepository;
        this.clinicaRepository = clinicaRepository;
        this.pacienteRepository = pacienteRepository;
        this.doctorRepository = doctorRepository;
        this.administradorRepository = administradorRepository;
        this.sedeRepository = sedeRepository;
    }
    public AdministradorDTO_superadmin toAdministradorDTO_superadmin(Administrador administrador){
        AdministradorDTO_superadmin dto = new AdministradorDTO_superadmin();
        dto.setIdAdministrador(administrador.getIdAdministrador());
        dto.setNombre(administrador.getNombre());
        dto.setApellidos(administrador.getApellidos());
        dto.setEstado(administrador.getEstado());
        dto.setCorreo(administrador.getCorreo());
        //Obtenemos la sede y clinica para los pacientes

        //devuelve la sede(int)  ubicada en la tabla administrador
        int administradorxsedexclinica_sede = administrativoPorEspecialidadPorSedeRepository.obteneSedePorAdministradorId(administrador.getIdAdministrador());
        //busca la fila a la cual corresponde el valor de sede_id (valor único) en tabla sede
        Sede sedexadministrador = sedeRepository.buscarPorSedeId(String.valueOf(administradorxsedexclinica_sede));

        Clinica clinica = clinicaRepository.buscarClinicaPorID(sedexadministrador.getClinica().getIdClinica());
        String sede_administrador;
        String clinica_administrador;
        if (administradorxsedexclinica_sede != 0) {
            // Asegúrate de que exista un método getSedeId() en la clase AdministrativoPorEspecialidadPorSede que devuelva un objeto Sede
            sede_administrador = sedexadministrador.getNombre();
            clinica_administrador =clinica.getNombre();
        } else {
            sede_administrador = "Sin asignar"; // Valor predeterminado si no se encuentra la sede en la base de datos
            clinica_administrador = "Sin asignar";
        }
        dto.setSedeNombre(sede_administrador);
        dto.setClinica(clinica_administrador);
        return dto;
    }
  /*  public AdministrativoDTO_superadmin toAdministrativoDTO_superadmin(Administrativo administrativo) {
        AdministrativoDTO_superadmin dto = new AdministrativoDTO_superadmin();
        dto.setIdAdministrativo(administrativo.getIdAdministrativo());
        dto.setNombre(administrativo.getNombre());
        dto.setApellidos(administrativo.getApellidos());
        dto.setEstado(administrativo.getEstado());
        dto.setCorreo(administrativo.getCorreo());

        // Obtiene la sede de este administrativo usando el repositorio necesario
        AdministrativoPorEspecialidadPorSede administrativoxsede = administrativoPorEspecialidadPorSedeRepository.buscarXAdministrativoId(administrativo.getIdAdministrativo());
        // Asegúrate de que exista un método getSede() en la clase AdministrativoPorEspecialidadPorSede que devuelva un objeto Sede
        String sede;
        String especialidad;
        String clinica;
        if (administrativoxsede != null) {
            // Asegúrate de que exista un método getSedeId() en la clase AdministrativoPorEspecialidadPorSede que devuelva un objeto Sede
            sede = administrativoxsede.getSedeId().getNombre();
            especialidad = administrativoxsede.getEspecialidadId().getNombre();
            Clinica clinicaxid  = clinicaRepository.buscarClinicaPorID(administrativoxsede.getSedeId().getClinica().getIdClinica());
            clinica = clinicaxid.getNombre();
        } else {
            sede = "Sin asignar"; // Valor predeterminado si no se encuentra la sede en la base de datos
            especialidad = "Sin asignar";
            clinica = "Sin asignar";
        }

        dto.setSedeNombre(sede);
        dto.setEspecialidad(especialidad);
        dto.setClinica(clinica);

        return dto;
    }*/
  public AdministrativoDTO_superadmin toAdministrativoDTO_superadmin(Administrativo administrativo) {
      AdministrativoDTO_superadmin dto = new AdministrativoDTO_superadmin();
      dto.setIdAdministrativo(administrativo.getIdAdministrativo());
      dto.setNombre(administrativo.getNombre());
      dto.setApellidos(administrativo.getApellidos());
      dto.setEstado(administrativo.getEstado());
      dto.setCorreo(administrativo.getCorreo());

      List<AdministrativoPorEspecialidadPorSede> administrativoxsedes = administrativoPorEspecialidadPorSedeRepository.buscarXAdministrativoId(administrativo.getIdAdministrativo());

      List<String> nombresSedes = new ArrayList<>();
      List<String> nombresEspecialidades = new ArrayList<>();
      String nombreClinica = "Sin asignar";

      for (AdministrativoPorEspecialidadPorSede administrativoxsede : administrativoxsedes) {
          nombresSedes.add(administrativoxsede.getSedeId().getNombre());
          nombresEspecialidades.add(administrativoxsede.getEspecialidadId().getNombre());
          if (nombreClinica.equals("Sin asignar")) { // Only assign the clinic name once
              nombreClinica = clinicaRepository.buscarClinicaPorID(administrativoxsede.getSedeId().getClinica().getIdClinica()).getNombre();
          }
      }

      String sedes = String.join(", ", nombresSedes);
      String especialidades = String.join(", ", nombresEspecialidades);

      dto.setSedeNombre(sedes.isEmpty() ? "Sin asignar" : sedes);
      dto.setEspecialidad(especialidades.isEmpty() ? "Sin asignar" : especialidades);
      dto.setClinica(nombreClinica);

      return dto;
  }


    public DoctorDTO_superadmin toDoctoDTO_superadmin(Doctor doctor) {
        DoctorDTO_superadmin dto = new DoctorDTO_superadmin();
        dto.setIdDoctor(doctor.getId_doctor());
        dto.setNombre(doctor.getNombre());
        dto.setApellidos(doctor.getApellidos());
        dto.setEstado(String.valueOf(doctor.getEstado()));
        dto.setEspecialidad(doctor.getEspecialidad().getNombre());
        dto.setSede(doctor.getSede().getNombre());
        /*if(doctor.getHorario()==null){
            dto.setHorario(0);
        }else{
            dto.setHorario(doctor.getHorario().getId_horario());
        }*/
        dto.setCorreo(doctor.getCorreo());
        dto.setClinica(doctor.getSede().getClinica().getNombre());
//        if(doctor.getDuracion_cita_horas() == null){
//            dto.setDuracion_cita_horas("Por Elegir");
//        }else{
//            dto.setDuracion_cita_horas(doctor.getDuracion_cita_horas());
//        }
        return dto;
    }

    public PacienteDTO_superadmin toPacienteDTO_superadmin(Paciente paciente) {
        PacienteDTO_superadmin dto = new PacienteDTO_superadmin();
        dto.setIdPaciente(paciente.getIdPaciente());
        dto.setNombre(paciente.getNombre());
        dto.setApellidos(paciente.getApellidos());
        dto.setEstado(paciente.getEstado());
        dto.setSeguro(paciente.getSeguro().getNombre());
        dto.setTelefono(paciente.getTelefono());
        dto.setCorreo(paciente.getCorreo());
        dto.setDireccion(paciente.getDireccion());
        dto.setDistrito(paciente.getDistrito().getNombre());
        String sede;
        String clinica;
        if(paciente.getAdministrativo() == null){
            dto.setAdministrador_in_Charge("Sin Asignar");
            sede = "Sin asignar"; // Valor predeterminado si no se encuentra la sede en la base de datos
            clinica = "Sin asignar";
        }else{
            AdministrativoPorEspecialidadPorSede pacientexsede = administrativoPorEspecialidadPorSedeRepository.buscarPorAdministrativoId(paciente.getAdministrativo().getIdAdministrativo());
            dto.setAdministrador_in_Charge(paciente.getAdministrativo().getNombre());
            // Obtiene la sede de este administrativo usando el repositorio necesario
            Clinica clinica_paciente = clinicaRepository.buscarClinicaPorID(pacientexsede.getSedeId().getIdSede());
            // Asegúrate de que exista un método getSedeId() en la clase AdministrativoPorEspecialidadPorSede que devuelva un objeto Sede
            sede = pacientexsede.getSedeId().getNombre();
            clinica = clinica_paciente.getNombre();
            // Asegúrate de que exista un método getSede() en la clase AdministrativoPorEspecialidadPorSede que devuelva un objeto Sede
        }
        dto.setClinica(clinica);
        dto.setSede(sede);
        return dto;
    }

    public List<DoctorDTO_superadmin> obtenerTodosLosDoctoresDTO() {
        List<Doctor> listaDoctores = doctorRepository.findAll();
        List<DoctorDTO_superadmin> listaDoctoresDTO_superadmin = new ArrayList<>();
        for(Doctor doctor: listaDoctores){
            listaDoctoresDTO_superadmin.add(toDoctoDTO_superadmin(doctor));
        }
        return listaDoctoresDTO_superadmin;
    }



    public List<PacienteDTO_superadmin> obtenerTodosLosPacientesDTO() {
        List<Paciente> listaPacientes = pacienteRepository.findAll();
        List<PacienteDTO_superadmin> listaPacientesDTO_superadmin = new ArrayList<>();
        for(Paciente paciente: listaPacientes){

            listaPacientesDTO_superadmin.add(toPacienteDTO_superadmin(paciente));
        }
        return listaPacientesDTO_superadmin;
    }

    public List<AdministrativoDTO_superadmin> obtenerTodosLosAdministrativosDTO() {
        List<Administrativo> listaAdministrativo = administrativoRepository.findAll();
        List<AdministrativoDTO_superadmin> listaAdministrativoDTO_superadmin = new ArrayList<>();

        for (Administrativo administrativo : listaAdministrativo) {
            listaAdministrativoDTO_superadmin.add(toAdministrativoDTO_superadmin(administrativo));
        }

        return listaAdministrativoDTO_superadmin;
    }
    public List<AdministradorDTO_superadmin> obtenerTodosLosAdministradoresDTO() {
        List<Administrador> listaAdministradores = administradorRepository.findAll();
        List<AdministradorDTO_superadmin> listaAdministradoresDTO_superadmin = new ArrayList<>();
        for (Administrador administrador : listaAdministradores){
            listaAdministradoresDTO_superadmin.add(toAdministradorDTO_superadmin(administrador));
        }
        return listaAdministradoresDTO_superadmin;
    }
    private String quitarTildes(String cadena) {
        return Normalizer.normalize(cadena, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public List<DoctorDTO_superadmin> filtrarDoctores(String clinicaId, String sedeId, String nombre, String especialidadId) {
        List<DoctorDTO_superadmin> listaDoctoresDTO = obtenerTodosLosDoctoresDTO();
        List<DoctorDTO_superadmin> listaFiltrada = new ArrayList<>();
        for (DoctorDTO_superadmin doctorDTOSuperadmin : listaDoctoresDTO){
            String clinicaDTO = doctorDTOSuperadmin.getClinica();
            String sedeDTO = doctorDTOSuperadmin.getSede();
            String especialidadDTO = doctorDTOSuperadmin.getEspecialidad();
            String nombreDTO = doctorDTOSuperadmin.getNombre();
            String apellidosDTO = doctorDTOSuperadmin.getApellidos();
            String nombreCompletoDTO = nombreDTO + " " + apellidosDTO;

            boolean matchClinica = clinicaId.equals("todos") || clinicaId.equals(clinicaDTO);
            boolean matchSede = sedeId.equals("todos") || sedeId.equals(sedeDTO);
            boolean matchEspecialidad = especialidadId.equals("todos") || especialidadId.equals(especialidadDTO);
            boolean matchNombre = nombre.isEmpty() || quitarTildes(nombreCompletoDTO.toLowerCase()).contains(quitarTildes(nombre.toLowerCase()));

            if (matchClinica && matchSede && matchEspecialidad && matchNombre) {
                listaFiltrada.add(doctorDTOSuperadmin);
            }
        }
        return listaFiltrada;
    }


    public List<AdministrativoDTO_superadmin> filtrarAdministrativos(String clinicaId, String sedeId, String especialidadId, String nombre) {
        // Obtén todos los AdministrativoDTO_superadmin y filtra la lista según los parámetros recibidos
        List<AdministrativoDTO_superadmin> listaAdministrativosDTO = obtenerTodosLosAdministrativosDTO();
        List<AdministrativoDTO_superadmin> listaFiltrada = new ArrayList<>();

        for (AdministrativoDTO_superadmin AdministrativoDTO_superadmin : listaAdministrativosDTO) {
            String clinicaDTO = AdministrativoDTO_superadmin.getClinica();
            String sedeDTO = AdministrativoDTO_superadmin.getSedeNombre();
            String especialidadDTO = AdministrativoDTO_superadmin.getEspecialidad();
            String nombreDTO = AdministrativoDTO_superadmin.getNombre();
            String apellidosDTO = AdministrativoDTO_superadmin.getApellidos();
            String nombreCompletoDTO = nombreDTO + " " + apellidosDTO;

            boolean matchClinica = clinicaId.equals("todos") || clinicaId.equals(clinicaDTO);
            boolean matchSede = sedeId.equals("todos") || sedeId.equals(sedeDTO);
            boolean matchEspecialidad = especialidadId.equals("todos") || especialidadId.equals(especialidadDTO);
            boolean matchNombre = nombre.isEmpty() || quitarTildes(nombreCompletoDTO.toLowerCase()).contains(quitarTildes(nombre.toLowerCase()));

            if (matchClinica && matchSede && matchEspecialidad && matchNombre) {
                listaFiltrada.add(AdministrativoDTO_superadmin);
            }
        }

        return listaFiltrada;
    }
    public List<PacienteDTO_superadmin> filtrarPacientes(String clinicaId, String sedeId, String nombre) {
        // Obtén todos los PacienteDTO_superadmin y filtra la lista según los parámetros recibidos
        List<PacienteDTO_superadmin> listaPacienteDTO = obtenerTodosLosPacientesDTO();
        List<PacienteDTO_superadmin> listaFiltradaPaciente = new ArrayList<>();

        for (PacienteDTO_superadmin PacienteDTO_superadmin : listaPacienteDTO) {
            String clinicaDTO = PacienteDTO_superadmin.getClinica();
            String sedeDTO = PacienteDTO_superadmin.getSede();
            String nombreDTO = PacienteDTO_superadmin.getNombre();
            String apellidosDTO = PacienteDTO_superadmin.getApellidos();
            String nombreCompletoDTO = nombreDTO + " " + apellidosDTO;

            boolean matchClinica = clinicaId.equals("todos") || clinicaId.equals(clinicaDTO);
            boolean matchSede = sedeId.equals("todos") || sedeId.equals(sedeDTO);
//            boolean matchEspecialidad = especialidadId.equals("todos") || especialidadId.equals(especialidadDTO);
            boolean matchNombre = nombre.isEmpty() || quitarTildes(nombreCompletoDTO.toLowerCase()).contains(quitarTildes(nombre.toLowerCase()));

            if (matchClinica && matchSede  && matchNombre) {
                listaFiltradaPaciente.add(PacienteDTO_superadmin);
            }
        }

        return listaFiltradaPaciente;
    }


    public List<AdministradorDTO_superadmin> filtrarAdministradores(String clinicaId, String sedeId, String nombre) {
        List<AdministradorDTO_superadmin> listaAdministradorDTO = obtenerTodosLosAdministradoresDTO();
        List<AdministradorDTO_superadmin> listaFiltradaAdministradores = new ArrayList<>();

        for (AdministradorDTO_superadmin AdministradorDTO_superadmin : listaAdministradorDTO) {
            String clinicaDTO = AdministradorDTO_superadmin.getClinica();
            String sedeDTO = AdministradorDTO_superadmin.getSedeNombre();
            String nombreDTO = AdministradorDTO_superadmin.getNombre();
            String apellidosDTO = AdministradorDTO_superadmin.getApellidos();
            String nombreCompletoDTO = nombreDTO + " " + apellidosDTO;

            boolean matchClinica = clinicaId.equals("todos") || clinicaId.equals(clinicaDTO);
            boolean matchSede = sedeId.equals("todos") || sedeId.equals(sedeDTO);
//          boolean matchEspecialidad = especialidadId.equals("todos") || especialidadId.equals(especialidadDTO);
            boolean matchNombre = nombre.isEmpty() || quitarTildes(nombreCompletoDTO.toLowerCase()).contains(quitarTildes(nombre.toLowerCase()));

            if (matchClinica && matchSede  && matchNombre) {
                listaFiltradaAdministradores.add(AdministradorDTO_superadmin);
            }
        }
        return listaFiltradaAdministradores;
    }
}