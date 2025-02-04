package com.inventarios.handler.tipos.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.core.DatabaseOperationException;
import com.inventarios.handler.tipos.response.TipoResponseRest;
import com.inventarios.model.Tipo;
import java.util.*;
import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public abstract class InventariosBusquedaTipoAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final org.jooq.Table<?> TIPO_TABLE = DSL.table("tipo");
  protected static final org.jooq.Field<String> TIPO_TABLE_COLUMNA = DSL.field("nombretipo", String.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }
  protected abstract Result<Record> busquedaPorNombreTipo(String argv) throws DatabaseOperationException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    LambdaLogger logger = context.getLogger();
    TipoResponseRest responseRest = new TipoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    logger.log("buscar: " + idString);
    String output = "";
    try {
      Result<Record> result = busquedaPorNombreTipo(idString);
      responseRest.getTipoResponse().setListatipos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Tipos encontrados");
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

  protected List<Tipo> convertResultToList(Result<Record> result) {
    List<Tipo> listaTipos = new ArrayList<>();
    for (Record record : result) {
      Tipo tipo = new Tipo();
      tipo.setId(record.getValue("id", Long.class));
      tipo.setNombretipo(record.getValue("nombretipo", String.class));
      tipo.setDescriptipo(record.getValue("descriptipo", String.class));
      listaTipos.add(tipo);
    }
    return listaTipos;
  }
}
