package com.example.proyectogticsgrupo2.dto;

import java.util.Date;

public interface MensajeDatosDto {
    int getId_mensaje();
    String getId_emisor();
    String getId_receptor();
    String getMensaje();
    Date getFecha();
    String getNombreemisor();
}
