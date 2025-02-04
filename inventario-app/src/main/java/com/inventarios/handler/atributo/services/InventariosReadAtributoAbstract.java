package com.inventarios.handler.atributo.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.core.DatabaseOperationException;
import java.util.*;
import com.inventarios.handler.atributo.response.AtributoResponseRest;
import com.inventarios.model.*;
import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.*;

public abstract class InventariosReadAtributoAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  protected final static Table<Record> ATRIBUTO_TABLE = table("atributo");
  protected final static Table<Record> ATRIBUTOS_TABLE = table("atributos");
  protected final static Field<Long> ATRIBUTO_ID = field(name("atributo", "id"), Long.class);
  protected final static Field<Long> ATRIBUTO_RESPONSABLE_ID = field(name("atributo", "custodioid"), Long.class);
  protected final static Field<Long> ATRIBUTO_ARTICULO_ID = field(name("atributo", "articuloid"), Long.class);
  protected final static Field<Long> ATRIBUTOS_ID = field(name("atributos", "id"), Long.class);
  protected final static Field<Long> ATRIBUTOS_ATRIBUTOID = field(name("atributos", "atributoid"), Long.class);
  protected final static Field<String> ATRIBUTOS_NOMBREATRIBUTO = field(name("atributos", "nombreatributo"), String.class);

  protected final static Table<Record> RESPONSABLE_TABLE = DSL.table("custodio");
  protected static final Field<String> RESPONSABLE_TABLE_COLUMNA = DSL.field("arearesponsable", String.class);
  protected final static Table<Record> ARTICULO_TABLE = table("articulo");
  protected static final Field<String> ARTICULO_TABLE_COLUMNA = DSL.field("nombrearticulo", String.class);
  protected final static Table<Record> TIPO_TABLE = DSL.table("tipo");
  protected static final Field<String> TIPO_TABLE_COLUMNA = DSL.field("nombretipo", String.class);
  protected final static Table<Record> GRUPO_TABLE = DSL.table("categoria");
  protected static final Field<String> GRUPO_TABLE_COLUMNA = DSL.field("nombregrupo", String.class);

  protected final static Field<Long> ATRIBUTO_TIPO_ID = field(name("atributo", "tipoid"), Long.class);
  protected final static Field<Long> ATRIBUTO_GRUPO_ID = field(name("atributo", "categoriaid"), Long.class);

  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract Result<Record8<Long, Long, Long, Long, Long, Long, Long, String>> read() throws DatabaseOperationException;
  protected abstract String mostrarCustodio(Long id) throws DatabaseOperationException;
  protected abstract String mostrarArticulo(Long id) throws DatabaseOperationException;
  protected abstract String mostrarTipoBien(Long id) throws DatabaseOperationException;
  protected abstract String mostrarCategoria(Long id) throws DatabaseOperationException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    AtributoResponseRest responseRest = new AtributoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    String output ="";
    try {
      Result<Record8<Long, Long, Long, Long, Long, Long, Long, String>> result = read();
      responseRest.getAtributoResponse().setListaatributos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Atributos encontrados");
      //output = GsonFactory.createGson().toJson(responseRest);
      Gson gson = GsonFactory.createGson();
      output = gson.toJson(responseRest);
      return response.withStatusCode(200).withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al consultar");
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
          // Aquí deberías asignar los valores correspondientes a responsable y articulo
          // Consultas adicionales o mapeos deben ser implementados para Responsable y Articulo
          Custodio custodio = new Custodio();
          custodio.setId(record.get(ATRIBUTO_RESPONSABLE_ID));
          custodio.setArearesponsable(mostrarCustodio(custodio.getId()));
          atributo.setCustodio(custodio);
          //atributo.setResponsable(findResponsableById(record.get(ATRIBUTO_RESPONSABLE_ID)));
          Articulo articulo = new Articulo();
          articulo.setId(record.get(ATRIBUTO_ARTICULO_ID));
          articulo.setNombrearticulo(mostrarArticulo(articulo.getId()));
          atributo.setArticulo(articulo);
          //atributo.setArticulo(findArticuloById(record.get(ATRIBUTO_ARTICULO_ID)));

          Tipo tipo = new Tipo();
          tipo.setId(record.get(ATRIBUTO_TIPO_ID));
          tipo.setNombretipo(mostrarTipoBien(tipo.getId()));
          atributo.setTipo(tipo);

          Categoria categoria =new Categoria();
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
/*
    List<Atributo> listaAtributos = new ArrayList<>();

    for (Record record : result) {
      Atributo atributo = new Atributo();
      atributo.setId(record.getValue("id", Long.class));

      / *Responsable responsable=new Responsable();
      responsable.setId(record.getValue("custodioid", Long.class));
      Articulo articulo=new Articulo();
      articulo.setId(record.getValue("articuloid", Long.class));
      atributo.setResponsable(responsable);
      atributo.setArticulo(articulo);* /

      Responsable responsable = new Responsable();
      responsable.setId(record.getValue("custodioid", Long.class));
      responsable.setArearesponsable(mostrarCustodio(responsable.getId()));
      atributo.setResponsable(responsable);

      Articulo articulo = new Articulo();
      articulo.setId(record.getValue("articuloid", Long.class));
      articulo.setNombrearticulo(mostrarArticulo(articulo.getId()));
      atributo.setArticulo(articulo);

      listaAtributos.add(atributo);
    }
    return listaAtributos;
  }*/

}
