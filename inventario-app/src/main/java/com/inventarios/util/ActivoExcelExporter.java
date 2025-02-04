package com.inventarios.util;

import java.io.IOException;
import java.util.List;
import com.inventarios.model.Activo;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ActivoExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Activo> activo;

    public ActivoExcelExporter (List<Activo> activos) {
        this.activo = activos;
        workbook = new XSSFWorkbook();
    }
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Resultado");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();

        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "ID", style);
        createCell(row, 1, "Responsable", style);
        createCell(row, 2, "Proveedor", style);
        createCell(row, 3, "Tipo", style);
        createCell(row, 4, "Grupo", style);
        createCell(row, 5, "Artículo", style);
        createCell(row, 6, "Cod. Inventario", style);
        createCell(row, 7, "Modelo", style);
        createCell(row, 8, "Marca", style);
        createCell(row, 9, "Moneda", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if(value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if(value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
    private void writeDataLines() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for( Activo result: activo) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, String.valueOf(result.getId()), style);
            createCell(row, columnCount++, result.getCustodio(), style);
            //createCell(row, columnCount++, result.getProveedor().getRazonsocial(), style);
            createCell(row, columnCount++, result.getCustodio().getProveedores().get(0).getRazonsocial(), style);
            createCell(row, columnCount++, result.getTipo().getNombretipo(), style);
            createCell(row, columnCount++, result.getCategoria().getNombregrupo(), style);
            createCell(row, columnCount++, result.getArticulo().getNombrearticulo(), style);
            createCell(row, columnCount++, result.getCodinventario(), style);
            createCell(row, columnCount++, result.getModelo(), style);
            createCell(row, columnCount++, result.getMarca(), style);
            createCell(row, columnCount++, result.getMoneda(), style);
        }
    }
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine(); //write the header
        writeDataLines(); //write the data
        ServletOutputStream servletOutput = response.getOutputStream();
        workbook.write(servletOutput);
        workbook.close();
        servletOutput.close();
    }

}
