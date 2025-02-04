package com.inventarios.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

public class Conversiones {
    /*public LocalDate convertirALocalDate0(String fechaCompraStr) {
        if (fechaCompraStr == null || fechaCompraStr.isEmpty()) {
            return null; // Devuelve null si la cadena de fecha es nula o vacía
        }
        try {
            LocalDate fecha = LocalDate.parse(fechaCompraStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            return fecha;
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            // Manejar el error de formato de fecha aquí si es necesario
            return null; // Devuelve null si hay un error al analizar la fecha
        }
    }*/

    public String convertirAString0(LocalDate fecha) {
        if (fecha == null) {
            return null; // Devuelve null si la fecha es nula
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fecha.format(formatter);
    }
    public LocalDate convertirALocalDate(String fechaCompraStr) {
        System.out.println("convertirALocalDate(String fechaCompraStr = " + fechaCompraStr + ")");
        if (fechaCompraStr == null || fechaCompraStr.isEmpty()) {
            return null; // Devuelve null si la cadena de fecha es nula o vacía
        }
        try {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("[M/d/yyyy][MM/dd/yyyy][MM/d/yyyy][M/dd/yyyy]")
                    .toFormatter();

            LocalDate fecha = LocalDate.parse(fechaCompraStr, formatter);
            System.out.println("LocalDate fecha a retornar = " + fecha);
            return fecha;
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            // Manejar el error de formato de fecha aquí si es necesario
            return null; // Devuelve null si hay un error al analizar la fecha
        }
    }
    public String convertirAString1(LocalDate fecha) {
        if (fecha == null) {
            return null; // Devuelve null si la fecha es nula
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fecha.format(formatter);
    }

    public LocalDate convertirALocalDate2(String fechaCompraStr) {
        System.out.println("convertirALocalDate(String fechaCompraStr = "+fechaCompraStr+")");
        if (fechaCompraStr == null || fechaCompraStr.isEmpty()) {
            return null; // Devuelve null si la cadena de fecha es nula o vacía
        }
        try {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .optionalStart()
                    .appendPattern("d/M/yyyy")
                    .optionalEnd()
                    .optionalStart()
                    .appendPattern("dd/MM/yyyy")
                    .optionalEnd()
                    .toFormatter();

            LocalDate fecha = LocalDate.parse(fechaCompraStr, formatter);
            System.out.println("LocalDate fecha = "+fecha);
            return fecha;
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            // Manejar el error de formato de fecha aquí si es necesario
            return null; // Devuelve null si hay un error al analizar la fecha
        }
    }

    public LocalDate convertirALocalDate1(String fechaCompraStr) {
        if (fechaCompraStr == null || fechaCompraStr.isEmpty()) {
            return null; // Devuelve null si la cadena de fecha es nula o vacía
        }
        try {
            // Intenta parsear con el formato dd/MM/yyyy
            LocalDate fecha = LocalDate.parse(fechaCompraStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return fecha;
        } catch (DateTimeParseException e) {
            // Si falla, intenta parsear con el formato d/M/yyyy
            try {
                LocalDate fecha = LocalDate.parse(fechaCompraStr, DateTimeFormatter.ofPattern("d/M/yyyy"));
                return fecha;
            } catch (DateTimeParseException ex) {
                ex.printStackTrace();
                // Manejar el error de formato de fecha aquí si es necesario
                return null; // Devuelve null si hay un error al analizar la fecha
            }
        }
    }
}