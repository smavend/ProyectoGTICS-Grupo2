package com.example.proyectogticsgrupo2.web_service.general;

import com.example.proyectogticsgrupo2.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(method = RequestMethod.GET, value = "/General/api")
public class GeneralAPI {

    @Autowired
    PacienteRepository pacienteRepository;

    @GetMapping("/uid/{correo}")
    public ResponseEntity<HashMap<String, Object>> buscarId(@PathVariable("correo") String correo){
        HashMap<String, Object> response = new HashMap<>();
        return ResponseEntity.ok().body(response);
    }
}
