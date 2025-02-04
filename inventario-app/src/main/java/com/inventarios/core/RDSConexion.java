  package com.inventarios.core;

  import com.zaxxer.hikari.HikariConfig;
  import com.zaxxer.hikari.HikariDataSource;
  import org.jooq.DSLContext;
  import org.jooq.SQLDialect;
  import org.jooq.impl.DSL;
  import java.sql.Connection;
  import java.sql.PreparedStatement;
  import java.sql.SQLException;

  public class RDSConexion {

    public static final String DATABASE_NAME_ENV = "DBName";
    public static final String POSTGRES_SECRET_ARN_ENV = "RDSSecretArn";
    public static final String DB_ENDPOINT = "DBEnpoint";
    public static final String DB_USER = "user";
    public static final String DB_PASS = "pass";

    private static HikariDataSource dataSource;

    /*static {
      initDataSource();
    }*/

    private static void initDataSource() {
      if (dataSource == null) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://" + rdsEndpoint() + "/" + rdsDatabase());
        config.setUsername(rdsUserDB());
        config.setPassword(rdsPassDB());
        config.setMaximumPoolSize(20);
        config.setUsername(rdsUserDB());
        config.setPassword(rdsPassDB());
        config.setMinimumIdle(5); // Número máximo de conexiones inactivas en el pool
        config.setIdleTimeout(30000); // Tiempo máximo de inactividad antes de cerrar una conexión
        config.setConnectionTimeout(30000); // Tiempo máximo de espera para obtener una conexión del pool
        config.setMaxLifetime(1800000);
        config.setLeakDetectionThreshold(20000); // Detectar conexiones que están abiertas demasiado tiempo (20 segundos)
        config.setValidationTimeout(5000); // 5 segundos para validar conexiones antes de devolverlas al pool
        config.setPoolName("HikariPool-t3-micro");
        dataSource = new HikariDataSource(config);
        /*try {
          dataSource = new HikariDataSource(config);
        } catch (HikariPool.PoolInitializationException e) {
          // Manejar la excepción aquí, por ejemplo, registrarla y/o reintentar la conexión.
          System.err.println("Error initializing HikariDataSource: " + e.getMessage());
          throw new RuntimeException("Failed to initialize HikariDataSource", e);
        }*/
      }
    }

    public static void close() {
      if (dataSource != null) {
        dataSource.close();
      }
    }

    /*public static Connection getConnection() {
      try {
        return dataSource.getConnection();
      } catch (SQLException e) {
        throw new RuntimeException("Error al obtener la conexión a la base de datos", e);
      }
    }*/
    public static Connection getConnection() {
      if (dataSource == null) {
        initDataSource();
      }
      try {
        return dataSource.getConnection();
      } catch (SQLException e) {
        throw new RuntimeException("Error al obtener la conexión a la base de datos", e);
      }
    }

    public static String rdsDatabase() {
      return System.getenv(DATABASE_NAME_ENV);
    }
    public static String rdsSecretArn() {
      return System.getenv(POSTGRES_SECRET_ARN_ENV);
    }
    public static String rdsEndpoint() {
      return System.getenv(DB_ENDPOINT);
    }
    public static String rdsUserDB() {
      return System.getenv(DB_USER);
    }
    public static String rdsPassDB() {
      return System.getenv(DB_PASS);
    }

    /*public static DSLContext getDSL() throws SQLException {
      return DSL.using(getConnection(), SQLDialect.POSTGRES);
    }*/
    public static DSLContext getDSL() throws SQLException {
      if (dataSource == null) {
        initDataSource();
      }
      return DSL.using(dataSource.getConnection(), SQLDialect.POSTGRES);
    }

    public static void terminateActiveConnections() {
      String sql = "SELECT pg_terminate_backend(pid) " +
              "FROM pg_stat_activity " +
              "WHERE datname = ? AND pid <> pg_backend_pid();";
      System.out.println("##############################################################");
      System.out.println(sql);
      System.out.println("##############################################################");
      try (Connection conn = getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, rdsDatabase()); // Evita la concatenación de cadenas
        stmt.execute();
        System.out.println("Consultas para terminar conexiones ejecutadas correctamente.");
      } catch (SQLException e) {
        System.err.println("Error al ejecutar la consulta para terminar conexiones: " + e.getMessage());
      }
    }

    /*public static void terminateActiveConnections() {
      Connection conn = null;
      Statement stmt = null;
      try {
        conn = getConnection();
        stmt = conn.createStatement();
        String sql = "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = '" + rdsDatabase() + "' AND pid <> pg_backend_pid();";
        System.out.println(sql);
        stmt.execute(sql);
        System.out.println("Consultas para terminar conexiones ejecutadas correctamente.");
      } catch (SQLException e) {
        System.out.println("Error al ejecutar la consulta para terminar conexiones: " + e.getMessage());
      } finally {
        try {
          if (stmt != null) {
            stmt.close();
          }
        } catch (SQLException e) {
          System.out.println("Error al cerrar Statement: " + e.getMessage());
        }

        try {
          if (conn != null) {
            conn.close();
          }
        } catch (SQLException e) {
          System.out.println("Error al cerrar conexión: " + e.getMessage());
        }
      }
    }*/
  }