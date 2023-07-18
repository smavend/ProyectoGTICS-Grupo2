package com.example.proyectogticsgrupo2.metodos;

import com.example.proyectogticsgrupo2.dto.AdministradorEgresos;
import com.example.proyectogticsgrupo2.dto.AdministradorIngresos;
import com.example.proyectogticsgrupo2.repository.AdministradorRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
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

        // Crear el encabezado de la hoja
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Fecha Cancelada");
        headerRow.createCell(1).setCellValue("Nombre Usuario");
        headerRow.createCell(2).setCellValue("Especialidad Cita");
        headerRow.createCell(3).setCellValue("Concepto");
        headerRow.createCell(4).setCellValue("Nombre Seguro");
        headerRow.createCell(5).setCellValue("Precio Cita (Soles)");
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
            // Configurar orientación de página horizontal
            document.setPageSize(PageSize.A4.rotate());

            // Crear el escritor PDF y asociarlo con el flujo de bytes
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            // Abrir el documento
            document.open();

            // Crear la tabla de ingresos
            PdfPTable table = new PdfPTable(7); // Número de columnas

            // Crear el encabezado del informe con formato atractivo
            PdfPCell headerCell = new PdfPCell();
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell.addElement(new Paragraph("Informe de ingresos", FontFactory.getFont(FontFactory.HELVETICA, 18, BaseColor.BLUE)));
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(10);
            headerCell.setColspan(7);
            table.addCell(headerCell);

            // Agregar las cabeceras de columna
            table.addCell("Fecha");
            table.addCell("Monto\n(Soles)");
            table.addCell("Concepto");
            table.addCell("Paciente");
            table.addCell("Tipo de Pago");
            table.addCell("Especialidad");
            table.addCell("Seguro");

            // Agregar los datos de la tabla de ingresos
            for (AdministradorIngresos income : incomes) {
                PdfPCell fechacanceladaCell = new PdfPCell(new Paragraph(income.getFechacancelada().toString(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
                PdfPCell preciocitaCell = new PdfPCell(new Paragraph(String.valueOf(income.getPreciocita()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
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

    public ResponseEntity<Resource> generarEgresosExcel(List<AdministradorEgresos> ingresos, String nombreDoc) {
        // Crear un nuevo libro de Excel
        Workbook workbook = new XSSFWorkbook();
        // Crear una hoja de Excel
        Sheet sheet = workbook.createSheet("Egresos");
        // Crear estilos para las celdas
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd/MM/yyyy"));

        CellStyle currencyStyle = workbook.createCellStyle();

        // Crear el encabezado de la hoja
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Concepto");
        headerRow.createCell(1).setCellValue("Nombre Usuario");
        headerRow.createCell(2).setCellValue("Monto (Soles)");
        headerRow.createCell(3).setCellValue("Especialidad ");
        headerRow.createCell(4).setCellValue("Nombre Seguro");
        headerRow.createCell(5).setCellValue("Fecha");
        headerRow.createCell(6).setCellValue("Categoria de Pago");
        // Aplicar estilos a las celdas del encabezado
        for (int i = 0; i < 7; i++) {
            Cell headerCell = headerRow.getCell(i);
            headerCell.setCellStyle(headerStyle);}
        // Llenar los datos de ingresos en la hoja
        int rowNum = 1;
        for (AdministradorEgresos ingreso : ingresos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(ingreso.getConcepto());
            row.createCell(1).setCellValue(ingreso.getNombreuser());
            row.createCell(2).setCellValue(ingreso.getPagodoctor());
            row.createCell(3).setCellValue(ingreso.getEspecialidadcita());
            row.createCell(4).setCellValue(ingreso.getNombreseguro());
            row.createCell(5).setCellValue(ingreso.getFecha());
            row.createCell(6).setCellValue(ingreso.getCategoriagasto());
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

    public ResponseEntity<Resource> generarEgresosPDF(List<AdministradorEgresos> incomes, String nombreDoc) {
        Document document = new Document();

        // Crear un flujo de bytes en memoria para el archivo PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            // Configurar orientación de página horizontal
            document.setPageSize(PageSize.A4.rotate());

            // Crear el escritor PDF y asociarlo con el flujo de bytes
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            // Abrir el documento
            document.open();

            // Crear la tabla de ingresos
            PdfPTable table = new PdfPTable(7); // Número de columnas

            // Crear el encabezado del informe con formato atractivo
            PdfPCell headerCell = new PdfPCell();
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell.addElement(new Paragraph("Informe de ingresos", FontFactory.getFont(FontFactory.HELVETICA, 18, BaseColor.BLUE)));
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(10);
            headerCell.setColspan(7);
            table.addCell(headerCell);

            // Agregar las cabeceras de columna
            table.addCell("Concepto");
            table.addCell("Monto\n(Soles)");
            table.addCell("Nombre");
            table.addCell("Seguro");
            table.addCell("Especialidad");
            table.addCell("Fecha");
            table.addCell("Categoria de Pago");


            // Agregar los datos de la tabla de ingresos
            for (AdministradorEgresos income : incomes) {
                PdfPCell fechacanceladaCell = new PdfPCell(new Paragraph(income.getFecha().toString(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
                PdfPCell preciocitaCell = new PdfPCell(new Paragraph(String.valueOf(income.getPagodoctor()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(income.getConcepto());
                table.addCell(preciocitaCell);
                table.addCell(income.getNombreuser());
                table.addCell(income.getEspecialidadcita());
                table.addCell(income.getNombreseguro());
                table.addCell(fechacanceladaCell);
                table.addCell(income.getCategoriagasto());
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
