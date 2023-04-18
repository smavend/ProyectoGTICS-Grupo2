package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.Administrador;
import com.example.proyectogticsgrupo2.entity.Administrativo;
import com.example.proyectogticsgrupo2.entity.Paciente;
import com.example.proyectogticsgrupo2.repository.AdministradorRepository;
import com.example.proyectogticsgrupo2.repository.AdministrativoRepository;
import com.example.proyectogticsgrupo2.repository.PacienteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/SuperAdminHomePage")
public class SuperAdminController {
    /**
     * Inyección de dependencias
     */
    final PacienteRepository pacienteRepository;

    final AdministrativoRepository administrativoRepository;
    final AdministradorRepository administradorRepository;
   /*public SuperAdminController(PacienteRepository pacienteRepository,AdministradorRepository administradorRepository, AdministrativoRepository administrativoRepository) {
        this.pacienteRepository = pacienteRepository;
        this.administradorRepository = administradorRepository;
        this.administrativoRepository = administrativoRepository;
    }*/

    public SuperAdminController(PacienteRepository pacienteRepository,
                                AdministrativoRepository administrativoRepository,
                                AdministradorRepository administradorRepository) {
        this.pacienteRepository = pacienteRepository;
        this.administrativoRepository = administrativoRepository;
        this.administradorRepository = administradorRepository;
    }

    @GetMapping("")
    public String HomePageSuperAdmin(Model model) {
        List<Paciente> listaPaciente = pacienteRepository.findAll();
        List<Administrativo> listaAdministrativo = administrativoRepository.findAll();
        List<Administrador> listaAdministrador = administradorRepository.findAll();
        model.addAttribute("listaAdministradores",listaAdministrador);
        model.addAttribute("listaAdministrativo", listaAdministrativo);
        model.addAttribute("listaPaciente", listaPaciente);

        return "superAdmin/superadmin-dashboard";
    }
    @GetMapping("/crearformulario")
    public String crearFormulario() {
        return "superAdmin/superadmin-crearformulario";
    }

}
