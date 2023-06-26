package com.example.proyectogticsgrupo2.service;

import com.example.proyectogticsgrupo2.entity.Cita;
import com.example.proyectogticsgrupo2.repository.CitaRepository;
import com.example.proyectogticsgrupo2.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class DocStorageService {
    @Autowired
    private CitaRepository citaRepository;

    public Cita saveFile(MultipartFile file){
        String docname=file.getOriginalFilename();
        try {
            Cita cita = new Cita();
            return citaRepository.save(cita);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public Optional<Cita> getFile(Integer citaId){
        return citaRepository.findById(citaId);
    }
    public List<Cita> getFiles(){
        return citaRepository.findAll();
    }
}
