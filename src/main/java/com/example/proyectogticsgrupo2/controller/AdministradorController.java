package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.Administrativo;
import com.example.proyectogticsgrupo2.entity.Distrito;
import com.example.proyectogticsgrupo2.entity.Paciente;
import com.example.proyectogticsgrupo2.entity.Seguro;
import com.example.proyectogticsgrupo2.repository.AdministrativoRepository;
import com.example.proyectogticsgrupo2.repository.DistritoRepository;
import com.example.proyectogticsgrupo2.repository.PacienteRepository;
import com.example.proyectogticsgrupo2.repository.SeguroRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {
    final PacienteRepository pacienteRepository;
    final SeguroRepository seguroRepository;
    final AdministrativoRepository administrativoRepository;
    final DistritoRepository distritoRepository;
    public AdministradorController(PacienteRepository pacienteRepository, SeguroRepository seguroRepository, AdministrativoRepository administrativoRepository, DistritoRepository distritoRepository) {
        this.pacienteRepository = pacienteRepository;
        this.seguroRepository = seguroRepository;
        this.administrativoRepository = administrativoRepository;
        this.distritoRepository = distritoRepository;
    }

    //#####################################33
    @GetMapping("/dashboard")
    public String dashboard (Model model){
        List<Paciente> listaPaciente =pacienteRepository.findAll();
        model.addAttribute("listaPaciente", listaPaciente);
        return "administrador/dashboard";
    }
    @GetMapping("/finanzas")
    public String finanzas(){return "administrador/finanzas";}
    @GetMapping("/finanzas-recibos")
    public String finanzas_recibos(){return "administrador/finanzas-recibos";}
    @GetMapping("/crearPaciente")
    public String crearPaciente(Model model){
    List<Seguro> listaSeguro  = seguroRepository.findAll();
    List<Distrito> listaDistrito = distritoRepository.findAll();
    List<Administrativo> listaAdministrativo = administrativoRepository.findAll();
    model.addAttribute("listaSeguro",listaSeguro);
    model.addAttribute("listaDistrito",listaDistrito);
    model.addAttribute("listaAdministrativo",listaAdministrativo);
        return "administrador/crearPaciente";}
    @GetMapping("/crearDoctor")
    public String crearDoctor(){return "administrador/crearDoctor";}
    @GetMapping("/calendario")
    public String calendario(){return "administrador/calendario";}
    @GetMapping("/mensajeria")
    public String mensajeria(){return "administrador/mensajeria";}
    @GetMapping("/historialPaciente")
    public String historialPaciente(){return "administrador/historialPaciente";}

    //###############################3



}
