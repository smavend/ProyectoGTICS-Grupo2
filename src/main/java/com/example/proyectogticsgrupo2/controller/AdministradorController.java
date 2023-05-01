package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
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
    final AdministradorRepository administradorRepository;
    public AdministradorController(PacienteRepository pacienteRepository, DoctorRepository doctorRepository, SeguroRepository seguroRepository, AdministrativoRepository administrativoRepository, DistritoRepository distritoRepository, EspecialidadRepository especialidadRepository, SedeRepository sedeRepository, AdministradorRepository administradorRepository) {
        this.pacienteRepository = pacienteRepository;
        this.doctorRepository = doctorRepository;
        this.seguroRepository = seguroRepository;
        this.administrativoRepository = administrativoRepository;
        this.distritoRepository = distritoRepository;
        this.especialidadRepository = especialidadRepository;
        this.sedeRepository = sedeRepository;
        this.administradorRepository = administradorRepository;
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
    public String perfil(@RequestParam("id") String id, Model model){
        Optional<Administrador> optAministrador = administradorRepository.findById(id);
        Administrador administrador= optAministrador.get();
        model.addAttribute("administrador", administrador);
        return "administrador/perfil";}
    @GetMapping("/finanzas-recibos")
    public String finanzas_recibos(){return "administrador/finanzas-recibos";}
    //###########################################################################
    @GetMapping("/crearPaciente")
    public String crearPaciente( @ModelAttribute("paciente") Paciente paciente,  Model model){
    List<Seguro> listaSeguro  = seguroRepository.findAll();
    List<Distrito> listaDistrito = distritoRepository.findAll();
    List<Administrativo> listaAdministrativo = administrativoRepository.findAll();
    model.addAttribute("listaSeguro",listaSeguro);
    model.addAttribute("listaDistrito",listaDistrito);
    model.addAttribute("listaAdministrativo",listaAdministrativo);
        return "administrador/crearPaciente";}
    @PostMapping("/guardarPaciente")
    public String guardarEmpleado(@RequestParam("archivo") MultipartFile file,
                                  @ModelAttribute("paciente") @Valid Paciente paciente, BindingResult bindingResult, Model model, RedirectAttributes attr){
        if(bindingResult.hasErrors()){
            List<Seguro> listaSeguro  = seguroRepository.findAll();
            List<Distrito> listaDistrito = distritoRepository.findAll();
            List<Administrativo> listaAdministrativo = administrativoRepository.findAll();
            model.addAttribute("listaSeguro",listaSeguro);
            model.addAttribute("listaDistrito",listaDistrito);
            model.addAttribute("listaAdministrativo",listaAdministrativo);
            return "administrador/crearPaciente";
        } else{
            if (file.isEmpty()) {
                model.addAttribute("msg", "Debe subir un archivo");
                return "redirect:/administrador/crearPaciente";
            }
            String fileName = file.getOriginalFilename();
            if (fileName.contains("..")) {
                model.addAttribute("msg", "No se permiten '..' en el archivo");
                return "redirect:/administrador/crearPaciente";
            }
            try {
                paciente.setFoto(file.getBytes());
                paciente.setFotoname(fileName);
                paciente.setFotocontenttype(file.getContentType());
                paciente.setEstado(1);
                paciente.setFecharegistro(LocalDateTime.now());
                pacienteRepository.save(paciente);
                attr.addFlashAttribute("msgPaci","Paciente creado exitosamente");
                return "redirect:/administrador/dashboard";
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("msg", "ocurrió un error al subir el archivo");
                return "redirect:/administrador/crearPaciente";
            }
        }
    }
    //###########################################################################
    @GetMapping("/crearDoctor")
    public String crearDoctor(@ModelAttribute("doctor") Doctor doctor,Model model){
        List<Especialidad> listaEspecialidad = especialidadRepository.findAll();
        List<Sede> listaSede = sedeRepository.findAll();
        model.addAttribute("listaSede",listaSede);
        model.addAttribute("listaEspecialidad",listaEspecialidad);
        return "administrador/crearDoctor";}
    @PostMapping("/guardarDoctor")
    public String guardarDoctor(@RequestParam("archivo") MultipartFile file,
                                @ModelAttribute("doctor") @Valid Doctor doctor, BindingResult bindingResult,
                                Model model, RedirectAttributes attr){
        if(bindingResult.hasErrors()){
            List<Especialidad> listaEspecialidad = especialidadRepository.findAll();
            List<Sede> listaSede = sedeRepository.findAll();
            model.addAttribute("listaSede",listaSede);
            model.addAttribute("listaEspecialidad",listaEspecialidad);
            return "administrador/crearDoctor";
        }else {
            /*if (file.isEmpty()) {
                model.addAttribute("msg", "Debe subir un archivo");
                return "redirect:/administrador/crearDoctor";
            }*/
            String fileName = file.getOriginalFilename();
            /*if (fileName.contains("..")) {
                model.addAttribute("msg", "No se permiten '..' en el archivo");
                return "redirect:/administrador/crearDoctor";
            }*/

            try {
                doctor.setFoto(file.getBytes());
                doctor.setFotoname(fileName);
                doctor.setFotocontenttype(file.getContentType());
                doctor.setEstado(1);
                doctorRepository.save(doctor);
                attr.addFlashAttribute("msgDoc","Doctor creado exitosamente");
                return "redirect:/administrador/dashboard";
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("msg", "ocurrió un error al subir el archivo");
                return "redirect:/administrador/crearDoctor";
            }
        }


    }
    @GetMapping("/calendario")
    public String calendario(){return "administrador/calendario";}
    @GetMapping("/mensajeria")
    public String mensajeria(){return "administrador/mensajeria";}
    @GetMapping("/historialPaciente")
    public String historialPaciente(@RequestParam("id") String id, Model model){
        Optional<Paciente> optPaciente = pacienteRepository.findById(id);
        if(optPaciente.isPresent()){
            Paciente paciente = optPaciente.get();
            model.addAttribute("paciente", paciente);
            return "administrador/historialPaciente";
        }else {
            return "redirect:/administrador/dashboard";
        }

    }

    @GetMapping("/imagePaci/{id}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable("id") String dni) {
        Optional<Paciente> opt = pacienteRepository.findById(dni);

        if (opt.isPresent()) {
            Paciente p = opt.get();

            byte[] imagenComoBytes = p.getFoto();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(
                    MediaType.parseMediaType(p.getFotocontenttype()));

            return new ResponseEntity<>(
                    imagenComoBytes,
                    httpHeaders,
                    HttpStatus.OK);
        } else {
            return null;
        }
    }
    @GetMapping("/invitacion")
    public String invitacion (){
        return "administrador/invitar";
    }
    @GetMapping("/imageDoc/{id}")
    public ResponseEntity<byte[]> mostrarImagenDoc(@PathVariable("id") String dni) {
        Optional<Doctor> opt = doctorRepository.findById(dni);

        if (opt.isPresent()) {
            Doctor doc = opt.get();

            byte[] imagenComoBytes = doc.getFoto();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(
                    MediaType.parseMediaType(doc.getFotocontenttype()));

            return new ResponseEntity<>(
                    imagenComoBytes,
                    httpHeaders,
                    HttpStatus.OK);
        } else {
            return null;
        }
    }

    //###############################3



}
