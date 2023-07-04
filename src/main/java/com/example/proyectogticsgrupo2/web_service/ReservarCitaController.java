package com.example.proyectogticsgrupo2.web_service;

import com.example.proyectogticsgrupo2.dto.HorarioDeDiaDTO;
import com.example.proyectogticsgrupo2.dto.HorarioOcupadoDTO;
import com.example.proyectogticsgrupo2.entity.Doctor;
import com.example.proyectogticsgrupo2.entity.Especialidad;
import com.example.proyectogticsgrupo2.repository.DoctorRepository;
import com.example.proyectogticsgrupo2.repository.EspecialidadRepository;
import com.example.proyectogticsgrupo2.repository.HorarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(method = RequestMethod.GET, value = "/Paciente/api")
public class ReservarCitaController {

    final EspecialidadRepository especialidadRepository;
    final HorarioRepository horarioRepository;
    final DoctorRepository doctorRepository;

    public ReservarCitaController(EspecialidadRepository especialidadRepository,
                                  HorarioRepository horarioRepository,
                                  DoctorRepository doctorRepository) {
        this.especialidadRepository = especialidadRepository;
        this.horarioRepository = horarioRepository;
        this.doctorRepository = doctorRepository;
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

    @GetMapping("/horarios/{doctor}/{fecha}")
    public ResponseEntity<HashMap<String, Object>> obtenerHorarios(@PathVariable("doctor") String idDoctor,
                                                                   @PathVariable("fecha") String fechaString) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
                        horarioDia.put("fin", hora.plusMinutes(doctor.getDuracion_cita_minutos()));
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
}