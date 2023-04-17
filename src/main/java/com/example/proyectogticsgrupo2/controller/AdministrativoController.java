package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.Paciente;
import com.example.proyectogticsgrupo2.repository.PacienteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdministrativoController {
    final PacienteRepository pacienteRepository;

    public AdministrativoController(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping("/administrativo")
    public String dashboard(Model model){
        List<Paciente> lista = pacienteRepository.buscarPorIdAdministrativo("15648912");
        model.addAttribute("listaPacientes", lista);
        return "administrativo/index";
    }
}
