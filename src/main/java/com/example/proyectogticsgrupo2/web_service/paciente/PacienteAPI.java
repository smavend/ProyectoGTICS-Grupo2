package com.example.proyectogticsgrupo2.web_service.paciente;

import com.example.proyectogticsgrupo2.dto.HorarioDeDiaDTO;
import com.example.proyectogticsgrupo2.dto.HorarioOcupadoDTO;
import com.example.proyectogticsgrupo2.entity.Cita;
import com.example.proyectogticsgrupo2.entity.Doctor;
import com.example.proyectogticsgrupo2.entity.Especialidad;
import com.example.proyectogticsgrupo2.entity.pacienteAPI.HorariosRoot;
import com.example.proyectogticsgrupo2.repository.CitaRepository;
import com.example.proyectogticsgrupo2.repository.DoctorRepository;
import com.example.proyectogticsgrupo2.repository.EspecialidadRepository;
import com.example.proyectogticsgrupo2.repository.HorarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@RestController
@CrossOrigin
@RequestMapping(method = RequestMethod.GET, value = "/Paciente/api")
public class PacienteAPI {

    final EspecialidadRepository especialidadRepository;
    final HorarioRepository horarioRepository;
    final DoctorRepository doctorRepository;
    final CitaRepository citaRepository;

    @Autowired
    HorariosDao horariosDao;

    public PacienteAPI(EspecialidadRepository especialidadRepository,
                       HorarioRepository horarioRepository,
                       DoctorRepository doctorRepository,
                       CitaRepository citaRepository) {
        this.especialidadRepository = especialidadRepository;
        this.horarioRepository = horarioRepository;
        this.doctorRepository = doctorRepository;
        this.citaRepository = citaRepository;
    }

