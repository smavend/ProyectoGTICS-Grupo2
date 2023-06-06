package com.example.proyectogticsgrupo2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "stylevistas")
public class Stylevistas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user")
    private String user;

    @Column(name = "Header")
    private String header;

  /*  @Column(name= "Sidebar")
    private String sidebar;
*/
    @Column(name = "Footer")
    private String footer;

    @Column(name = "Background")
    private String background;

    // Getters y setters
}
