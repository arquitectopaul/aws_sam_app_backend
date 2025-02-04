package com.inventarios.handler.articulos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.model.Articulo;
import com.inventarios.core.DatabaseOperationException;
import java.util.HashMap;
import java.util.Map;
import com.inventarios.handler.articulos.response.ArticuloResponseRest;
import com.inventarios.util.GsonFactory;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

public abstract class InventariosCreateArticuloAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> ARTICULO_TABLE = DSL.table("articulo");
  final static Map<String, String> headers = new HashMap<>();
  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "POST");
  }
  protected abstract void save(String nombrearticulo, String descriparticulo) throws DatabaseOperationException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    ArticuloResponseRest responseRest = new ArticuloResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);
    String body = input.getBody();
    context.getLogger().log("body " + body);
    String output ="";
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Articulo articulo = GsonFactory.createGson().fromJson(body, Articulo.class);
        if (articulo != null) {
          /*
          * Could not initialize class com.inventarios.core.RDSConexion: java.lang.NoClassDefFoundError
java.lang.NoClassDefFoundError: Could not initialize class com.inventarios.core.RDSConexion
	at com.inventarios.handler.articulos.CreateArticuloHandler.save(CreateArticuloHandler.java:10)
	at com.inventarios.handler.articulos.services.CreateArticuloAbstractHandler.handleRequest(CreateArticuloAbstractHandler.java:45)
	at com.inventarios.handler.articulos.services.CreateArticuloAbstractHandler.handleRequest(CreateArticuloAbstractHandler.java:18)
Caused by: java.lang.ExceptionInInitializerError: Exception com.zaxxer.hikari.pool.HikariPool$PoolInitializationException: Failed to initialize pool: FATAL: remaining connection slots are reserved for roles with the SUPERUSER attribute [in thread "main"]
	at com.zaxxer.hikari.pool.HikariPool.throwPoolInitializationException(HikariPool.java:584)
	at com.zaxxer.hikari.pool.HikariPool.checkFailFast(HikariPool.java:571)
	at com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:98)
	at com.zaxxer.hikari.HikariDataSource.<init>(HikariDataSource.java:80)
	at com.inventarios.core.RDSConexion.initDataSource(RDSConexion.java:34)
	at com.inventarios.core.RDSConexion.<clinit>(RDSConexion.java:19)
	* */
          save(articulo.getNombrearticulo().toUpperCase(), articulo.getDescriparticulo().toUpperCase());
          responseRest.setMetadata("Respuesta ok", "00", "Articulo guardado");
        }
        output = GsonFactory.createGson().toJson(responseRest);
      }
      return response.withStatusCode(200)
              .withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al guardar");
      return response
        .withBody(e.toString())
        .withStatusCode(500);
    }
  }
}