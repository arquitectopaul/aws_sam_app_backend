package com.inventarios.handler.atributo.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.inventarios.handler.atributo.response.AtributoResponseRest;
import com.inventarios.model.*;
import com.inventarios.core.DatabaseOperationException;
import java.util.*;
import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.DSL.name;

public abstract class InventariosBusquedaAtributoAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  protected static final org.jooq.Table<?> ATRIBUTO_TABLE = DSL.table("atributo");
  //protected static final org.jooq.Field<Long> ATRIBUTO_TABLE_COLUMNA = DSL.field("articuloId", Long.class);

  //protected final static Table<Record> ATRIBUTO_TABLE = table("atributo");
  //protected final static Table<Record> ATRIBUTOS_TABLE = table("atributos");
  protected final static Field<Long> ATRIBUTO_ID = field(name("atributo", "id"), Long.class);
  protected final static Field<Long> ATRIBUTO_RESPONSABLE_ID = field(name("atributo", "custodioid"), Long.class);
  protected final static Field<Long> ATRIBUTO_ARTICULO_ID = field(name("atributo", "articuloid"), Long.class);
  //protected final static Field<Long> ATRIBUTOS_ID = field(name("atributos", "id"), Long.class);
  //protected final static Field<Long> ATRIBUTOS_ATRIBUTOID = field(name("atributos", "atributoid"), Long.class);
  //protected final static Field<String> ATRIBUTOS_NOMBREATRIBUTO = field(name("atributos", "nombreatributo"), String.class);

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

  /*protected final static Field<Long> PROVEEDOR_TABLE_ID = field(name("proveedor", "id"), Long.class);
  //protected static final Field<String> PROVEEDOR_TABLE_COLUMNA = DSL.field("razonsocial", String.class);
  protected static final Field<String> PROVEEDOR_TABLE_COLUMNA =  field(name("proveedor", "ruc"), String.class);

  protected final static Table<Record> PROVEEDOR_TABLE = DSL.table("proveedor");
  protected static final Field<Long> CUSTODIOID_PROVEEDOR_TABLE =  field(name("proveedor", "custodioid"), Long.class);*/
  protected final static Field<Long> PROVEEDOR_TABLE_ID = field(name("proveedor", "id"), Long.class);
  protected static final Field<String> PROVEEDOR_TABLE_RAZONSOCIAL = field(name("proveedor", "razonsocial"), String.class);
  protected static final Field<String> PROVEEDOR_TABLE_RUC = field(name("proveedor", "ruc"), String.class);
  protected static final Field<String> PROVEEDOR_TABLE_DIRECCIONFISCAL = field(name("proveedor", "direccionfiscal"), String.class);
  protected static final Field<String> PROVEEDOR_TABLE_CONTACTO = field(name("proveedor", "contacto"), String.class);
  protected static final Field<String> PROVEEDOR_TABLE_TELEFONO = field(name("proveedor", "telefono"), String.class);
  protected static final Field<String> PROVEEDOR_TABLE_CORREO = field(name("proveedor", "correo"), String.class);
  protected final static Table<Record> PROVEEDOR_TABLE = DSL.table("proveedor");
  protected static final Field<Long> CUSTODIOID_PROVEEDOR_TABLE = field(name("proveedor", "custodioid"), Long.class);
  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract Result<Record6<Long, Long, Long, Long, Long, String>> busquedaPorArticuloId(String articuloId) throws DatabaseOperationException;
  protected abstract String mostrarCustodio(Long id) throws DatabaseOperationException;
  protected abstract String mostrarArticulo(Long id) throws DatabaseOperationException;
  protected abstract String mostrarTipoBien(Long id) throws DatabaseOperationException;
  protected abstract String mostrarCategoria(Long id) throws DatabaseOperationException;
  protected abstract List<Proveedor> listaCustodioProveedores(Long id) throws DatabaseOperationException;

  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    LambdaLogger logger = context.getLogger();
    AtributoResponseRest responseRest = new AtributoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    Map<String, String> pathParameters = input.getPathParameters();
    String articuloId = pathParameters.get("id");
    logger.log("articuloId: " + articuloId);
    try {
      Result<Record6<Long, Long, Long, Long, Long, String>> result = busquedaPorArticuloId(articuloId);
      responseRest.getAtributoResponse().setListaatributos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Atributos encontrados");
      Gson gson = GsonFactory.createGson();
      String output = gson.toJson(responseRest);
      logger.log(output);
      return response.withStatusCode(200).withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al consultar");
      return response.withBody(e.toString()).withStatusCode(500);
    }
  }

  protected List<Atributo> convertResultToList(Result<Record6<Long, Long, Long, Long, Long, String>> result) throws DatabaseOperationException {
    List<Atributo> listaAtributos = new ArrayList<>();
    Map<Long, Atributo> atributosMap = new HashMap<>(); // Usamos un mapa para agrupar por atributoId

    for (Record record : result) {
      Long atributoId = record.getValue(ATRIBUTO_ID);

      if (!atributosMap.containsKey(atributoId)) {
        Atributo atributo = new Atributo();
        atributo.setId(atributoId);

        Custodio custodio = new Custodio();
        custodio.setId(record.get(ATRIBUTO_RESPONSABLE_ID));
        custodio.setArearesponsable(mostrarCustodio(custodio.getId()));
        custodio.setProveedores(new ArrayList<>());
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

        atributosMap.put(atributoId, atributo);
      }

      // Proveedores
      Proveedor proveedor = new Proveedor();
      proveedor.setId(record.get(PROVEEDOR_TABLE_ID));
      proveedor.setRazonsocial(record.getValue(PROVEEDOR_TABLE_RAZONSOCIAL));

      /*Custodio custodio=new Custodio();
      custodio.setId(record.get(CUSTODIOID_PROVEEDOR_TABLE));
      proveedor.setCustodio(custodio);*/
      //proveedor.setCustodioid();

      Atributo atributoExistente = atributosMap.get(atributoId);
      atributoExistente.getCustodio().getProveedores().add(proveedor);
    }

    // Convertir el mapa de atributos a lista
    listaAtributos.addAll(atributosMap.values());

    return listaAtributos;
  }

