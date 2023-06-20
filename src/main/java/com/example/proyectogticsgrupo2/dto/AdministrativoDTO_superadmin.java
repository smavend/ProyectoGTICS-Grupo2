package com.example.proyectogticsgrupo2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministrativoDTO_superadmin {
    private String idAdministrativo;
    private String nombre;
    private String apellidos;
    private int estado;
    private String correo;
    private String sedeNombre;
    private String clinica;
    private String especialidad;
    private boolean showLoguearButton;  // Nueva propiedad



    // Constructor, getters y setters
    // ...
}