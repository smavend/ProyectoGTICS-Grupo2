package com.example.proyectogticsgrupo2.dto;

import java.time.LocalDate;

public interface ListaRecibosDTO {
    String getFecha();
    String getNombres();
    float getPago_doctor();
    int getId_cita();
    String getId_doctor();
    String getSede();
}
