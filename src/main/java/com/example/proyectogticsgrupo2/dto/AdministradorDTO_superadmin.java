package com.example.proyectogticsgrupo2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministradorDTO_superadmin {
    private String idAdministrador;
    private String nombre;
    private String apellidos;
    private int estado;
    private String correo;
    private String sedeNombre;
    private String clinica;
    private boolean showLoguearButton;  // Nueva propiedad

}
