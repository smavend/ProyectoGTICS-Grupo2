package com.example.proyectogticsgrupo2.service;

import com.example.proyectogticsgrupo2.dto.AdministradorIngresos;
import com.example.proyectogticsgrupo2.repository.AdministradorRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
public class ReporteService {
    private final AdministradorRepository administradorRepository;

    public ReporteService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    @GetMapping(value = "/reporteExc", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> generarReporteIngresos() throws IOException {
        List<AdministradorIngresos> ingresos = administradorRepository.obtenerIgresosPorSeguro(5);

        // Crear un nuevo libro de Excel
        Workbook workbook = new XSSFWorkbook();
        // Crear una hoja de Excel
        Sheet sheet = workbook.createSheet("Ingresos");
        // Crear estilos para las celdas
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd/MM/yyyy"));

        CellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("$#,##0.00"));
        // Crear el encabezado de la hoja
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Fecha Cancelada");
        headerRow.createCell(1).setCellValue("Nombre Usuario");
        headerRow.createCell(2).setCellValue("Especialidad Cita");
        headerRow.createCell(3).setCellValue("Concepto");
        headerRow.createCell(4).setCellValue("Nombre Seguro");
        headerRow.createCell(5).setCellValue("Precio Cita");
        headerRow.createCell(6).setCellValue("Tipo Pago");
        // Aplicar estilos a las celdas del encabezado
        for (int i = 0; i < 7; i++) {
            Cell headerCell = headerRow.getCell(i);
            headerCell.setCellStyle(headerStyle);}
        // Llenar los datos de ingresos en la hoja
        int rowNum = 1;
        for (AdministradorIngresos ingreso : ingresos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(ingreso.getFechacancelada());
            row.createCell(1).setCellValue(ingreso.getNombreuser());
            row.createCell(2).setCellValue(ingreso.getEspecialidadcita());
            row.createCell(3).setCellValue(ingreso.getConcepto());
            row.createCell(4).setCellValue(ingreso.getNombreseguro());
            row.createCell(5).setCellValue(ingreso.getPreciocita());
            row.createCell(6).setCellValue(ingreso.getTipopago());
            // Aplicar estilos a las celdas de fecha y precio
            Cell dateCell = row.getCell(0);
            dateCell.setCellStyle(dateStyle);

            Cell priceCell = row.getCell(5);
            priceCell.setCellStyle(currencyStyle);}
        // Ajustar automáticamente el ancho de las columnas
        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);}
        // Crear un flujo de bytes en memoria para el archivo
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // Escribir el libro de Excel en el flujo de bytes
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {e.printStackTrace();}

        // Configurar la respuesta HTTP con el archivo Excel
        byte[] reportBytes = outputStream.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "reporte_ingresos.xlsx");

        return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
    }
}
