package com.inventarios.handler.especificaciones.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.handler.especificaciones.response.EspecificacionesResponseRest;
import com.inventarios.model.Especificaciones;
import com.inventarios.core.DatabaseOperationException;
import java.util.*;
import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public abstract class InventariosBusquedaEspecificacionesAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final org.jooq.Table<?> ESPECIFICACIONES_TABLE = DSL.table("especificaciones");
  protected static final org.jooq.Field<String> ESPECIFICACIONES_TABLE_COLUMNA = DSL.field("nombreespecificaciones", String.class);
  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }
  protected abstract Result<Record> busquedaPorNombreespecificaciones(String argv) throws DatabaseOperationException;
  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    EspecificacionesResponseRest responseRest = new EspecificacionesResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    context.getLogger().log("id from path: " + idString);
    String output = "";
    try {
      Result<Record> result = busquedaPorNombreespecificaciones(idString);
      responseRest.getEspecificacionesResponse().setListaespecificaciones(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "especificaciones encontradas");
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

  protected List<Especificaciones> convertResultToList(Result<Record> result) {
    List<Especificaciones> listaespecificacioness = new ArrayList<>();
    for (Record record : result) {
      Especificaciones especificaciones = new Especificaciones();
      especificaciones.setId(record.getValue("id", Long.class));
      //especificaciones.setEspecifico(record.getValue("nombreespecifico", Especifico.class));
      especificaciones.setNombreatributo(record.getValue("nombreatributo", String.class));
      especificaciones.setDescripcionatributo(record.getValue("descripcionatributo", String.class));
      listaespecificacioness.add(especificaciones);
    }
    return listaespecificacioness;
  }
}



    /*
    String body = input.getBody();
    context.getLogger().log("body " + body);
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        especificaciones especificaciones = GsonFactory.createGson().fromJson(body, especificaciones.class);
        if (especificaciones != null) {
          context.getLogger().log("especificaciones.getId() = " + especificaciones.getId());
          if (id.equals(especificaciones.getId())) {
            busquedaPorId(especificaciones.getId());
            list.add(especificaciones);
            responseRest.getespecificacionesResponse().setListaespecificacioness(list);
            responseRest.setMetadata("Respuesta ok", "00", "especificaciones actualizado");
          } else {
            return response
                    .withBody("Id no coincide con el id del body")
                    .withStatusCode(400);
          }
        }
        output = GsonFactory.createGson().toJson(responseRest);
      }
      return response.withStatusCode(200)
              .withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al eliminar");
      return response
        .withBody(e.toString())
        .withStatusCode(500);
    }*/

  /*private String convertResultToJson(Result<Record> result) {
    List<Map<String, Object>> resultList = new ArrayList<>();
    for (Record record : result) {
      Map<String, Object> recordMap = new HashMap<>();
      for (Field<?> field : result.fields()) {
        recordMap.put(field.getName(), record.get(field));
      }
      resultList.add(recordMap);
    }
    return new Gson().toJson(resultList);
  }*/