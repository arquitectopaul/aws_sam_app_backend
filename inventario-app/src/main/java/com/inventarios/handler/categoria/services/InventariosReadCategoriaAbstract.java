package com.inventarios.handler.categoria.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.core.DatabaseOperationException;
import java.util.*;
import com.inventarios.model.Categoria;
import com.inventarios.handler.categoria.response.CategoriaResponseRest;
import com.inventarios.util.GsonFactory;
import org.jooq.Table;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

/*Para implementar mayor seguridad se puede hacer   extends AuthorizerKeycloakAbstractHandler
* en lugar de implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyRespons.
* Heredar sus métodos y copiar las implementaciones de sus métodos para leer el token.*/
public abstract class InventariosReadCategoriaAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  protected final static Table<Record> GRUPO_TABLE = DSL.table("categoria");
  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }
  protected abstract Result<Record> read() throws DatabaseOperationException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    CategoriaResponseRest responseRest = new CategoriaResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
      .withHeaders(headers);
    String output ="";
    try {
      Result<Record> result = read();
      responseRest.getCategoriaResponse().setListacategorias(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Grupos encontrados");
      output = GsonFactory.createGson().toJson(responseRest);
      return response.withStatusCode(200).withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al consultar");
      return response
        .withBody(e.toString())
        .withStatusCode(500);
    }
  }

  protected List<Categoria> convertResultToList(Result<Record> result) {
    List<Categoria> listaCategorias = new ArrayList<>();
    for (Record record : result) {
      Categoria categoria = new Categoria();
      categoria.setId(record.getValue("id", Long.class));
      categoria.setNombregrupo(record.getValue("nombregrupo", String.class));
      categoria.setDescripgrupo(record.getValue("descripgrupo", String.class));
      listaCategorias.add(categoria);
    }
    return listaCategorias;
  }

}