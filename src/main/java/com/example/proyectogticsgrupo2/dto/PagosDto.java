package com.example.proyectogticsgrupo2.dto;

import java.time.LocalDate;

public interface PagosDto {
    String getEspecialidad();
    LocalDate getFechaCancelada();
    Float getPrecio();
    String getTipoPago();
    String getEstado();
}
