package com.inventarios.handler.parametros.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.core.DatabaseOperationException;
import com.inventarios.handler.parametros.response.ParametroResponseRest;
import com.inventarios.model.AtributosFiltro;
import com.inventarios.model.Parametro;
import java.util.*;
import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public abstract class InventariosBusquedaParametroAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  final static Map<String, String> headers = new HashMap<>();
  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected static final org.jooq.Table<?> PARAMETRO_TABLE = DSL.table("parametros");
  protected static final org.jooq.Field<String> PARAMETRO_TABLE_COLUMNA = DSL.field("nombre", String.class);
  protected abstract Result<Record> filterAtributos(AtributosFiltro filter) throws DatabaseOperationException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    LambdaLogger logger = context.getLogger();
    ParametroResponseRest responseRest = new ParametroResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    logger.log("buscar: " + idString);
    String output = "";
    try {
      AtributosFiltro filtro=new AtributosFiltro();
      Result<Record> result = filterAtributos(filtro);
      responseRest.getParametroResponse().setListaparametros(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Parametros encontrados");
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

  protected List<Parametro> convertResultToList(Result<Record> result) {
    List<Parametro> listaParametros = new ArrayList<>();
    for (Record record : result) {
      Parametro parametro = new Parametro();
      parametro.setId(record.getValue("id", Long.class));
      parametro.setNombre(record.getValue("nombre", String.class));
      parametro.setDescripcion(record.getValue("descripcion", String.class));
      listaParametros.add(parametro);
    }
    return listaParametros;
  }
}