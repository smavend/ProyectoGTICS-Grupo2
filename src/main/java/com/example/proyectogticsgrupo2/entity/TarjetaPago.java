package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "tarjeta_pago")
public class TarjetaPago {

    @Id
    @Column(name="numero")
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(min = 16, max = 16, message = "En número de tarjeta debe tener 16 dígitos" )
    private String numeroTarjeta;

    @Column (nullable = false)
    @NotBlank(message = "Este campo no puede estar vacío")
    private String titular;

    @Column (nullable = false)
    @NotBlank(message = "Este campo no puede estar vacío")
    @Size(min = 3, max = 3, message = "En código debe tener 3 dígitos" )
    private String codigo;

    @Column (nullable = false)
    private LocalDate fecha;


}
