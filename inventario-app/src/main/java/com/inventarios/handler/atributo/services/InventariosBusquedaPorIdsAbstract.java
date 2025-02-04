package com.inventarios.handler.atributo.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.handler.atributo.response.AtributoResponseRest;
import com.inventarios.model.Articulo;
import com.inventarios.model.Atributo;
import com.inventarios.model.AtributosFiltro;
import com.inventarios.model.Custodio;
import com.inventarios.util.GsonFactory;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.impl.DSL;
import com.inventarios.core.DatabaseOperationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public abstract class InventariosBusquedaPorIdsAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final Table<?> ATRIBUTO_TABLE = DSL.table("atributo");
  protected static final Field<String> ATRIBUTO_TABLE_COLUMNA = DSL.field("nombreatributo", String.class);
  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }
  protected abstract Result<Record> filtraPorIds(AtributosFiltro filter) throws DatabaseOperationException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    LambdaLogger logger = context.getLogger();
    AtributoResponseRest responseRest = new AtributoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String idString = pathParameters.get("id");
    logger.log("buscar: " + idString);
    String output = "";
    AtributosFiltro filtro=new AtributosFiltro();
    try {
      Result<Record> result = filtraPorIds(filtro);
      responseRest.getAtributoResponse().setListaatributos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Atributos encontrados");
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

  protected List<Atributo> convertResultToList(Result<Record> result) {
    List<Atributo> listaAtributos = new ArrayList<>();
    for (Record record : result) {
      Atributo atributo = new Atributo();
      atributo.setId(record.getValue("id", Long.class));
      atributo.setCustodio(record.getValue("custodioId", Custodio.class));
      atributo.setArticulo(record.getValue("articuloId", Articulo.class));
      atributo.setAtributos(record.getValue("descripatributo", List.class));
      listaAtributos.add(atributo);
    }
    return listaAtributos;
  }

}