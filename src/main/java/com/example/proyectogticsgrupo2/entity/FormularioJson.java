package com.example.proyectogticsgrupo2.entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "formularios_json")
public class FormularioJson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 45)
    private String titulo;

    @Column(nullable = false, length = 5000)
    private String estructura_formulario;

    @Column(nullable = false)
    private int sent;

    @Column(nullable = false)
    private String rutaController;
    // Los getters y setters para cada campo van aquí
    // ...
}
