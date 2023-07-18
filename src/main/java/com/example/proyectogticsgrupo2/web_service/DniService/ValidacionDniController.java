package com.example.proyectogticsgrupo2.web_service.DniService;

import com.example.proyectogticsgrupo2.entity.Paciente;
import com.example.proyectogticsgrupo2.entity.Temporal;
import com.example.proyectogticsgrupo2.repository.PacienteRepository;
import com.example.proyectogticsgrupo2.repository.TemporalRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
public class ValidacionDniController {
    final PacienteRepository pacienteRepository;
    final TemporalRepository temporalRepository;

    public ValidacionDniController(PacienteRepository pacienteRepository, TemporalRepository temporalRepository) {
        this.pacienteRepository = pacienteRepository;
        this.temporalRepository = temporalRepository;
    }

    @GetMapping({"/validarDni/{dni}","/signin/validarDni/{dni}"})
    public HashMap<String, Object> validacion(@PathVariable("dni") String dni){
        List<Paciente> pacientes = pacienteRepository.findAll();
        boolean existDni = false;
        for (Paciente p : pacientes) {
            if (p.getIdPaciente().equals(dni)) {
                existDni = true;
                break;
            }
        }
        List<Temporal> temp = temporalRepository.findAll();
        for (Temporal t : temp) {
            if (t.getDni().equals(dni) && t.getLlenado() == 1) {
                existDni = true;
                break;
            }
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        if (dni.length()!=8){
            hashMap.put("err","Debe ingresar un número de 8 dígitos");
        }else if(existDni){
            hashMap.put("err","El DNI ya está registrado");
        }else {
            hashMap.put("msg","Éxito");
        }
        return hashMap;
    }
    @GetMapping({"/validarDniAdmin/{dni}","/signin/validarDni/{dni}"})
    public HashMap<String, Object> validacion1(@PathVariable("dni") String dni){
        List<Paciente> pacientes = pacienteRepository.findAll();
        boolean existDni = false;
        for (Paciente p : pacientes) {
            if (p.getIdPaciente().equals(dni)) {
                existDni = true;
                break;
            }
        }
        List<Temporal> temp = temporalRepository.findAll();
        for (Temporal t : temp) {
            if (t.getDni().equals(dni)) {
                existDni = true;
                break;
            }
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        if (dni.length()!=8){
            hashMap.put("err","Debe ingresar un número de 8 dígitos");
        }else if(existDni){
            hashMap.put("err","El DNI ya está registrado");
        }else {
            hashMap.put("msg","Éxito");
        }
        return hashMap;
    }
}
