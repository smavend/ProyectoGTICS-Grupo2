package com.example.proyectogticsgrupo2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorDTO_superadmin {
    private String idDoctor;
    private String nombre;
    private String apellidos;
    private String estado;
    private String correo;
    private int horario;
    private String clinica;
    private String sede;
    private String especialidad;
    private boolean showLoguearButton;  // Nueva propiedad

}
