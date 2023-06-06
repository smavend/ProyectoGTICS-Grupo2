package com.example.proyectogticsgrupo2.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import jakarta.validation.constraints.*;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name="doctor")
public class Doctor implements Serializable {
    @Id
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(min = 8,max = 8,message = "En DNI debe tener 8 dígitos" )
    @Column(nullable = false)
    private String id_doctor;
    @Column(nullable = false)
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max = 45, message = "El nombre no puede tener más de 45 caracteres")
    private String nombre;
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max = 45, message = "El apellido no puede tener más de 45 caracteres")
    @Column(nullable = false)
    private String apellidos;
    @Column(nullable = false)
    private int estado;

    @ManyToOne
    @JoinColumn(name = "especialidad_id_especialidad",nullable = false)
    private Especialidad especialidad;

    @ManyToOne
    @JoinColumn(name = "sede_id_sede",nullable = false)
    private Sede sede;

    @Column(name = "duracion_cita_minutos")
    private Integer duracion_cita_minutos;

    @Column(name = "pregrado")
    @Size(max = 100, message = "El pregrado no puede tener más de 100 caracteres")
    private String pregrado;

    @Column(name = "posgrado")
    @Size(max = 100, message = "El posgrado no puede tener más de 100 caracteres")
    private String posgrado;

    
    @Size(max = 10, message = "La colegiatura no puede tener más de 10 caracteres")
    private String colegiatura;
    @ManyToOne
    @JoinColumn(name = "horario_id_horario")
    private Horario horario;

    
    @NotBlank(message = "Este campo no puede estar vacío")
    @Email(message = "Ingrese una dirección de correo válida")
    private String correo;

    private String fotoname;
    private String fotocontenttype;
    private byte[] foto;

    @NotBlank(message = "Seleccione un género")
    private String genero;

    public String getNombreYApellido(){
        String[] nombres = this.getNombre().split(" ");
        String[] apellidos = this.getApellidos().split(" ");
        return nombres[0] + " " + apellidos[0];
    }


}
