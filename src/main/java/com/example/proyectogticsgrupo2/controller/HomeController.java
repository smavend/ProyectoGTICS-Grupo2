package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class HomeController {
    final PacienteRepository pacienteRepository;
    final DoctorRepository doctorRepository;
    final AdministradorRepository administradorRepository;
    final CredencialesRepository credencialesRepository;
    final DistritoRepository distritoRepository;
    final SeguroRepository seguroRepository;
    final TemporalRepository temporalRepository;
    final AlergiaRepository alergiaRepository;

    public HomeController(PacienteRepository pacienteRepository, DoctorRepository doctorRepository, AdministradorRepository administradorRepository, CredencialesRepository credencialesRepository, DistritoRepository distritoRepository, SeguroRepository seguroRepository, TemporalRepository temporalRepository, AlergiaRepository alergiaRepository) {
        this.pacienteRepository = pacienteRepository;
        this.doctorRepository = doctorRepository;
        this.administradorRepository = administradorRepository;
        this.credencialesRepository = credencialesRepository;
        this.distritoRepository = distritoRepository;
        this.seguroRepository = seguroRepository;
        this.temporalRepository = temporalRepository;
        this.alergiaRepository = alergiaRepository;
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


    @GetMapping(value = {"/signin","/signin/save"})
    public String vistaRegistro(Model model, @ModelAttribute("paciente") Paciente paciente){
        List<Distrito> list = distritoRepository.findAll();
        List<Seguro> list1 = seguroRepository.findAll();
        model.addAttribute("distritos", list);
        model.addAttribute("seguros", list1);
        return "general/registro";
    }

    @PostMapping("/signin/save")
    public String guardarRegistro (HttpSession session,
                                   @RequestParam ("radios") String radio,
                                   @RequestParam ("alergias") String alergias,
                                   Model model,
                                   RedirectAttributes attr,
                                   @ModelAttribute("paciente") @Valid Paciente paciente,
                                   BindingResult bindingResult){
        List<Distrito> list = distritoRepository.findAll();
        List<Seguro> list1 = seguroRepository.findAll();
        List<Paciente> pacientes = pacienteRepository.findAll();
        boolean existDni = false;
        boolean existCorreo = false;
        boolean existDni1 = false;
        boolean existCorreo1 = false;
        paciente.setCorreo(paciente.getCorreo().toLowerCase());
        for (Paciente p : pacientes) {
            if (p.getIdPaciente().equals(paciente.getIdPaciente())) {
                existDni = true;
                break;
            } else if (p.getCorreo().equals(paciente.getCorreo())) {
                existCorreo = true;
                break;
            }
        }
        List<Temporal> temp = temporalRepository.findAll();
        for (Temporal t : temp) {
            if (t.getDni().equals(paciente.getIdPaciente())) {
                existDni1 = true;
                break;
            } else if (t.getCorreo().equals(paciente.getCorreo())) {
                existCorreo1 = true;
                break;
            }
        }
        if(bindingResult.hasErrors() || (!paciente.getGenero().equals("M") && !paciente.getGenero().equals("F")) || existCorreo1 || existDni1 || existCorreo || existDni){
            if(existDni || existDni1){
                bindingResult.rejectValue("idPaciente", "errorDni", "El DNI ya se encuentra registrado.");
            }
            if(existCorreo || existCorreo1){
                bindingResult.rejectValue("correo","errorCorreo", "El correo ya se encuentra registrado.");
            }
            if(!paciente.getGenero().equals("M") && !paciente.getGenero().equals("F")){
                bindingResult.rejectValue("genero", "errorGenero", "Debe seleccionar uno de los géneros listados");
            }
            model.addAttribute("distritos", list);
            model.addAttribute("seguros", list1);
            return "general/registro";
        }else{
            if(paciente.getGenero().equals("M")){
                paciente.setGenero("Masculino");
            } else{
                paciente.setGenero("Femenino");
            }
            paciente.setEstado(3);
            paciente.setFecharegistro(LocalDateTime.now());

            pacienteRepository.save(paciente);
            if(radio.equals("1")){
                String[] alergiasArray = alergias.split(",");
                String alergia = "";
                Alergia alergia1 = null;
                for(int i = 0; i<alergiasArray.length; i++){
                    alergia = alergiasArray[i].trim();
                    alergia = alergia.replaceAll(" +"," ");
                    if(!alergia.equals(" ") && !alergia.equals("")){
                        alergia = alergia.substring(0,1).toUpperCase() + alergia.substring(1).toLowerCase();
                        alergia1 = new Alergia();
                        alergia1.setNombre(alergia);
                        alergia1.setPaciente(paciente);
                        alergiaRepository.save(alergia1);
                    }
                }
            }
            session.setAttribute("registro", paciente.getIdPaciente());
            return "redirect:/signin/confirmacion";
        }
    }

    @GetMapping("/signin/confirmacion")
    public String confirmacionRegistro(HttpSession session){
        String id = (String) session.getAttribute("registro");
        if(id!=null){
            return "general/confirmacionregistro";
        }else {
            return "redirect:/";
        }
    }

    @GetMapping("/usuario/foto/{id}")
    public void showUsuarioImage(@PathVariable String id,
                                 HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        response.setContentType("image/png");
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
}
