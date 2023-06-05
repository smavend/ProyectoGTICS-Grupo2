package com.example.proyectogticsgrupo2.service;

import java.util.UUID;
public class TokenService {
    public String generateToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
