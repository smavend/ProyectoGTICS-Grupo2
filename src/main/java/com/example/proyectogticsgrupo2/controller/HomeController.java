package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import jakarta.servlet.http.HttpServletResponse;
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


    @GetMapping("/signin")
    public String vistaRegistro(Model model, @ModelAttribute("paciente") Paciente paciente){
        List<Distrito> list = distritoRepository.findAll();
        List<Seguro> list1 = seguroRepository.findAll();
        model.addAttribute("distritos", list);
        model.addAttribute("seguros", list1);
        return "general/registro";
    }

    @PostMapping("/signin/save")
    public String guardarRegistro (@RequestParam ("radios") String radio,
                                   @RequestParam ("alergias") String alergias,
                                   Model model,
                                   RedirectAttributes attr,
                                   @ModelAttribute("paciente") @Valid Paciente paciente,
                                   BindingResult bindingResult){
        List<Distrito> list = distritoRepository.findAll();
        List<Seguro> list1 = seguroRepository.findAll();
        if(bindingResult.hasErrors()){
            model.addAttribute("distritos", list);
            model.addAttribute("seguros", list1);
            return "general/registro";
        }else{
            List<Paciente> pacientes = pacienteRepository.findAll();
            boolean exist = false;
            boolean existTemp = false;
            String msgDni = "";
            String msgEmail = "";
            for (Paciente p : pacientes) {
                if (p.getIdPaciente().equals(paciente.getIdPaciente())) {
                    exist = true;
                    msgDni = "Este DNI ya se encuentra registrado";
                    break;
                } else if (p.getCorreo().equals(paciente.getCorreo())) {
                    exist = true;
                    msgEmail = "Este correo ya se encuentra registrado";
                    break;
                }
            }
            List<Temporal> temp = temporalRepository.findAll();
            for (Temporal t : temp) {
                if (t.getDni().equals(paciente.getIdPaciente())) {
                    existTemp = true;
                    msgDni = "Este DNI ya se encuentra registrado";
                    break;
                } else if (t.getCorreo().equals(paciente.getCorreo())) {
                    existTemp = true;
                    msgEmail = "Este correo ya se encuentra registrado";
                    break;
                }
            }
            if (!exist & !existTemp) {
                String msgGen = "";
                if(paciente.getGenero().equals("M")){
                    paciente.setGenero("Masculino");
                } else if (paciente.getGenero().equals("F")) {
                    paciente.setGenero("Femenino");
                }else{
                    msgGen = "Debe seleccionar uno de los géneros listados";
                    attr.addFlashAttribute("msgGen", msgGen);
                    model.addAttribute("distritos", list);
                    model.addAttribute("seguros", list1);
                    return "general/registro";
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
                             alergia1 = new Alergia();
                             alergia1.setNombre(alergia);
                             alergia1.setPaciente(paciente);
                             alergiaRepository.save(alergia1);
                        }
                    }
                }
                return "redirect:/signin/confirmacion";
            } else {
                attr.addFlashAttribute("msgDni", msgDni);
                attr.addFlashAttribute("msgEmail", msgEmail);
                model.addAttribute("distritos", list);
                model.addAttribute("seguros", list1);
                return "general/registro";
            }
        }
    }

    @GetMapping("/signin/confirmacion")
    public String confirmacionRegistro(){
        return "general/confirmacionregistro";
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
