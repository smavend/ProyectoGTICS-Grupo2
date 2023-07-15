package com.example.proyectogticsgrupo2.web_service.paciente;

import com.example.proyectogticsgrupo2.entity.pacienteAPI.HorariosRoot;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
@Component
public class HorariosDao {
    public HorariosRoot listarHorarios(String idDoctor, String fecha, HttpServletRequest request){
        String host = request.getScheme()+"://"+request.getServerName()+":"+request.getLocalPort();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HorariosRoot> response = restTemplate.getForEntity(host+"/Paciente/api/horarios/consulta/"+idDoctor+"/"+fecha, HorariosRoot.class);
        return response.getBody();
    }
}
