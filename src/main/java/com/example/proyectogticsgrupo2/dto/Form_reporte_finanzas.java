package com.example.proyectogticsgrupo2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class Form_reporte_finanzas {

    @NotBlank(message = "Seleccione el tipo de reporte")
    private String selectReporte;
    @NotBlank(message = "El campo nombres no puede estar vacío")
    private String nombres;

    @NotBlank(message = "El campo apellidos no puede estar vacío")
    private String apellidos;

    @NotBlank(message = "Seleccione un tipo de Sede")
    private String seguro;

    @NotBlank(message = "Seleccione una especialidad")
    private String especialidad;
}
