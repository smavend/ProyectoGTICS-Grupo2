package com.example.proyectogticsgrupo2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministrativoController {
    @GetMapping("/administrativo")
    public String dashboard(){
        return "administrativo/index";
    }
}
