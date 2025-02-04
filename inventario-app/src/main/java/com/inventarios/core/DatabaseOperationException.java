package com.inventarios.core;

import java.sql.SQLException;

/**
 * Excepción personalizada para manejar errores de operación en la base de datos.
 * Se asegura de cerrar las conexiones activas al ser lanzada.
 */
public class DatabaseOperationException extends Exception {

    /**
     * Crea una nueva instancia de DatabaseOperationException con un mensaje y una causa.
     *
     * @param message El mensaje de error.
     * @param cause La excepción SQL original que causó el error.
     */
    public DatabaseOperationException(String message, SQLException cause) {
        super(message, cause);
        terminateConnections();
    }

    /**
     * Crea una nueva instancia de DatabaseOperationException con una causa.
     *
     * @param cause La excepción SQL original que causó el error.
     */
    public DatabaseOperationException(SQLException cause) {
        super("Error en la operación de la base de datos", cause);
        terminateConnections();
    }

    /**
     * Método privado para cerrar las conexiones activas.
     * Este método se llama cuando se lanza la excepción.
     */
    private void terminateConnections() {
        RDSConexion.terminateActiveConnections();
    }
}