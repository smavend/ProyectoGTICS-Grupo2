package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {
    final PacienteRepository pacienteRepository;
    final DoctorRepository doctorRepository;
    final SeguroRepository seguroRepository;
    final AdministrativoRepository administrativoRepository;
    final DistritoRepository distritoRepository;
    final EspecialidadRepository especialidadRepository;
    final SedeRepository sedeRepository;
    public AdministradorController(PacienteRepository pacienteRepository, DoctorRepository doctorRepository, SeguroRepository seguroRepository, AdministrativoRepository administrativoRepository, DistritoRepository distritoRepository, EspecialidadRepository especialidadRepository, SedeRepository sedeRepository) {
        this.pacienteRepository = pacienteRepository;
        this.doctorRepository = doctorRepository;
        this.seguroRepository = seguroRepository;
        this.administrativoRepository = administrativoRepository;
        this.distritoRepository = distritoRepository;
        this.especialidadRepository = especialidadRepository;
        this.sedeRepository = sedeRepository;
    }

    //#####################################33
    @GetMapping("/dashboard")
    public String dashboard (Model model){
        List<Paciente> listaPaciente =pacienteRepository.findAll();
        List<Doctor> listaDoctores = doctorRepository.findAll();
        model.addAttribute("listaDoctores",listaDoctores);
        model.addAttribute("listaPaciente", listaPaciente);
        return "administrador/dashboard";
    }
    @GetMapping("/finanzas")
    public String finanzas(){return "administrador/finanzas";}
    @GetMapping("/perfil")
    public String perfil(){return "administrador/perfil";}
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
    @PostMapping("/nuevoPaciente")
    public String nuevoPaciente(@RequestParam("idPaciente") String idPaciente ,@RequestParam("nombre") String nombre, @RequestParam("telefono") String telefono,
                                @RequestParam("seguro") int numSeguro, @RequestParam("administrativo") String numAdministrativo,
                                @RequestParam("correo") String correo, @RequestParam("apellidos") String apellido,
                                @RequestParam("direccion") String direccion,@RequestParam("distrito") int numDistrito){

        Optional<Distrito> optdistrito = distritoRepository.findById(numDistrito);
        Optional<Seguro> optSeguro = seguroRepository.findById(numSeguro);
        Optional<Administrativo> optAdministrativo = administrativoRepository.findById(numAdministrativo);
        if(optdistrito.isPresent() && optSeguro.isPresent() && optAdministrativo.isPresent()) {
            Seguro seguro1 = optSeguro.get();
            Distrito distrito1 = optdistrito.get();
            Administrativo admin = optAdministrativo.get();

            pacienteRepository.guardarPaciente(idPaciente, nombre, apellido,1,seguro1.getIdSeguro(), telefono,
                    admin.getIdAdministrativo(), correo, direccion, distrito1.getIdDistrito());
            System.out.println("guardadoooooooooooooooooooooooo");
            return "redirect:/administrador/dashboard";
        }else {
            return "redirect:/administrador/crearPaciente";
        }




    }

    @GetMapping("/crearDoctor")
    public String crearDoctor(Model model){
        List<Especialidad> listaEspecialidad = especialidadRepository.findAll();
        List<Sede> listaSede = sedeRepository.findAll();
        model.addAttribute("listaSede",listaSede);
        model.addAttribute("listaEspecialidad",listaEspecialidad);
        return "administrador/crearDoctor";}
    @GetMapping("/calendario")
    public String calendario(){return "administrador/calendario";}
    @GetMapping("/mensajeria")
    public String mensajeria(){return "administrador/mensajeria";}
    @GetMapping("/historialPaciente")
    public String historialPaciente(){return "administrador/historialPaciente";}

    //###############################3



}
