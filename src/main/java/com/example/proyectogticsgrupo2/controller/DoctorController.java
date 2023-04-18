package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.entity.Doctor;
import com.example.proyectogticsgrupo2.entity.Paciente;
import com.example.proyectogticsgrupo2.repository.DoctorRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
@Controller
public class DoctorController {
    final DoctorRepository doctorRepository;

    public DoctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/doctor/foto/{id}")
    public void showPacienteImage(@PathVariable String id,
                                  HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");

        Optional<Doctor> optPaciente = doctorRepository.findById(id);
        if(optPaciente.isPresent()){
            Doctor doctor = optPaciente.get();
            InputStream is = new ByteArrayInputStream(doctor.getFoto());
            IOUtils.copy(is, response.getOutputStream());
        }
    }
}
