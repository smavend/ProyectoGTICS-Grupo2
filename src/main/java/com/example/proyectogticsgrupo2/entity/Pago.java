package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private int id;

    @ManyToOne
    @JoinColumn(name="cita_id_cita", nullable = false)
    private Cita cita;

    @Column(name = "fecha_emitida")
    private LocalDate fechaEmitida;

    @Column(name = "fecha_cancelada")
    private LocalDate fechaCancelada;

    @Column(name = "estado_pago")
    private int estadoPago;

    @Column(name = "tipo_pago")
    private String tipoPago;

    @Column(name = "codigo_recibo")
    private String codigoRecibo;

}
