package com.example.proyectogticsgrupo2.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "spring_session")
public class SpringSession implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRIMARY_ID", nullable = false)
    private String primaryId;

    @Column(name = "SESSION_ID",nullable = false)
    private String sessionId;

    @Column(name = "CREATION_TIME",nullable = false)
    private long creationTime;

    @Column(name = "LAST_ACCESS_TIME",nullable = false)
    private long lastAccessTime;

    @Column(name = "MAX_INACTIVE_INTERVAL",nullable = false)
    private int maxInactiveInterval;

    @Column(name = "EXPIRY_TIME",nullable = false)
    private long expiryTime;

    @Column(name = "PRINCIPAL_NAME")
    private String principalName;

}
