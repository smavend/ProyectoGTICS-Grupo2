package com.example.proyectogticsgrupo2.dto;

import java.time.LocalDate;

public interface ListaRecibosDTO {
    LocalDate getFecha();
    String getNombres();
    float getPago_doctor();
    int getId_cita();
    String getId_paciente();
    String getId_doctor();
    float getSeguro_doctor();
}
