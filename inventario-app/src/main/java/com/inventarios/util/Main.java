package com.inventarios.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        String user = "postgres";
        String password = "1234567890";
        String server = "sam-app-rdsinstance-yzmysskmudjz.ctcosak24j76.ap-southeast-2.rds.amazonaws.com";
        String port = "5432";
        String database = "basededatos";

        // Configuración de HikariCP
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.setUsername(user);
        config.setPassword(password);
        config.addDataSourceProperty("serverName", server);
        config.addDataSourceProperty("portNumber", port);
        config.addDataSourceProperty("databaseName", database);

        int waitForConnectionMillis = 3000;
        int tooSlowQuerySeconds = 5;
        config.setAutoCommit(false);
        config.setConnectionTimeout(waitForConnectionMillis);
        config.setValidationTimeout(waitForConnectionMillis - 1000);
        config.addDataSourceProperty("loginTimeout", tooSlowQuerySeconds);
        config.addDataSourceProperty("socketTimeout", tooSlowQuerySeconds);
        // config.setReadOnly(readOnly); -- Puedes añadir esta línea si es necesario
        config.setIsolateInternalQueries(true);
        config.setConnectionInitSql("select 1");
        config.setConnectionTestQuery("select 1");
        config.setTransactionIsolation("TRANSACTION_SERIALIZABLE");

        // Crear el DataSource de Hikari
        DataSource dataSource = new HikariDataSource(config);

        // Ejemplo de uso del DataSource para obtener una conexión
        try (Connection connection = dataSource.getConnection()) {
            // Aquí puedes utilizar la conexión para realizar operaciones en la base de datos
            System.out.println("Conexión exitosa");
        } catch (SQLException e) {
            System.err.println("Error en la conexión: " + e.getMessage());
        }
    }
}
