package com.example.proyectogticsgrupo2.web_service.DniService;

import com.example.proyectogticsgrupo2.entity.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataDto {
    private String success;
    private Data data;
    private String source;
}
