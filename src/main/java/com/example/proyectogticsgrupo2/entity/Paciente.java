package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "paciente")
@Getter
@Setter
public class Paciente {
    @Id
    @Column(name="id_paciente", nullable = false)
    private String idPaciente;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private int estado; //activo: 1, no activo: 0, otros

    @ManyToOne
    @JoinColumn(name="seguro_id_seguro",nullable = false)
    private Seguro seguro;

    @Column(nullable = false)
    private String telefono;

    @ManyToOne
    @JoinColumn(name="administrativo_id_administrativo",nullable = false)
    private Administrativo administrativo;

    @Column(nullable = false)
    private String correo;

    @Lob
    @Column(nullable = false)
    private byte[] foto;

    @Column(nullable = false)
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "distrito_id_distrito")
    /**
     * Aquí falta digamos mmm declarar la entidad
     */
    private Distrito distrito;

    public String obtenerSede() {
        // Verifica si hay un administrativo asignado al paciente
        if (administrativo != null && administrativo.getSedesAdministrativo() != null && !administrativo.getSedesAdministrativo().isEmpty()) {
            // Retorna la primera sede asociada al administrativo
            return administrativo.getSedesAdministrativo().get(0).getSede().getNombre();
        }
        // Retorna null si no hay un administrativo asignado al paciente o si el administrativo no tiene sedes asociadas
        return null;
    }


}
