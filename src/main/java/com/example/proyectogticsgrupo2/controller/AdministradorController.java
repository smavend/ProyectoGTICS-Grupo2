package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import com.example.proyectogticsgrupo2.service.CorreoService;
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

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
    final CredencialesRepository credencialesRepository;
    final TemporalRepository temporalRepository;
    public AdministradorController(PacienteRepository pacienteRepository, DoctorRepository doctorRepository, SeguroRepository seguroRepository, AdministrativoRepository administrativoRepository, DistritoRepository distritoRepository, EspecialidadRepository especialidadRepository, SedeRepository sedeRepository, AdministradorRepository administradorRepository, CredencialesRepository credencialesRepository, TemporalRepository temporalRepository) {
        this.pacienteRepository = pacienteRepository;
        this.doctorRepository = doctorRepository;
        this.seguroRepository = seguroRepository;
        this.administrativoRepository = administrativoRepository;
        this.distritoRepository = distritoRepository;
        this.especialidadRepository = especialidadRepository;
        this.sedeRepository = sedeRepository;
        this.administradorRepository = administradorRepository;
        this.credencialesRepository = credencialesRepository;
        this.temporalRepository = temporalRepository;
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
    @GetMapping("/config")
    public String config(){return "administrador/config";}
    @GetMapping("/registro")
    public String registro(Model model){
        List<Temporal> listaTemporal = temporalRepository.findAll();
        model.addAttribute("listaTemporal",listaTemporal);
        return "administrador/rptaForm";}
    @PostMapping("/guardarTemporales")
    public String guardarTemporales(@RequestParam("usuarios") List<Integer> ids, Paciente paciente, RedirectAttributes attr){
        List<Temporal> pacientesTemp = temporalRepository.findAllById(ids);
            //Cuanto funcione perfectamente los temporales, entonces los filtro por llenado 1
            // y usare el datablindig
            for (Temporal pacitemp : pacientesTemp){
                paciente.setIdPaciente(pacitemp.getDni());
                paciente.setNombre(pacitemp.getNombre());
                paciente.setApellidos(pacitemp.getApellidos());
                paciente.setTelefono(pacitemp.getTelefono());
                paciente.setDireccion("Av Calle 124");
                paciente.setFechanacimiento(pacitemp.getFecha_nacimiento());
                paciente.setSeguro(pacitemp.getSeguro());
                paciente.setDistrito(pacitemp.getDistrito());
                paciente.setGenero("Masculino");
                paciente.setCorreo(pacitemp.getCorreo());
                paciente.setEstado(1);
                paciente.setFecharegistro(LocalDateTime.now());
                paciente.setFoto(null);
                paciente.setFotoname(null);
                paciente.setFotocontenttype(null);
                pacienteRepository.save(paciente);
                temporalRepository.deleteById(pacitemp.getId_temporal());

                CorreoService correoService= new CorreoService();
                correoService.props(paciente.getCorreo(),paciente.getNombre());
                attr.addFlashAttribute("msgPaci","Pacientes creados exitosamente");

            }
            return "redirect:/administrador/dashboard";

    }
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
                                  @ModelAttribute("paciente") @Valid Paciente paciente, BindingResult bindingResult,
                                  Model model, RedirectAttributes attr){
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
                    paciente.setFoto(null);
                    paciente.setFotoname(null);
                    paciente.setFotocontenttype(null);

            }else{
                String fileName = file.getOriginalFilename();
                try {
                    paciente.setFoto(file.getBytes());
                    paciente.setFotoname(fileName);
                    paciente.setFotocontenttype(file.getContentType());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            paciente.setEstado(1);
            paciente.setFecharegistro(LocalDateTime.now());
            pacienteRepository.save(paciente);
            credencialesRepository.crearCredenciales(paciente.getIdPaciente(),paciente.getCorreo(),paciente.getNombre());
            CorreoService correoService = new CorreoService();
            correoService.props(paciente.getCorreo(),paciente.getNombre());
            attr.addFlashAttribute("msgPaci","Paciente creado exitosamente");
            return "redirect:/administrador/dashboard";
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
            if(file.isEmpty()){
                    doctor.setFoto(null);
                    doctor.setFotoname(null);
                    doctor.setFotocontenttype(null);
            }else {
                String fileName = file.getOriginalFilename();
                try {
                    doctor.setFoto(file.getBytes());
                    doctor.setFotoname(fileName);
                    doctor.setFotocontenttype(file.getContentType());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            doctor.setEstado(1);
            doctorRepository.save(doctor);
            credencialesRepository.crearCredenciales(doctor.getId_doctor(),doctor.getCorreo(),doctor.getNombre());
            CorreoService correoService = new CorreoService();
            correoService.props(doctor.getCorreo(),doctor.getNombre());
            attr.addFlashAttribute("msgDoc","Doctor creado exitosamente");
            return "redirect:/administrador/dashboard";
        }
    }
    @GetMapping("/calendario")
    public String calendario(){return "administrador/calendario";}
    @GetMapping("/mensajeria")
    public String mensajeria(){
        //En onstruccion
        return "administrador/mensajeria";}
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
            //agregue desde aca
            if(imagenComoBytes==null){
                try {
                    File foto = new File("src/main/resources/static/assets/img/userPorDefecto.jpg");
                    FileInputStream input = new FileInputStream(foto);
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = input.read(buffer)) !=-1){
                        output.write(buffer,0,length);
                    }
                    input.close();;
                    output.close();
                    byte[] bytes = output.toByteArray();
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setContentType(
                            MediaType.parseMediaType("image/jpg"));

                    return new ResponseEntity<>(
                            bytes,
                            httpHeaders,
                            HttpStatus.OK);

                }catch (IOException e){
                    e.printStackTrace();
                }
            }else {

            } //agregue hasta aca

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
            //agregue desde aca
            if(imagenComoBytes==null){
                try {
                    File foto = new File("src/main/resources/static/assets/img/userPorDefecto.jpg");
                    FileInputStream input = new FileInputStream(foto);
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = input.read(buffer)) !=-1){
                        output.write(buffer,0,length);
                    }
                    input.close();;
                    output.close();
                    byte[] bytes = output.toByteArray();
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setContentType(
                            MediaType.parseMediaType("image/jpg"));

                    return new ResponseEntity<>(
                            bytes,
                            httpHeaders,
                            HttpStatus.OK);

                }catch (IOException e){
                    e.printStackTrace();
                }
            }else {

            } //agregue hasta aca

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
