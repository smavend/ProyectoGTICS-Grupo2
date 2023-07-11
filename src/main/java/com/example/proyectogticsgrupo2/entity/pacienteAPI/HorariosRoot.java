package com.example.proyectogticsgrupo2.entity.pacienteAPI;

import com.example.proyectogticsgrupo2.entity.Horario;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HorariosRoot {
    public int doctor;
    public List<Horarios> horarios;
    public String resultado;
}
