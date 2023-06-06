package com.example.proyectogticsgrupo2.dto;

import java.time.LocalDateTime;

public interface ListaBuscadorDoctor {
    int getId_cita();
    String getId_paciente();
    String getNombre();
    String getApellidos();
    int getModalidad();
    LocalDateTime getInicio();
    LocalDateTime getFin();
    int getEstado();
    int getSeguro_id_seguro();
}
