package com.example.proyectogticsgrupo2.dto;

import java.time.LocalDate;

public interface AdministradorIngresos {
    LocalDate getFechacancelada();
    String getNombreuser();
    String getEspecialidadcita();
    String getConcepto();
    String getNombreseguro();
    double getPreciocita();
    String getTipopago();

}
