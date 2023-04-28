package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

@Getter
@Setter
@Entity
@Table(name = "paciente")
public class Paciente {

    @Id
    @Column(name="id_paciente")
    @NotBlank(message = "Este campo no puede estar vacío")
    @Digits(integer = 8, fraction = 0, message = "En DNI debe ser un número y tener 8 dígitos")
    private String idPaciente;

    @Column (nullable = false)
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max = 45, message = "El nombre no puede tener más de 45 caracteres")
    private String nombre;
    @Column (nullable = false)
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max = 45, message = "El apellido no puede tener mas de 45 caracteres")
    private String apellidos;
    @Column (nullable = false)
    private int estado; //no activo: 0, activo: 1,invitado: 2, registrado:3, agendado: 4, en consulta: 5
    @ManyToOne
    @JoinColumn(name="seguro_id_seguro",nullable = false)
    private Seguro seguro;
    @NotBlank(message = "Este campo no puede estar vacío")
    @Digits(integer = 9, fraction = 0, message = "El telefono debe ser un número tener 9 dígitos")
    private String telefono;
    @ManyToOne
    @JoinColumn(name="administrativo_id_administrativo")
    private Administrativo administrativo;
    @Column (nullable = false)
    @NotBlank(message = "Este campo no puede estar vacío")
    @Email(message = "Ingrese una dirección de correo válida")
    private String correo;
    private String fotoname;
    private String fotocontenttype;
    private byte[] foto;
    @ManyToOne
    @JoinColumn(name = "distrito_id_distrito", nullable = false)

    private Distrito distrito;
    @Column (nullable = false)
    @NotBlank(message = "Este campo no puede estar vacío")
    private String direccion;
    @Column (nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechanacimiento;
    @Column (nullable = false)
    @NotBlank(message = "Seleccione un género")
    private String genero;
    @Column (nullable = false)
    private LocalDateTime fecharegistro;

}
