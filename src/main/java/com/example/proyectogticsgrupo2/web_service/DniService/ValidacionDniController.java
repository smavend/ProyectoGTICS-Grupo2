package com.example.proyectogticsgrupo2.web_service.DniService;

import com.example.proyectogticsgrupo2.entity.Paciente;
import com.example.proyectogticsgrupo2.repository.PacienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
public class ValidacionDniController {
    final PacienteRepository pacienteRepository;

    public ValidacionDniController(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @PostMapping("/validarDni/{dni}")
    public HashMap<String, Object> validacion(@PathVariable("dni") String dni){
        List<Paciente> pacientes = pacienteRepository.findAll();
        boolean existDni = false;
        for (Paciente p : pacientes) {
            if (p.getIdPaciente().equals(dni)) {
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
