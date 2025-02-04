package com.inventarios.handler.categoria.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.handler.categoria.response.CategoriaResponseRest;
import com.inventarios.model.Categoria;
import com.inventarios.core.DatabaseOperationException;
import java.util.*;
import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public abstract class InventariosBusquedaCategoriaAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final org.jooq.Table<?> GRUPO_TABLE = DSL.table("categoria");
  protected static final org.jooq.Field<String> GRUPO_TABLE_COLUMNA = DSL.field("nombregrupo", String.class);
  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract Result<Record> busquedaPorNombreGrupo(String argv) throws DatabaseOperationException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    LambdaLogger logger = context.getLogger();
    CategoriaResponseRest responseRest = new CategoriaResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    logger.log("buscar: " + idString);
    String output = "";
    try {
      Result<Record> result = busquedaPorNombreGrupo(idString);
      responseRest.getCategoriaResponse().setListacategorias(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Grupos encontrados");
      output = GsonFactory.createGson().toJson(responseRest);
      return response.withStatusCode(200)
                    .withBody(output);
  } catch (Exception e) {
        responseRest.setMetadata("Respuesta nok", "-1", "Error al guardar");
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