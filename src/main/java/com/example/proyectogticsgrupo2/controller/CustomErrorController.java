package com.example.proyectogticsgrupo2.controller;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public String handleError(HttpServletRequest request) {
        // Obtener el código de estado del error
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (statusCode != null && statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return "error500"; // Nombre del archivo HTML de error 500
        }

        return "error"; // Página de error genérica para otros errores
    }

    public String getErrorPath() {
        return ERROR_PATH;
    }
}

