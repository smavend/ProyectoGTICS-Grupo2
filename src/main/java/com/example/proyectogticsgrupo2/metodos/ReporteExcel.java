package com.example.proyectogticsgrupo2.metodos;

import com.example.proyectogticsgrupo2.dto.AdministradorIngresos;
import com.example.proyectogticsgrupo2.repository.AdministradorRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.util.List;

public class ReporteExcel {


    public ResponseEntity<Resource> generarInformeIngresosExcel(List<AdministradorIngresos> ingresos, String nombreDoc) {
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
        // Crear un recurso de tipo ByteArrayResource con los bytes del archivo
        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        // Configurar las cabeceras de la respuesta HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreDoc + ".xlsx\"");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // Devolver la respuesta con el archivo adjunto y las cabeceras
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);

    }

    public ResponseEntity<Resource> generateIncomeReportPDF(List<AdministradorIngresos> incomes, String nombreDoc) {
        Document document = new Document();

        // Crear un flujo de bytes en memoria para el archivo PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            // Crear el escritor PDF y asociarlo con el flujo de bytes
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            // Abrir el documento
            document.open();

            // Agregar el encabezado del informe
            Paragraph header = new Paragraph("Informe de ingresos\n");
            document.add(header);

            // Crear la tabla de ingresos
            PdfPTable table = new PdfPTable(7); // Número de columnas

            // Agregar las cabeceras de columna
            table.addCell("Fecha");
            table.addCell("Monto");
            table.addCell("Concepto");
            table.addCell("Paciente");
            table.addCell("Tipo de Pago");
            table.addCell("Especialidad");
            table.addCell("Seguro");

            // Agregar los datos de la tabla de ingresos
            for (AdministradorIngresos income : incomes) {
                PdfPCell fechacanceladaCell = new PdfPCell(new Paragraph(income.getFechacancelada().toString()));
                PdfPCell preciocitaCell = new PdfPCell(new Paragraph(String.valueOf(income.getPreciocita())));
                table.addCell(fechacanceladaCell);
                table.addCell(preciocitaCell);
                table.addCell(income.getConcepto());
                table.addCell(income.getNombreuser());
                table.addCell(income.getTipopago());
                table.addCell(income.getEspecialidadcita());
                table.addCell(income.getNombreseguro());
            }

            // Agregar la tabla al documento
            document.add(table);

            // Cerrar el documento
            document.close();

            // Configurar las cabeceras de la respuesta HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreDoc + ".pdf\"");
            headers.setContentType(MediaType.APPLICATION_PDF);

            // Crear un recurso de tipo ByteArrayResource con los bytes del archivo PDF
            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

            // Devolver la respuesta con el archivo adjunto y las cabeceras
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


}
