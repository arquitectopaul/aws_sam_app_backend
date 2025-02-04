package com.inventarios.handler.marcas.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.handler.marcas.response.MarcaResponseRest;
import com.inventarios.model.Marca;
import com.inventarios.core.DatabaseOperationException;
import java.util.*;
import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public abstract class InventariosBusquedaMarcaAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  final static Map<String, String> headers = new HashMap<>();
  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected static final org.jooq.Table<?> MARCA_TABLE = DSL.table("marca");
  protected static final org.jooq.Field<String> MARCA_TABLE_COLUMNA = DSL.field("nombremarca", String.class);
  protected abstract Result<Record> busquedaPorNombreMarca(String argv) throws DatabaseOperationException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    LambdaLogger logger = context.getLogger();
    MarcaResponseRest responseRest = new MarcaResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    logger.log("buscar: " + idString);
    String output = "";
    try {
      Result<Record> result = busquedaPorNombreMarca(idString);
      responseRest.getMarcaResponse().setListamarcas(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Marcas encontrados");
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

  protected List<Marca> convertResultToList(Result<Record> result) {
    List<Marca> listaMarcas = new ArrayList<>();
    for (Record record : result) {
      Marca marca = new Marca();
      marca.setId(record.getValue("id", Long.class));
      //marca.setDescripmarca(record.getValue("descripmarca", String.class));
      listaMarcas.add(marca);
    }
    return listaMarcas;
  }
}