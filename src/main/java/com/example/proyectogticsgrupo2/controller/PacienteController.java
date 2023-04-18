package com.example.proyectogticsgrupo2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PacienteController {

    @GetMapping(value = {"", "/", "index"})
    public String index(){
        return "paciente/index";
    }

    @GetMapping(value = {"/perfil"})
    public String perfil(){
        return "paciente/perfil";
    }

    @GetMapping(value = {"/reservarCita"})
    public String reservarCita(){
        return "paciente/reservarCita";
    }

    @GetMapping(value = {"/verCitas"})
    public String verCitas(){
        return "paciente/verCitas";
    }

    @GetMapping(value = {"/pagos"})
    public String pagos(){
        return "paciente/pagos";
    }

    @GetMapping(value = {"/cuestionarios"})
    public String cuestionarios(){ return "paciente/cuestionarios"; }

    @GetMapping(value = {"/consentimientos"})
    public String consentimientos(){ return "paciente/consentimientos"; }
}
