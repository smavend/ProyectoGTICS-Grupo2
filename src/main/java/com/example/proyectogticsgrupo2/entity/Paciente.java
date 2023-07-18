package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

import java.time.Period;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "paciente")
public class Paciente implements Serializable {

    @Id
    @Column(name="id_paciente")
    @NotBlank(message = "Este campo no puede estar vacío")
    //@Digits(integer = 8, fraction = 0, message = "En DNI debe ser un número")
    @Size(min = 8, max = 8, message = "En DNI debe tener 8 dígitos" )
    private String idPaciente;

    @Column (nullable = false)
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max = 45, message = "El nombre no puede tener más de 45 caracteres")
    @Pattern(regexp = "^[A-Za-z]+(\\s[A-Za-z]+)*$", message = "Ingrese un nombre válido")
    private String nombre;

    @Column (nullable = false)
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(max = 45, message = "El apellido no puede tener mas de 45 caracteres")
    @Pattern(regexp = "^[A-Za-z]+(\\s[A-Za-z]+)*$", message = "Ingrese un apellido válido")
    private String apellidos;

    @Column (nullable = false)
    private int estado; //no activo: 0, activo: 1,invitado: 2, registrado:3, agendado: 4, en consulta: 5

    @ManyToOne
    @JoinColumn(name="seguro_id_seguro",nullable = false)
    private Seguro seguro;

    @NotBlank(message = "Este campo no puede estar vacío")
    //@Digits(integer = 9, fraction = 0, message = "El teléfono debe ser un número")
    @Size(min = 9, max = 9, message = "El teléfono debe tener 9 dígitos" )
    @Pattern(regexp = "^\\d{9}$", message = "Ingrese un número válido")
    private String telefono;

    @ManyToOne
    @JoinColumn(name="administrativo_id_administrativo")
    private Administrativo administrativo;

    @Column (nullable = false)
    @NotBlank(message = "Este campo no puede estar vacío")
    @Pattern(regexp = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$", message = "Ingrese una dirección de correo válida")
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
    @NotNull(message = "Seleccione una fecha")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechanacimiento;

    @Column (nullable = false)
    @NotBlank(message = "Seleccione un género")
    private String genero;

    @Column (nullable = false)
    private LocalDateTime fecharegistro;


    private LocalDateTime fechainvitado;

    public String getNombreYApellido(){
        String[] nombres = this.getNombre().split(" ");
        String[] apellidos = this.getApellidos().split(" ");
        return nombres[0] + " " + apellidos[0];
    }

    public String getEdad(){
        LocalDate fechaActual = LocalDate.now();
        if (this.getFechanacimiento() != null){
            return String.valueOf(Period.between(this.getFechanacimiento(), fechaActual).getYears());
        }
        else {
            return "0";
        }
    }

}
