package com.inventarios.handler.activo.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.inventarios.handler.activo.response.ActivoResponseRest;
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

public abstract class InventariosUpdateActivoAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected final static Table<Record> ACTIVO_TABLE = table("activo");
  protected final static Table<Record> ESPECIFICACIONES_TABLE = table("especificaciones");
  protected final static Field<Long> ACTIVO_ID = field(name("activo", "id"), Long.class);
  protected final static Field<Long> ACTIVO_RESPONSABLE_ID = field(name("activo", "custodioid"), Long.class);
  protected final static Field<Long> ACTIVO_ARTICULO_ID = field(name("activo", "articuloid"), Long.class);
  protected final static Field<Long> ESPECIFICACIONES_ID = field(name("especificaciones", "id"), Long.class);
  protected final static Field<Long> ESPECIFICACIONES_ACTIVOID = field(name("especificaciones", "activoid"), Long.class);
  protected final static Field<String> ESPECIFICACIONES_NOMBREATRIBUTO = field(name("especificaciones", "nombreactivo"), String.class);
  protected final static Table<Record> RESPONSABLE_TABLE = table("custodio");
  protected static final Field<String> RESPONSABLE_TABLE_COLUMNA = field("arearesponsable", String.class);
  protected final static Table<Record> ARTICULO_TABLE = table("articulo");
  protected static final Field<String> ARTICULO_TABLE_COLUMNA = field("nombrearticulo", String.class);
  protected final static Table<Record> TIPO_TABLE = table("tipo");
  protected static final Field<String> TIPO_TABLE_COLUMNA = field("nombretipo", String.class);
  protected final static Table<Record> GRUPO_TABLE = table("categoria");
  protected static final Field<String> GRUPO_TABLE_COLUMNA = field("nombregrupo", String.class);
  protected final static Field<Long> ACTIVO_TIPO_ID = field(name("activo", "tipoid"), Long.class);
  protected final static Field<Long> ACTIVO_GRUPO_ID = field(name("activo", "categoriaid"), Long.class);
  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "PUT");
  }

  protected abstract void updateActivo(Activo activo, List<Especificaciones> especificacionesList) throws DatabaseOperationException;
  protected abstract Result<Record8<Long, Long, Long, Long, Long, Long, Long, String>> read() throws DatabaseOperationException;
  protected abstract String mostrarCustodio(Long id) throws DatabaseOperationException;
  protected abstract String mostrarArticulo(Long id) throws DatabaseOperationException;
  protected abstract String mostrarTipoBien(Long id) throws DatabaseOperationException;
  protected abstract String mostrarCategoria(Long id) throws DatabaseOperationException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    ActivoResponseRest responseRest = new ActivoResponseRest();
    LambdaLogger logger = context.getLogger();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
    String output = "";
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonNode = mapper.readTree(input.getBody());
      String body = input.getBody();
      //String body = "{\"custodioId\": 4,\"articuloId\": 1,\"tipoId\": 1,\"categoriaId\": 2,\"especificaciones\": [{\"id\": 1,\"activoid\": 1,\"nombreactivo\": \"COLOR\"}]}";

      if (body != null && !body.isEmpty()) {
        logger.log("Body: " + body);
        Activo activo = GsonFactory.createGson().fromJson(body, Activo.class);

        if (activo != null) {
          // Comprobar los IDs de los especificaciones en la lista
          JsonNode especificacionesNode = jsonNode.get("especificaciones");
          if (especificacionesNode != null && especificacionesNode.isArray()) {
            for (JsonNode activoNode : especificacionesNode) {
              Long activoListId = activoNode.get("activoid").asLong();
              logger.log("ID del activo en la lista (activoid): " + activoListId);

              // Si activoListId es nulo, loggear el nodo completo para investigar
              if (activoListId == null) {
                logger.log("Nodo del activo con ID nulo: " + activoNode.toString());
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

          activo.setCustodio(custodio);
          activo.setArticulo(articulo);
          activo.setTipo(tipo);
          activo.setCategoria(categoria);

          List<Especificaciones> especificacionesList = new ArrayList<>();
          if (especificacionesNode != null && especificacionesNode.isArray()) {
            for (JsonNode activoNode : especificacionesNode) {
              Especificaciones especificaciones = new Especificaciones();
              especificaciones.setId(activoNode.get("id").asLong());
              especificaciones.setEspecificacionid(activoNode.get("especificacionid").asLong());
              especificaciones.setNombreatributo(activoNode.get("nombreatributo") != null ? activoNode.get("nombreatributo").asText() : "");
              especificacionesList.add(especificaciones);
            }
          }
          activo.setEspecificaciones(especificacionesList);

          logger.log("Activo después del mapeo completo: " + GsonFactory.createGson().toJson(activo));
          updateActivo(activo, especificacionesList);

          Result<Record8<Long, Long, Long, Long, Long, Long, Long, String>> result = read();
          responseRest.getActivoResponse().setListaactivos(convertResultToList(result));
          responseRest.setMetadata("Respuesta ok", "00", "Activo actualizado");
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

  protected List<Activo> convertResultToList(Result<Record8<Long, Long, Long, Long, Long, Long, Long, String>> result) throws DatabaseOperationException {
    Map<Long, Activo> activoMap = new HashMap<>();

    for (Record8<Long, Long, Long, Long, Long, Long, Long, String> record : result) {
      Long activoId = record.get(ACTIVO_ID);
      Activo activo = activoMap.get(activoId);
      if (activo == null) {
        activo = new Activo();
        activo.setId(activoId);

        Custodio custodio = new Custodio();
        custodio.setId(record.get(ACTIVO_RESPONSABLE_ID));
        custodio.setArearesponsable(mostrarCustodio(custodio.getId()));
        activo.setCustodio(custodio);

        Articulo articulo = new Articulo();
        articulo.setId(record.get(ACTIVO_ARTICULO_ID));
        articulo.setNombrearticulo(mostrarArticulo(articulo.getId()));
        activo.setArticulo(articulo);

        Tipo tipo = new Tipo();
        tipo.setId(record.get(ACTIVO_TIPO_ID));
        tipo.setNombretipo(mostrarTipoBien(tipo.getId()));
        activo.setTipo(tipo);

        Categoria categoria = new Categoria();
        categoria.setId(record.get(ACTIVO_GRUPO_ID));
        categoria.setNombregrupo(mostrarCategoria(categoria.getId()));
        activo.setCategoria(categoria);

        activo.setEspecificaciones(new ArrayList<>());
        activoMap.put(activoId, activo);
      }
      Long especificacionesId = record.get(ESPECIFICACIONES_ID);
      if (especificacionesId != null) {
        Especificaciones especificaciones = new Especificaciones();
        especificaciones.setId(especificacionesId);
        especificaciones.setEspecificacionid(record.get(ESPECIFICACIONES_ACTIVOID));
        especificaciones.setNombreatributo(record.get(ESPECIFICACIONES_NOMBREATRIBUTO));
        activo.getEspecificaciones().add(especificaciones);
      }
    }
    return new ArrayList<>(activoMap.values());
  }
}