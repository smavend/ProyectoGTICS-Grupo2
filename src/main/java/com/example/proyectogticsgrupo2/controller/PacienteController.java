package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/Paciente")
public class PacienteController {

    String idPrueba = "10203010";

    final PacienteRepository pacienteRepository;
    final SedeRepository sedeRepository;
    final EspecialidadRepository especialidadRepository;
    final AlergiaRepository alergiaRepository;
    final SeguroRepository seguroRepository;
    final DistritoRepository distritoRepository;
    final DoctorRepository doctorRepository;
    final PacientePorConsentimientoRepository pacientePorConsentimientoRepository;
    final CitaRepository citaRepository;

    public PacienteController(PacienteRepository pacienteRepository, EspecialidadRepository especialidadRepository, SedeRepository sedeRepository, AlergiaRepository alergiaRepository, SeguroRepository seguroRepository, DistritoRepository distritoRepository, DoctorRepository doctorRepository, PacientePorConsentimientoRepository pacientePorConsentimientoRepository, CitaRepository citaRepository) {
        this.pacienteRepository = pacienteRepository;
        this.especialidadRepository = especialidadRepository;
        this.sedeRepository = sedeRepository;
        this.alergiaRepository = alergiaRepository;
        this.seguroRepository = seguroRepository;
        this.distritoRepository = distritoRepository;
        this.doctorRepository = doctorRepository;
        this.pacientePorConsentimientoRepository = pacientePorConsentimientoRepository;
        this.citaRepository = citaRepository;
    }

    /* INICIO */
    @GetMapping(value = {"", "/", "/index"})
    public String index(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        List<Sede> sedeList = sedeRepository.findAll();
        model.addAttribute("sedeList", sedeList);
        return "paciente/index";
    }

    /* RESERVAR CITA */
    @GetMapping("/reservar")
    public String reservarCita(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        List<Seguro> seguroList = seguroRepository.findAll();
        List<Especialidad> especialidadList = especialidadRepository.findAll();
        List<Doctor> doctorList = doctorRepository.findAll();
        model.addAttribute("especialidadList", especialidadList);
        model.addAttribute("seguroList", seguroList);
        model.addAttribute("doctorList",doctorList);
        return "paciente/reservar";
    }

    /* PERFIL */
    @GetMapping("/perfil")
    public String perfil(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            List<Alergia> alergiasPaciente = alergiaRepository.buscarPorPacienteId(idPrueba);
            model.addAttribute("alergiasPaciente", alergiasPaciente);
            model.addAttribute("paciente", paciente);
        }
        return "paciente/perfil";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil(Model model,
                               @RequestParam(name = "idPaciente") String idPaciente){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPaciente);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            List<Seguro> seguroList = seguroRepository.findAll();
            List<Alergia> alergiasPaciente = alergiaRepository.buscarPorPacienteId(idPrueba);
            List<Distrito> distritoList = distritoRepository.findAll();

