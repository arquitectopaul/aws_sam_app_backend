package com.inventarios.handler.atributo.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.inventarios.handler.atributo.response.AtributoResponseRest;
import com.inventarios.model.*;
import com.inventarios.core.DatabaseOperationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import static org.jooq.impl.DSL.*;

public abstract class InventariosUpdateAtributoAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> ATRIBUTO_TABLE = table("atributo");
  protected final static Table<Record> ATRIBUTOS_TABLE = table("atributos");
  protected final static Field<Long> ATRIBUTO_ID = field(name("atributo", "id"), Long.class);
  protected final static Field<Long> ATRIBUTO_RESPONSABLE_ID = field(name("atributo", "custodioid"), Long.class);
  protected final static Field<Long> ATRIBUTO_ARTICULO_ID = field(name("atributo", "articuloid"), Long.class);
  protected final static Field<Long> ATRIBUTOS_ID = field(name("atributos", "id"), Long.class);
  protected final static Field<Long> ATRIBUTOS_ATRIBUTOID = field(name("atributos", "atributoid"), Long.class);
  protected final static Field<String> ATRIBUTOS_NOMBREATRIBUTO = field(name("atributos", "nombreatributo"), String.class);
  protected final static Table<Record> RESPONSABLE_TABLE = table("custodio");
  protected static final Field<String> RESPONSABLE_TABLE_COLUMNA = field("arearesponsable", String.class);
  protected final static Table<Record> ARTICULO_TABLE = table("articulo");
  protected static final Field<String> ARTICULO_TABLE_COLUMNA = field("nombrearticulo", String.class);
  protected final static Table<Record> TIPO_TABLE = table("tipo");
  protected static final Field<String> TIPO_TABLE_COLUMNA = field("nombretipo", String.class);
  protected final static Table<Record> GRUPO_TABLE = table("categoria");
  protected static final Field<String> GRUPO_TABLE_COLUMNA = field("nombregrupo", String.class);
  protected final static Field<Long> ATRIBUTO_TIPO_ID = field(name("atributo", "tipoid"), Long.class);
  protected final static Field<Long> ATRIBUTO_GRUPO_ID = field(name("atributo", "categoriaid"), Long.class);
  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "PUT");
  }

  protected abstract void updateAtributo(Atributo atributo, List<Atributos> atributosList) throws DatabaseOperationException;
  protected abstract Result<Record8<Long, Long, Long, Long, Long, Long, Long, String>> read() throws DatabaseOperationException;
  protected abstract String mostrarCustodio(Long id) throws DatabaseOperationException;
  protected abstract String mostrarArticulo(Long id) throws DatabaseOperationException;
  protected abstract String mostrarTipoBien(Long id) throws DatabaseOperationException;
  protected abstract String mostrarCategoria(Long id) throws DatabaseOperationException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    AtributoResponseRest responseRest = new AtributoResponseRest();
    LambdaLogger logger = context.getLogger();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
    String output = "";
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonNode = mapper.readTree(input.getBody());
      String body = input.getBody();
      //String body = "{\"custodioId\": 4,\"articuloId\": 1,\"tipoId\": 1,\"categoriaId\": 2,\"atributos\": [{\"id\": 1,\"atributoid\": 1,\"nombreatributo\": \"COLOR\"}]}";

      if (body != null && !body.isEmpty()) {
        logger.log("Body: " + body);
        Atributo atributo = GsonFactory.createGson().fromJson(body, Atributo.class);

        if (atributo != null) {
          // Comprobar los IDs de los atributos en la lista
          JsonNode atributosNode = jsonNode.get("atributos");
          if (atributosNode != null && atributosNode.isArray()) {
            for (JsonNode atributoNode : atributosNode) {
              Long atributoListId = atributoNode.get("atributoid").asLong();
              logger.log("ID del atributo en la lista (atributoid): " + atributoListId);

              // Si atributoListId es nulo, loggear el nodo completo para investigar
              if (atributoListId == null) {
                logger.log("Nodo del atributo con ID nulo: " + atributoNode.toString());
              }
            }
          }

          Long custodioId = jsonNode.get("custodioId").asLong();
          Long articuloId = jsonNode.get("articuloId").asLong();
          Long tipoId = jsonNode.get("tipoId").asLong();
          Long categoriaId = jsonNode.get("categoriaId").asLong();

          Custodio custodio = new Custodio();
          custodio.setId(custodioId);

          Articulo articulo = new Articulo();
          articulo.setId(articuloId);

          Tipo tipo = new Tipo();
          tipo.setId(tipoId);

          Categoria categoria = new Categoria();
          categoria.setId(categoriaId);

          atributo.setCustodio(custodio);
          atributo.setArticulo(articulo);
          atributo.setTipo(tipo);
          atributo.setCategoria(categoria);

          List<Atributos> atributosList = new ArrayList<>();
          if (atributosNode != null && atributosNode.isArray()) {
            for (JsonNode atributoNode : atributosNode) {
              Atributos atributos = new Atributos();
              atributos.setId(atributoNode.get("id").asLong());
              atributos.setAtributoid(atributoNode.get("atributoid").asLong());
              atributos.setNombreatributo(atributoNode.get("nombreatributo") != null ? atributoNode.get("nombreatributo").asText() : "");
              atributosList.add(atributos);
            }
          }
          atributo.setAtributos(atributosList);

          logger.log("Atributo después del mapeo completo: " + GsonFactory.createGson().toJson(atributo));
          updateAtributo(atributo, atributosList);

          Result<Record8<Long, Long, Long, Long, Long, Long, Long, String>> result = read();
          responseRest.getAtributoResponse().setListaatributos(convertResultToList(result));
          responseRest.setMetadata("Respuesta ok", "00", "Atributo actualizado");
        }
      }
      Gson gson = GsonFactory.createGson();
      output = gson.toJson(responseRest);
      logger.log(output);
      return response.withStatusCode(200).withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al insertar y consultar");
      return response
              .withBody(e.toString())
              .withStatusCode(500);
    }
  }

  protected List<Atributo> convertResultToList(Result<Record8<Long, Long, Long, Long, Long, Long, Long, String>> result) throws DatabaseOperationException {
    Map<Long, Atributo> atributoMap = new HashMap<>();

    for (Record8<Long, Long, Long, Long, Long, Long, Long, String> record : result) {
      Long atributoId = record.get(ATRIBUTO_ID);
      Atributo atributo = atributoMap.get(atributoId);
      if (atributo == null) {
        atributo = new Atributo();
        atributo.setId(atributoId);

        Custodio custodio = new Custodio();
        custodio.setId(record.get(ATRIBUTO_RESPONSABLE_ID));
        custodio.setArearesponsable(mostrarCustodio(custodio.getId()));
        atributo.setCustodio(custodio);

        Articulo articulo = new Articulo();
        articulo.setId(record.get(ATRIBUTO_ARTICULO_ID));
        articulo.setNombrearticulo(mostrarArticulo(articulo.getId()));
        atributo.setArticulo(articulo);

        Tipo tipo = new Tipo();
        tipo.setId(record.get(ATRIBUTO_TIPO_ID));
        tipo.setNombretipo(mostrarTipoBien(tipo.getId()));
        atributo.setTipo(tipo);

        Categoria categoria = new Categoria();
        categoria.setId(record.get(ATRIBUTO_GRUPO_ID));
        categoria.setNombregrupo(mostrarCategoria(categoria.getId()));
        atributo.setCategoria(categoria);

        atributo.setAtributos(new ArrayList<>());
        atributoMap.put(atributoId, atributo);
      }
      Long atributosId = record.get(ATRIBUTOS_ID);
      if (atributosId != null) {
        Atributos atributos = new Atributos();
        atributos.setId(atributosId);
        atributos.setAtributoid(record.get(ATRIBUTOS_ATRIBUTOID));
        atributos.setNombreatributo(record.get(ATRIBUTOS_NOMBREATRIBUTO));
        atributo.getAtributos().add(atributos);
      }
    }
    return new ArrayList<>(atributoMap.values());
  }
}