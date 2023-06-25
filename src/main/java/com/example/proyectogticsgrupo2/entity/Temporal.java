package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table (name = "temporal")
public class Temporal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario_temporal")
    private int id_temporal;

    @Column(nullable = false)
    @NotBlank(message = "Debe ingresar un DNI")
    @Digits(integer = 8, fraction = 0, message = "Ingrese un número válido")
    @Size(min = 8, max = 8, message = "Debe ingresar un número de 8 dígitos")
    private String dni;

    @Column(nullable = false)
    @NotBlank(message = "Debe ingresar un nombre")
    @Size(min = 2,max = 45, message = "Debe ingresar un nombre entre 2 y 45 caracteres")
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "Debe ingresar sus apellidos")
    @Size(min = 2, max = 45,message = "Debe ingresar los apellidos entre 2 y 45 caracteres")
    private String apellidos;

    @Column(nullable = false)
    @NotBlank(message = "Ingrese un correo")
    @Email(message = "Debe ingresar un correo válido")
    private String correo;

    private String telefono;

    private LocalDate fecha_nacimiento;

    private LocalDateTime fechainvitado;

    private int llenado;

    @ManyToOne
    @JoinColumn(name = "administrativo_id_administrativo")
    private Administrativo administrativo;
    @ManyToOne
    @JoinColumn(name = "seguro_id_seguro")
    private Seguro seguro;
    @ManyToOne
    @JoinColumn(name = "distrito_id_distrito")
    private Distrito distrito;

    private String direccion;

    private Integer estado;

    private String genero;

    public String getNombreYApellido(){
        String[] nombres = this.getNombre().split(" ");
        String[] apellidos = this.getApellidos().split(" ");
        return nombres[0] + " " + apellidos[0];
    }
}
