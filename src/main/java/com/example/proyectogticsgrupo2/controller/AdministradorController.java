package com.example.proyectogticsgrupo2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {

    @GetMapping("/dashboard")
    public String dashboard (){
        return "administrador/dashboard";
    }
    @GetMapping("/finanzas")
    public String finanzas(){return "administrador/finanzas";}
    @GetMapping("/finanzas-recibos")
    public String finanzas_recibos(){return "administrador/finanzas-recibos";}
    @GetMapping("/crearPaciente")
    public String crearPaciente(){return "administrador/crearPaciente";}
    @GetMapping("/crearDoctor")
    public String crearDoctor(){return "administrador/crearDoctor";}
    @GetMapping("/calendario")
    public String calendario(){return "administrador/calendario";}
    @GetMapping("/mensajeria")
    public String mensajeria(){return "administrador/mensajeria";}
    @GetMapping("/historialPaciente")
    public String historialPaciente(){return "administrador/historialPaciente";}
}
