package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.repository.*;
import jakarta.validation.Valid;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    @GetMapping("/imageSede")
    public ResponseEntity<byte[]> mostrarImagenSede(@RequestParam("idSede") int idSede) {
        Optional<Sede> optionalSede = sedeRepository.findById(idSede);

        if (optionalSede.isPresent()) {
            Sede sede = optionalSede.get();
            byte[] imagenComoBytes = sede.getFoto();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(
                    MediaType.parseMediaType(sede.getFotocontenttype()));
            return new ResponseEntity<>(
                    imagenComoBytes,
                    httpHeaders,
                    HttpStatus.OK);
        } else {
            return null;
        }
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

    @PostMapping("/confirmarReserva")
    public String confirmarReserva(@RequestParam("idPaciente") int idPaciente,
                                   @RequestParam("idDoctor") String idDoctor,
                                   @RequestParam("modalidad") int modalidad) {
        int idSede = doctorRepository.buscarIdSedeDoctor(idDoctor);
        citaRepository.reservarCita(idPaciente,idDoctor,modalidad,idSede);
        return "redirect:/Paciente/confirmacion";
    }

    /* PERFIL */
    @GetMapping("/perfil")
    public String perfil(Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            List<Alergia> alergiasPaciente = alergiaRepository.buscarPorPacienteId(idPrueba);
            List<Cita> historialCitas = citaRepository.buscarHistorialDeCitas(idPrueba);
            model.addAttribute("alergiasPaciente", alergiasPaciente);
            model.addAttribute("paciente", paciente);
            model.addAttribute("historialCitas", historialCitas);
        }
        return "paciente/perfil";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfil(@ModelAttribute("paciente") Paciente paciente,
                               @RequestParam(name = "idPaciente") String idPaciente,
                               Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPaciente);
        if (optionalPaciente.isPresent()){
            paciente = optionalPaciente.get();
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

    @PostMapping("/perfil/guardar")
    public String guardarPerfil(@ModelAttribute("paciente") @Valid Paciente paciente,
                                BindingResult bindingResult,
                                @RequestParam(name = "archivo") MultipartFile file,
                                RedirectAttributes attr,
                                Model model){

        String fileName = file.getOriginalFilename();

        if (fileName.contains("..") || fileName.contains(" ")){
            attr.addFlashAttribute("msgError", "El archivo contiene caracteres inválidos");
            return "redirect:/Paciente/perfil/editar?idPaciente="+paciente.getIdPaciente();
        }

        if (bindingResult.hasErrors()){
            model.addAttribute("seguroList", seguroRepository.findAll());
            model.addAttribute("alergiasPaciente", alergiaRepository.buscarPorPacienteId(paciente.getIdPaciente()));
            model.addAttribute("distritoList", distritoRepository.findAll());
            return "paciente/perfilEditar";
        }
        else{
            try {
                if (file.isEmpty()){
                    Optional<Paciente> optionalPaciente = pacienteRepository.findById(paciente.getIdPaciente());
                    Paciente p = optionalPaciente.get();
                    paciente.setFoto(p.getFoto());
                    paciente.setFotoname(p.getFotoname());
                    paciente.setFotocontenttype(p.getFotocontenttype());
                }
                else {
                    paciente.setFoto(file.getBytes());
                    paciente.setFotoname(fileName);
                    paciente.setFotocontenttype(file.getContentType());
                }
                pacienteRepository.save(paciente);
                attr.addFlashAttribute("msgActualizacion", "Perfil actualizado correctamente");
                return "redirect:/Paciente/perfil";

            }
            catch (IOException e){
                e.printStackTrace();
                attr.addFlashAttribute("msgError", "Ocurrió un error al subir el archivo");
                // MENSAJE EN DE ERROR EN CASO HAYA ERROR AL SUBIR ARCHIVO
                return "redirect:/Paciente/perfil";
            }

        }
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

    @GetMapping("/perfil/quitarFoto")
    public String quitarFoto(@RequestParam(name = "id") String idPaciente){

        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPaciente);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            try{
                File foto = new File("src/main/resources/static/assets/img/userPorDefecto.jpg");
                FileInputStream input = new FileInputStream(foto);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) !=-1){
                    output.write(buffer,0,length);
                }
                input.close();
                output.close();
                byte[] bytes = output.toByteArray();

                paciente.setFoto(bytes);
                paciente.setFotoname("userPorDefecto.jpg");
                paciente.setFotocontenttype("image/jpg");

                pacienteRepository.save(paciente);
                return "redirect:/Paciente/perfil";
            }
            catch (IOException e){
                e.printStackTrace();
                // MENSAJE EN DE ERROR EN CASO HAYA ERROR AL SUBIR ARCHIVO
                return "redirect:/Paciente/perfil";
            }
        }

        return "redirect:/Paciente/perfil";
    }

    @GetMapping("/imagePaciente")
    public ResponseEntity<byte[]> mostrarImagenPaciente(@RequestParam("idPaciente") String idPaciente) {
        Optional<Paciente> optionalPaciente= pacienteRepository.findById(idPaciente);

        if (optionalPaciente.isPresent()) {
            Paciente paciente = optionalPaciente.get();
            byte[] imagenComoBytes = paciente.getFoto();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(
                    MediaType.parseMediaType(paciente.getFotocontenttype()));
            return new ResponseEntity<>(
                    imagenComoBytes,
                    httpHeaders,
                    HttpStatus.OK);
        } else {
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

    @PostMapping("/doctoresFiltrado")
    public String verDoctores(@RequestParam("idSedeFilter") int idSedeFilter,
                              @RequestParam("idEspecialidadFilter") int idEspecialidadFilter,
                              Model model){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPrueba);
        if (optionalPaciente.isPresent()){
            Paciente paciente = optionalPaciente.get();
            model.addAttribute("paciente", paciente);
        }
        List<Sede> sedeList =  sedeRepository.findAll();
        List<Especialidad> especialidadList = especialidadRepository.findAll();
        Optional<Sede> optionalSede = sedeRepository.findById(idSedeFilter);
        if (optionalSede.isPresent()){
            Sede sede = optionalSede.get();
            model.addAttribute("sede", sede);
        }
        if (idEspecialidadFilter==0){
            List<Doctor> doctorList = doctorRepository.listDoctorSede(idSedeFilter);
            model.addAttribute("doctorList", doctorList);
            model.addAttribute("sinFiltrar",0);
        }else {
            List<Doctor> listDoctorSedeEspecialidad = doctorRepository.listDoctorSedeEspecialidad(idSedeFilter, idEspecialidadFilter);
            model.addAttribute("doctorList", listDoctorSedeEspecialidad);
        }
        Optional<Especialidad> optionalEspecialidad = especialidadRepository.findById(idEspecialidadFilter);
        if (optionalEspecialidad.isPresent()){
            Especialidad especialidad = optionalEspecialidad.get();
            model.addAttribute("especialidad", especialidad);
            model.addAttribute("sinFiltrar",1);

        }
        model.addAttribute("sedeList", sedeList);
        model.addAttribute("especialidadList", especialidadList);
        return "paciente/doctores";
    }

    @GetMapping("/imageDoctor")
    public ResponseEntity<byte[]> mostrarImagenDoctor(@RequestParam("idDoctor") String idDoctor) {
        Optional<Doctor> optionalDoctor= doctorRepository.findById(idDoctor);

        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            byte[] imagenComoBytes = doctor.getFoto();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(
                    MediaType.parseMediaType(doctor.getFotocontenttype()));
            return new ResponseEntity<>(
                    imagenComoBytes,
                    httpHeaders,
                    HttpStatus.OK);
        } else {
            return null;
        }
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
    public String actualizarConsentimientos(@RequestParam("consentimiento1") String consentimiento1,
                                            @RequestParam("consentimiento2") String consentimiento2,
                                            @RequestParam("consentimiento3") String consentimiento3,
                                            @RequestParam("consentimiento4") String consentimiento4,
                                            @RequestParam("consentimiento5") String consentimiento5){

        System.out.println(consentimiento1);
        System.out.println(consentimiento2);

        return "redirect:/Paciente/consentimientos";
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