            model.addAttribute("seguroList", seguroList);
            model.addAttribute("alergiasPaciente", alergiasPaciente);
            model.addAttribute("paciente", paciente);
            model.addAttribute("distritoList", distritoList);
            return "paciente/perfilEditar";
        }
        return "redirect:/Paciente/perfil";
    }

    @PostMapping("/perfil/guardarAlergia")
    public String guardarAlergia(Alergia alergia){
        alergiaRepository.save(alergia);
        return "redirect:/Paciente/perfil/editar?idPaciente="+alergia.getPaciente().getIdPaciente();
    }

    @GetMapping("/perfil/borrarAlergia")
    public String borrarAlergia(@RequestParam(name = "idPaciente") String idPaciente,
                                @RequestParam(name = "idAlergia") int idAlergia){
        Optional<Alergia> optionalAlergia = alergiaRepository.findById(idAlergia);
        if (optionalAlergia.isPresent()){
            alergiaRepository.deleteById(idAlergia);
        }
        return "redirect:/Paciente/perfil/editar?idPaciente="+idPaciente;
    }

    @PostMapping("/perfil/guardar")
    public String guardarPerfil(Paciente paciente){
        pacienteRepository.save(paciente);
        return "redirect:/Paciente/perfil";
    }

    @GetMapping("/fotoPerfil/{id}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable("id") String id){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(id);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            byte[] imagenComoBytes = paciente.getFoto();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType(paciente.getFotocontenttype()));

            return new ResponseEntity<>(imagenComoBytes, httpHeaders, HttpStatus.OK);
        }
        else {
            return null;
        }
    }

    /* SECCIÓN DOCTORES */
    @GetMapping("/doctores")
    public String verDoctores(@RequestParam("idSede") int idSede,
                                Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        List<Sede> sedeList =  sedeRepository.findAll();
        List<Especialidad> especialidadList = especialidadRepository.findAll();
        Optional<Sede> optionalSede = sedeRepository.findById(idSede);
        if (optionalSede.isPresent()){
            Sede sede = optionalSede.get();
            model.addAttribute("sede", sede);
        }
        List<Doctor> listDoctorSede = doctorRepository.listDoctorSede(idSede);
        model.addAttribute("doctorList", listDoctorSede);
        model.addAttribute("sedeList", sedeList);
        model.addAttribute("especialidadList", especialidadList);
        return "paciente/doctores";

    }

    @GetMapping("/perfilDoctor")
    public String verPerfilDoctor(Model model,
                                  @RequestParam("idDoctor") String idDoctor){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        Optional<Doctor> optionalDoctor = doctorRepository.findById(idDoctor);
        if(optionalDoctor.isPresent()){
            Doctor doctor = optionalDoctor.get();
            model.addAttribute("doctor", doctor);
        }
        return "paciente/doctorPerfil";
    }

    @GetMapping("/reservarDoctor")
    public String reservarCitaDoctor(Model model,
                                     @RequestParam("idDoctor") String idDoctor){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        Optional<Doctor> optionalDoctor = doctorRepository.findById(idDoctor);
        if(optionalDoctor.isPresent()){
            Doctor doctor = optionalDoctor.get();
            model.addAttribute("doctor",doctor);
        }
        return "paciente/reservarDoctor";
    }

    @GetMapping("/confirmacion")
    public String confirmarReserva(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/confirmacion";
    }

    @GetMapping("/sesionVirtual")
    public String sesionVirtual(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/sesionVirtual";
    }

    /* SECCIÓN CITAS */
    @GetMapping("citas")
    public String verCitas(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            List<Cita> proximasCitas = citaRepository.buscarProximasCitas(idPrueba);

            model.addAttribute("proximasCitas", proximasCitas);
            model.addAttribute("paciente", paciente);
        }
        return "paciente/citas";
    }

    /* SECCIÓN PAGOS */
    @GetMapping("/pagos")
    public String pagos(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/pagos";
    }

    @GetMapping("/recibo")
    public String verReciboPago(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/recibo";
    }

    /* SECCIÓN CUESTIONARIOS */
    @GetMapping("/cuestionarios")
    public String cuestionarios(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/cuestionarios";
    }

    @GetMapping("/completarCuestionario")
    public String completarCuestionario(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        return "paciente/completarCuestionario";
    }

    /* SECCIÓN CONSENTIMIENTOS */
    @GetMapping("/consentimientos")
    public String consentimientos(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            List<PacientePorConsentimiento> consentimientos = pacientePorConsentimientoRepository.findByIdIdPaciente(idPrueba);

            model.addAttribute("consentimientos", consentimientos);
            model.addAttribute("paciente", paciente);
        }
        return "paciente/consentimientos";
    }

    @PostMapping("/consentimientos/actualizar")
    public String actualizarConsentimientos(PacientePorConsentimiento pacientePorConsentimiento){
        return "";
    }

    /* SECCIÓN MENSAJERÍA */

    @GetMapping("/mensajeria")
    public String mensajeria(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();

            model.addAttribute("paciente", paciente);
        }
        return "paciente/mensajeria";
    }
}
