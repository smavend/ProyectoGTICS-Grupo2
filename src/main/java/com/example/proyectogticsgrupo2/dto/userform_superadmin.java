package com.example.proyectogticsgrupo2.dto;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class userform_superadmin {

    @NotBlank(message = "Seleccione un tipo de usuario")
    private String selectUsuario;

    @NotBlank(message = "El campo DNI no puede estar vacío")
    @Size(min = 8, max = 8, message = "DNI debe contener 8 dígitos")
    @Digits(integer = 8, fraction = 0, message = "DNI debe contener sólo dígitos")
    private String dni;

    @NotBlank(message = "El campo nombres no puede estar vacío")
    private String nombres;

    @NotBlank(message = "El campo apellidos no puede estar vacío")
    private String apellidos;

    @NotBlank(message = "El campo correo no puede estar vacío")
    @Email(message = "Correo inválido")
    private String correoUser;


    @NotBlank(message = "Seleccione un tipo de clínica")
    private String clinica;

    @NotBlank(message = "El campo nombres no puede estar vacío")
    private String otraClinica;

    @NotBlank(message = "El campo correo no puede estar vacío")
    @Email(message = "Correo inválido")
    private String correo_nueva_clinica;

    @NotBlank(message = "El campo telefono no puede estar vacío")
    @Size(min = 8, max = 8, message = "telefono debe contener 8 dígitos")
    @Digits(integer = 8, fraction = 0, message = "Teléfono debe contener sólo dígitos")
    private String telefono_nueva_clinica;

    @NotBlank(message = "El campo nombre no puede estar vacío")
    private String otraSede;
    @NotBlank(message = "El campo nombre no puede estar vacío")
    private String sede_nueva_direccion;

//      @NotBlank(message = "Seleccione un tipo de Sede")
    private String sede;

    @NotBlank(message = "El campo nombre no puede estar vacío")
    private String otraSede4;

    @NotBlank(message = "El campo direccion no puede estar vacío")
    private String sede_nueva4_direccion;

    @NotBlank(message = "Seleccione una especialidad")
    private String especialidad;

    // Getters y setters

}