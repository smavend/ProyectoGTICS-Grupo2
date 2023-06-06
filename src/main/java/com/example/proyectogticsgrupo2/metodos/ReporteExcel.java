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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ReporteExcel {


    public void generarInformeIngresos(List<AdministradorIngresos> ingresos, String nombreDoc) {


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
            headerCell.setCellStyle(headerStyle);
        }

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
            priceCell.setCellStyle(currencyStyle);
        }

        // Ajustar automáticamente el ancho de las columnas
        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);
        }


        String carpetaDescargas = System.getProperty("user.home") + "/Downloads/";

        // Ruta completa del archivo en la carpeta de descargas
        String rutaArchivo = carpetaDescargas + "Reporte"+nombreDoc+".xlsx";
        // Guardar el libro de Excel en un archivo
        try (OutputStream outputStream = new FileOutputStream(rutaArchivo)) {
            workbook.write(outputStream);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void generateIncomeReport(List<AdministradorIngresos> incomes,String nombreDoc) {
        Document document = new Document();
        String carpetaDescargas = System.getProperty("user.home") + "/Downloads/";

        // Ruta completa del archivo en la carpeta de descargas
        String rutaArchivo = carpetaDescargas + "Reporte"+nombreDoc+".pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(rutaArchivo));
            document.open();

            // Agrega el encabezado del informe
            Paragraph header = new Paragraph("Informe de ingresos\n");
            document.add(header);


            // Crea la tabla de ingresos
            PdfPTable table = new PdfPTable(7); // Número de columnas

            // Agrega las cabeceras de columna
            table.addCell("Fecha");
            table.addCell("Monto");
            table.addCell("Concepto");
            table.addCell("Paciente");
            table.addCell("Tipo de Pago");
            table.addCell("Especialidad");
            table.addCell("Seguro");


            // Agrega los datos de la tabla de ingresos
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

            // Agrega la tabla al documento
            document.add(table);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
