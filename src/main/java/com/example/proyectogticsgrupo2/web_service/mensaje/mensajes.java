package com.example.proyectogticsgrupo2.web_service.mensaje;

import com.example.proyectogticsgrupo2.entity.Mensaje;
import com.example.proyectogticsgrupo2.repository.MensajeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@CrossOrigin

public class mensajes {
    final MensajeRepository mensajeRepository;

    public mensajes(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }

    @PostMapping("/guardarMens") // Actualiza la ruta si es necesario
    public ResponseEntity<HashMap<String,Object>> guardarMensaje(@RequestBody Mensaje mensaje) {
        HashMap<String, Object> hashMap = new HashMap<>();
        mensajeRepository.save(mensaje);
        hashMap.put("mensje","ok");
        return ResponseEntity.status(HttpStatus.CREATED).body(hashMap);
    }

}