    @GetMapping("/especialidades/{modalidad}")
    public ResponseEntity<HashMap<String, Object>> obtenerEspecialidades(@PathVariable("modalidad") String modalidadString) {
        HashMap<String, Object> response = new HashMap<>();

        try {
            int modalidad = Integer.parseInt(modalidadString);
            List<Especialidad> especialidadList = null;
            if (modalidad == 0) {
                especialidadList = especialidadRepository.findAll();
            } else if (modalidad == 1) {
                especialidadList = especialidadRepository.buscarEspecialidadesVirtuales();
            }
            response.put("resultado", "ok");
            response.put("especialidades", especialidadList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("resultado", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/horarios/consulta/{doctor}/{fecha}")
    public ResponseEntity<HashMap<String, Object>> obtenerHorarios(@PathVariable("doctor") String idDoctor,
                                                                   @PathVariable("fecha") String fechaString) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate fecha = LocalDate.parse(fechaString, formatter);

            List<HorarioOcupadoDTO> horariosOcupadosDTO = horarioRepository.buscarHorariosOcupados(idDoctor, fecha); // Horarios ocupados del doctor
            List<LocalTime> horariosOcupados = new ArrayList<>();
            for (HorarioOcupadoDTO h : horariosOcupadosDTO) {
                horariosOcupados.add(h.getHorario());
            }

            List<HashMap<String, LocalTime>> horariosDisponibles = new ArrayList<>(); // Todos los horarios registrados por el doctor
            Doctor doctor = doctorRepository.findById(idDoctor).get();
            int duracionCita = doctor.getDuracion_cita_minutos();
            int duracionComida = 60; // minutos

            HorarioDeDiaDTO horarioDeDia = null;
            LocalTime hora;
            LocalTime horaFin;
            LocalTime horaComida;

            switch (fecha.getDayOfWeek().getValue()) {
                case 1 -> horarioDeDia = horarioRepository.buscarHorarioLunes(doctor.getId_doctor());
                case 2 -> horarioDeDia = horarioRepository.buscarHorarioMartes(doctor.getId_doctor());
                case 3 -> horarioDeDia = horarioRepository.buscarHorarioMiercoles(doctor.getId_doctor());
                case 4 -> horarioDeDia = horarioRepository.buscarHorarioJueves(doctor.getId_doctor());
                case 5 -> horarioDeDia = horarioRepository.buscarHorarioViernes(doctor.getId_doctor());
                case 6 -> horarioDeDia = horarioRepository.buscarHorarioSabado(doctor.getId_doctor());
            }

            hora = horarioDeDia.getInicio();
            horaFin = horarioDeDia.getFin();
            horaComida = horarioDeDia.getComidaInicio();

            while (hora.isBefore(horaFin)) {

                if (hora.isBefore(horaComida) || hora.isAfter(horaComida.plusMinutes(duracionComida - 1))) {
                    if (!horariosOcupados.contains(hora)) {
                        HashMap<String, LocalTime> horarioDia = new HashMap<>();
                        horarioDia.put("inicio", hora);
                        horarioDia.put("fin", hora.plusMinutes(duracionCita));
                        horariosDisponibles.add(horarioDia);
                    }
                } else if (hora.isAfter(horaComida)) {
                    hora = horaComida.plusMinutes(duracionComida);
                    continue;
                }
                hora = hora.plusMinutes(duracionCita);
            }

            response.put("resultado", "ok");
            response.put("horarios", horariosDisponibles);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("resultado", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/horarios/examen/{sede}/{especialidad}/{fecha}")
    public ResponseEntity<HashMap<String, Object>> obtenerHorariosExamenes(@PathVariable("sede") int idSede,
                                                                           @PathVariable("especialidad") int idEspecialidad,
                                                                           @PathVariable("fecha") String fechaString,
                                                                           HttpServletRequest request) {
        HashMap<String, Object> response = new HashMap<>();

        try {

            // Considerando que todos los doctores de examenes estan disponibles todos los días (ya no se hace la búsqueda de acuerdo al día)
            List<Doctor> doctoresDisponibles = doctorRepository.findBySede_IdSedeAndEspecialidad_IdEspecialidad(idSede, idEspecialidad);

            for (Doctor doctor : doctoresDisponibles) {
                // Verificar si doctor tiene disponibilidad en la fecha
                HorariosRoot horariosRoot = horariosDao.listarHorarios(doctor.getId_doctor(), fechaString, request);
                if (horariosRoot.horarios.size() > 0) {
                    // Si es que tiene disponibilidad, retornar horarios y terminar bucle
                    response.put("resultado", "ok");
                    response.put("doctor", doctor.getId_doctor());
                    response.put("horarios", horariosRoot.horarios);
                    return ResponseEntity.ok().body(response);
                }
                // Si no tiene disponibilidad, pasar al siguiente doctor
            }
            // Si no hay horarios disponibles, retornar vacío
            List<HashMap<String, Object>> horarios = new ArrayList<>();
            response.put("resultado", "ok");
            response.put("horarios", horarios);
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("resultado", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/horarios/proximos/{doctor}")
    public ResponseEntity<HashMap<String, Object>> obtenerHorariosProximos(@PathVariable("doctor") String idDoctor) {
        HashMap<String, Object> response = new HashMap<>();

        try {
            LocalDate fecha = LocalDate.now().plusDays(1);

            Doctor doctor = doctorRepository.findById(idDoctor).get();
            int duracionCita = doctor.getDuracion_cita_minutos();
            int duracionComida = 60; // minutos

            List<HorarioOcupadoDTO> horariosOcupadosDTO = horarioRepository.buscarHorariosOcupados(idDoctor, fecha); // Horarios ocupados del doctor
            List<LocalTime> horariosOcupados = new ArrayList<>();
            for (HorarioOcupadoDTO h : horariosOcupadosDTO) {
                horariosOcupados.add(h.getHorario());
            }

            List<HashMap<String, String>> horariosDisponibles = new ArrayList<>();

            HorarioDeDiaDTO horarioDeDia = null;

            switch (fecha.getDayOfWeek().getValue()) {
                case 1 -> horarioDeDia = horarioRepository.buscarHorarioLunes(doctor.getId_doctor());
                case 2 -> horarioDeDia = horarioRepository.buscarHorarioMartes(doctor.getId_doctor());
                case 3 -> horarioDeDia = horarioRepository.buscarHorarioMiercoles(doctor.getId_doctor());
                case 4 -> horarioDeDia = horarioRepository.buscarHorarioJueves(doctor.getId_doctor());
                case 5 -> horarioDeDia = horarioRepository.buscarHorarioViernes(doctor.getId_doctor());
                case 6 -> horarioDeDia = horarioRepository.buscarHorarioSabado(doctor.getId_doctor());
            }

            if (horarioDeDia != null) {
                LocalTime hora = horarioDeDia.getInicio();
                LocalTime horaFin = horarioDeDia.getFin();
                LocalTime horaComida = horarioDeDia.getComidaInicio();

                while (hora.isBefore(horaFin)) {

                    if (hora.isBefore(horaComida) || hora.isAfter(horaComida.plusMinutes(duracionComida - 1))) {
                        if (!horariosOcupados.contains(hora)) {
                            HashMap<String, String> horarioDia = new HashMap<>();

                            Locale spanishLocale = new Locale("es", "ES");
                            DayOfWeek diaNumero = fecha.getDayOfWeek();
                            TextStyle textStyle = TextStyle.FULL;
                            String diaLowerCase = diaNumero.getDisplayName(textStyle, spanishLocale);
                            String dia = diaLowerCase.substring(0, 1).toUpperCase() + diaLowerCase.substring(1);

                            horarioDia.put("dia", dia);
                            horarioDia.put("inicio", hora.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                            horarioDia.put("fin", hora.plusMinutes(duracionCita).format(DateTimeFormatter.ofPattern("HH:mm:ss")));

                            horariosDisponibles.add(horarioDia);

                        }
                    } else if (hora.isAfter(horaComida)) {
                        hora = horaComida.plusMinutes(duracionComida);
                        continue;
                    }

                    if (horariosDisponibles.size() == 2){
                        break;
                    }

                    hora = hora.plusMinutes(duracionCita);
                }
            }

            response.put("resultado", "ok");
            response.put("horarios", horariosDisponibles);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("resultado", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/citas/pendientes/{paciente}")
    public ResponseEntity<HashMap<String, Object>> obtenerCitasPendientes(@PathVariable("paciente") String idPaciente){
        HashMap<String, Object> response = new HashMap<>();

        List<Cita> citasPendientes = citaRepository.buscarCitasPendientes(idPaciente);
        List<HashMap<String, String>> citas = new ArrayList<>();

        response.put("resultado", "ok");
        for (Cita c: citasPendientes){
            HashMap<String, String> infoCita = new HashMap<>();
            infoCita.put("id", String.valueOf(c.getId_cita()));
            infoCita.put("especialidad", c.getEspecialidad().getNombre());
            infoCita.put("doctor", c.getDoctor().getNombreYApellido());
            citas.add(infoCita);
        }
        response.put("citas", citas);

        return ResponseEntity.ok().body(response);
    }

}