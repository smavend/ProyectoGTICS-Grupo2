package com.example.proyectogticsgrupo2.dto;

import java.util.Date;

public interface PagoYPrecioDTO {
    int getIdPago();
    Date getFechaEmitida();
    Date getFechaCancelada();
    int getEstadoPago();
    String getTipoPago();
    int idCita();
    
}
