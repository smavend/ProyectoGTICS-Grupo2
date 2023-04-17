package com.example.proyectogticsgrupo2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PacienteController {

    @GetMapping(value = {"", "/", "index"})
    public String index(){
        return "paciente/index";
    }
}
