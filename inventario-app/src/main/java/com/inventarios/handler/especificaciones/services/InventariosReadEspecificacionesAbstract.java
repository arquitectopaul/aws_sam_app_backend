package com.inventarios.handler.especificaciones.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.core.DatabaseOperationException;
import java.util.*;
import com.google.gson.Gson;
import com.inventarios.handler.atributos.response.AtributosResponseRest;
import com.inventarios.model.Atributos;
import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;

public abstract class InventariosReadEspecificacionesAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> ATRIBUTO_TABLE = DSL.table("atributo");
  protected final static Table<Record> ATRIBUTOS_TABLE = DSL.table("atributos");
  protected final static Field<Long> ATRIBUTOS_ID = field(name("atributos", "id"), Long.class);
  protected final static Field<Long> ATRIBUTOS_ATRIBUTOID = field(name("atributos", "atributoid"), Long.class);
  protected final static Field<String> ATRIBUTOS_NOMBREATRIBUTO = field(name("atributos", "nombreatributo"), String.class);
  protected final static Field<Long> ATRIBUTO_ID = field(name("atributo", "id"), Long.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract Result<Record3<Long, Long, String>> read(long custodioId, long articuloId, long tipoId, long categoriaId) throws DatabaseOperationException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    AtributosResponseRest responseRest = new AtributosResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    LambdaLogger logger = context.getLogger();
    // Obtener los parámetros de consulta
    Map<String, String> queryParams = input.getQueryStringParameters();

    //comparar con los valores de la tabla especificaciones
    long custodioId  = queryParams.containsKey("custodioId") ? Long.parseLong(queryParams.get("custodioId")) : 0;
    long articuloId  = queryParams.containsKey("articuloId") ? Long.parseLong(queryParams.get("articuloId")) : 0;
    long tipoId      = queryParams.containsKey("tipoId") ? Long.parseLong(queryParams.get("tipoId")) : 0;
    long categoriaId = queryParams.containsKey("categoriaId") ? Long.parseLong(queryParams.get("categoriaId")) : 0;

    logger.log("custodioId: " + custodioId);
    logger.log("articuloId: " + articuloId);
    logger.log("tipoId: " + tipoId);
    logger.log("categoriaId: " + categoriaId);

    try {
      Result<Record3<Long, Long, String>> result = read(custodioId, articuloId, tipoId, categoriaId);
      responseRest.getAtributosResponse().setListaatributoss(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "especificacioness encontrados");
      Gson gson = GsonFactory.createGson();
      String output = gson.toJson(responseRest);
      logger.log(output);
      return response.withStatusCode(200)
              .withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al consultar");
      return response
              .withBody(e.toString())
              .withStatusCode(500);
    }
  }

  protected List<Atributos> convertResultToList(Result<Record3<Long, Long, String>> result) {
    List<Atributos> listaAtributos = new ArrayList<>();
    for (Record3<Long, Long, String> record : result) {
      Atributos atributos = new Atributos();
      atributos.setId(record.getValue(ATRIBUTOS_ID));
      atributos.setAtributoid(record.getValue(ATRIBUTOS_ATRIBUTOID));
      atributos.setNombreatributo(record.getValue(ATRIBUTOS_NOMBREATRIBUTO));
      listaAtributos.add(atributos);
    }
    return listaAtributos;
  }
}