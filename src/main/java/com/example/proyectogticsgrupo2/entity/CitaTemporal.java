package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "cita_temporal")
public class CitaTemporal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita_temporal")
    private Integer id;

    @Column(name = "paciente_id_paciente")
    private String idPaciente;

    @NotNull(message = "Seleccione una de las modalidades disponibles")
    private Integer modalidad;

    @NotNull(message = "Seleccione una de las sedes disponibles")
    @Column(name = "sede_id_sede")
    private Integer idSede;

    @NotBlank(message = "Seleccione uno de los doctores disponibles")
    @Column(name = "doctor_id_doctor")
    private String idDoctor;

    @NotNull(message = "Seleccione una fecha")
    @Future(message = "Seleccione una fecha válida")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime inicio;

    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime fin;

    @Column(name = "id_cita_previa")
    private Integer idCitaPrevia;

    @NotNull(message = "Seleccione una de las especialidades disponibles")
    @Column(name = "id_especialidad")
    private Integer idEspecialidad;
}