/*
  protected List<Atributo> convertResultToList2(Result<Record6<Long, Long, Long, Long, Long, String>> result) throws DatabaseOperationException {
    List<Atributo> listaAtributos = new ArrayList<>();
    for (Record record : result) {
      Atributo atributo = new Atributo();
      atributo.setId(record.getValue("id", Long.class));

      Custodio custodio = new Custodio();
      custodio.setId(record.get(ATRIBUTO_RESPONSABLE_ID));
      custodio.setArearesponsable(mostrarCustodio(custodio.getId()));
      List<Proveedor> proveedoresLista = listaCustodioProveedores(custodio.getId());
      for (Proveedor proveedor : proveedoresLista) {
        //proveedor.setCustodioId(custodio.getId());
        proveedor.setRazonsocial(record.getValue(PROVEEDOR_TABLE_RAZONSOCIAL, String.class));
      }
      //custodio.setProveedores(proveedoresLista);
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
      listaAtributos.add(atributo);
    }
    return listaAtributos;
  }
/*
  protected List<Atributo> convertResultToList0(Result<Record5<Long, Long, Long, Long, Long>> result) throws DatabaseOperationException {
    List<Atributo> listaAtributos = new ArrayList<>();
    for (Record record : result) {
      Atributo atributo = new Atributo();
      atributo.setId(record.getValue("id", Long.class));

      Custodio custodio = new Custodio();
      custodio.setId(record.get(ATRIBUTO_RESPONSABLE_ID));
      custodio.setArearesponsable(mostrarCustodio(custodio.getId()));
      List<Proveedor> proveedoresLista = listaCustodioProveedores(custodio.getId());
      //custodio.setProveedores(proveedoresLista);
      atributo.setCustodio(custodio);

      Articulo articulo = new Articulo();
      //System.out.println(record.get(ATRIBUTO_ARTICULO_ID)!=null?record.get(ATRIBUTO_ARTICULO_ID):"nulo");//33
      articulo.setId(record.get(ATRIBUTO_ARTICULO_ID));
      articulo.setNombrearticulo(mostrarArticulo(articulo.getId()));
      atributo.setArticulo(articulo);

      Tipo tipo = new Tipo();
      tipo.setId(record.get(ATRIBUTO_TIPO_ID));
      tipo.setNombretipo(mostrarTipoBien(tipo.getId()));
      atributo.setTipo(tipo);

      Categoria categoria =new Categoria();
      categoria.setId(record.get(ATRIBUTO_GRUPO_ID));
      categoria.setNombregrupo(mostrarCategoria(categoria.getId()));
      atributo.setCategoria(categoria);
      listaAtributos.add(atributo);
    }
    return listaAtributos;
  }
*/











  /*
  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    LambdaLogger logger = context.getLogger();
    AtributoResponseRest responseRest = new AtributoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);
    String articuloId = input.getPathParameters().get("articuloId");


    logger.log("articuloId: " + articuloId);

    try {
      Result<Record> result = busquedaPorArticuloId(String articuloId);
      responseRest.getAtributoResponse().setListaatributos(convertResultToList(result));
      responseRest.setMetadata("Respuesta ok", "00", "Atributos encontrados");
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

  protected List<Atributo> convertResultToList(Result<Record> result) {
    List<Atributo> listaAtributos = new ArrayList<>();
    for (Record record : result) {
      Atributo atributo = new Atributo();
      atributo.setId(record.getValue("id", Long.class));
      atributo.setResponsable(record.getValue("custodioId", Responsable.class));
      atributo.setTipo(record.getValue("tipoId", Tipo.class));
      atributo.setGrupo(record.getValue("categoriaId", Grupo.class));
      atributo.setArticulo(record.getValue("articuloId", Articulo.class));
      //atributo.setAtributos(record.getValue("descripatributo", List.class));
      listaAtributos.add(atributo);
    }
    return listaAtributos;
  }*/
}



    /*
    String body = input.getBody();
    context.getLogger().log("body " + body);
    try {
      if (body != null && !body.isEmpty()) {
        context.getLogger().log("body " + body);
        Atributo atributo = GsonFactory.createGson().fromJson(body, Atributo.class);
        if (atributo != null) {
          context.getLogger().log("atributo.getId() = " + atributo.getId());
          if (id.equals(atributo.getId())) {
            busquedaPorId(atributo.getId());
            list.add(atributo);
            responseRest.getAtributoResponse().setListaatributos(list);
            responseRest.setMetadata("Respuesta ok", "00", "Atributo actualizado");
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


