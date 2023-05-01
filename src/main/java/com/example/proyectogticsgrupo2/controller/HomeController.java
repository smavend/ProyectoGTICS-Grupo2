package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {
    final PacienteRepository pacienteRepository;
    final DoctorRepository doctorRepository;
    final AdministradorRepository administradorRepository;
    final CredencialesRepository credencialesRepository;
    final DistritoRepository distritoRepository;
    final SeguroRepository seguroRepository;

    public HomeController(PacienteRepository pacienteRepository, DoctorRepository doctorRepository, AdministradorRepository administradorRepository, CredencialesRepository credencialesRepository, DistritoRepository distritoRepository, SeguroRepository seguroRepository) {
        this.pacienteRepository = pacienteRepository;
        this.doctorRepository = doctorRepository;
        this.administradorRepository = administradorRepository;
        this.credencialesRepository = credencialesRepository;
        this.distritoRepository = distritoRepository;
        this.seguroRepository = seguroRepository;
    }

    @GetMapping("/")
    public String principal(){
        return "general/home";
    }
    @GetMapping("/invitacion")
    public String invitacion (){return "administrador/invitar";}
    @GetMapping("/sedes")
    public String mostrarSedes(){
        return "general/sedes";
    }
    @GetMapping("/login")
    public String iniciarSesion(){
        return "general/login";
    }
    @GetMapping("/login/olvidepassw")
    public String pedirCorreo(){
        return "general/pedirCorreo";
    }
    @GetMapping("/login/olvidepassw/confirmacion")
    public String mensajeCambioContrasenia(){
        return "general/mensajeSolicitudContrasenia";
    }
    @GetMapping("/login/cambiarpassw")
    public String cambiarContrasenia(){
        return "general/ingresarContrasenia";
    }
    @GetMapping("/login/cambiarpassw/success")
    public String cambioPasswExitoso(){
        return "general/confirmacioncontrasenia";
    }
    @PostMapping("/login/credenciales")
    public String credenciales(Model model, @RequestParam("username") String correo, @RequestParam("password") String password){

        Optional<Credenciales> optCredenciales = credencialesRepository.findByCorreoAndContrasena(correo,password);
        if(optCredenciales.isPresent()){
            Credenciales credencial = optCredenciales.get();
            Optional<Paciente> optionalPaciente = pacienteRepository.findById(credencial.getId_credenciales());
            Optional<Administrador> optAdministrador = administradorRepository.findById(credencial.getId_credenciales());
            if(optAdministrador.isPresent()){

                return "redirect:/administrador/dashboard";
            }else if (optionalPaciente.isPresent()){
                Paciente paciente = optionalPaciente.get();
                model.addAttribute("paciente",paciente);
                return "paciente/index";
            }else {
                return "redirect:/login"; //continuara
            }

        }else {
            return "redirect:/login";
        }
    }
    @GetMapping("/signin")
    public String vistaRegistro(Model model){
        List<Distrito> list = distritoRepository.findAll();
        List<Seguro> list1 = seguroRepository.findAll();
        model.addAttribute("distritos", list);
        model.addAttribute("seguros", list1);
        return "general/registro";
    }

    @GetMapping("/signin/confirmacion")
    public String confirmacionRegistro(){
        return "general/confirmacionregistro";
    }

    @GetMapping("/usuario/foto/{id}")
    public void showUsuarioImage(@PathVariable String id,
                                 HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");

        Optional<Paciente> optPaciente = pacienteRepository.findById(id);
        Optional<Doctor> optDoctor = doctorRepository.findById(id);

        if(optPaciente.isPresent()){
            Paciente paciente = optPaciente.get();
            InputStream is = new ByteArrayInputStream(paciente.getFoto());
            IOUtils.copy(is, response.getOutputStream());
        } else if (optDoctor.isPresent()) {
            Doctor doctor = optDoctor.get();
            InputStream is = new ByteArrayInputStream(doctor.getFoto());
            IOUtils.copy(is, response.getOutputStream());
        }
    }
    @PostMapping("/general/registrar")
    public String registrarPaciente(Paciente paciente){
        paciente.setFecharegistro(LocalDateTime.now());
        paciente.setEstado(3);
        pacienteRepository.save(paciente);
        return "general/confirmacionregistro";
    }
}
