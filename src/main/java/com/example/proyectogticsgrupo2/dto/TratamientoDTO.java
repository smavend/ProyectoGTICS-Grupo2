package com.example.proyectogticsgrupo2.dto;

import java.time.LocalDateTime;

public interface TratamientoDTO {
    int getId_cita();
    String getId_paciente();
    LocalDateTime getFin();
    String getDiagnostico();
    String getReceta();
    String getTratamiento();

}
