package com.example.proyectogticsgrupo2.dto;

import java.time.LocalDate;

public interface AdministradorEgresos {
    String getConcepto();
    double getPagodoctor();
    String getNombreuser();
    String getEspecialidadcita();
    String getNombreseguro();
    LocalDate getFecha();
    String getCategoriagasto();
}
