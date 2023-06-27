package com.example.proyectogticsgrupo2.web_service.DniService;

import com.example.proyectogticsgrupo2.entity.Data;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Component
public class DataDao {
    public Data getByDni(String dni) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://apiperu.dev/api/dni/" + dni;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(this.obtenerTokenDeAutenticacion());

        RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url), Void.class);
        ResponseEntity<DataDto> responseEntity = restTemplate.exchange(requestEntity, DataDto.class);

        DataDto dataDto = responseEntity.getBody();

        return dataDto.getData();
    }
    private String obtenerTokenDeAutenticacion() {
        return "5fe50f076dafad000ed90cd7d442fdd13381fe61cef4bddfb583614504a6b6de";
    }
}
