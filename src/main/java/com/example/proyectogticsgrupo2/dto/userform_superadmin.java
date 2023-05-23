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
    private String dni;

    @NotBlank(message = "El campo nombres no puede estar vacío")
    private String nombres;

    @NotBlank(message = "El campo apellidos no puede estar vacío")
    private String apellidos;

    @NotBlank(message = "El campo correo no puede estar vacío")
    @Email(message = "Correo inválido")
    private String correoUser;

    @NotBlank(message = "Seleccione un tipo de Sede")
    private String sede;

    @NotBlank(message = "Seleccione una especialidad")
    private String especialidad;

    // Getters y setters

}