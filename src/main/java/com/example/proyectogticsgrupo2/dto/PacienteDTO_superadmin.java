package com.example.proyectogticsgrupo2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PacienteDTO_superadmin {
    private String idPaciente;
    private String nombre;
    private String apellidos;
    private int estado;
    private String seguro;
    private String telefono;
    private String correo;
    private String direccion;
    private String distrito;
    private String clinica;
    private String sede;
    private String Administrador_in_Charge;
    private boolean showLoguearButton;  // Nueva propiedad




    // Constructor, getters y setters
    // ...
}