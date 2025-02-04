package com.inventarios.handler.activo.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import java.math.BigDecimal;
import com.inventarios.core.DatabaseOperationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.inventarios.handler.activo.response.ActivoResponseRest;
//import com.inventarios.handler.keycloak.service.AuthorizerKeycloakAbstractHandler;
import com.inventarios.model.*;
import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.*;

public abstract class InventariosReadActivoAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  // extends AuthorizerKeycloakAbstractHandler {

  protected final static Table<Record> ACTIVO_TABLE = table("activo");
  protected final static Table<Record> ESPECIFICACIONES_TABLE = table("especificaciones");
  protected final static Field<Long> ACTIVO_ID = field(name("activo", "id"), Long.class);
  protected final static Field<Long> ACTIVO_RESPONSABLE_ID = field(name("activo", "custodioid"), Long.class);
  protected final static Field<Long> ACTIVO_ARTICULO_ID = field(name("activo", "articuloid"), Long.class);
  protected final static Field<Long> ACTIVO_GRUPO_ID = field(name("activo", "categoriaid"), Long.class);

  protected final static Field<Long> ACTIVO_PROVEEDOR_ID = field(name("activo", "proveedorid"), Long.class);

  protected final static Field<Long> ESPECIFICACIONES_ID = field(name("especificaciones", "id"), Long.class);
  protected final static Field<Long> ESPECIFICACIONES_ESPECIFICOID = field(name("especificaciones", "especificacionid"), Long.class);
  protected final static Field<String> ESPECIFICACIONES_NOMBREESPECIFICO = field(name("especificaciones", "nombreatributo"), String.class);
  protected final static Field<String> ESPECIFICACIONES_DESCRIPESPECIFICO = field(name("especificaciones", "descripcionatributo"), String.class);
  protected final static Field<String> ACTIVO_CODINVENTARIO = field(name("activo", "codinventario"), String.class);
  protected final static Field<String> ACTIVO_MODELO = field(name("activo", "modelo"), String.class);
  protected final static Field<String> ACTIVO_MARCA = field(name("activo", "marca"), String.class);
  protected final static Field<String> ACTIVO_NROSERIE = field(name("activo", "nroserie"), String.class);
  protected final static Field<LocalDate> ACTIVO_FECHAINGRESO = field(name("activo", "fechaingreso"), LocalDate.class);
  protected final static Field<String> ACTIVO_FECHAINGRESOSTR = field(name("activo", "fechaingresostr"), String.class);
  protected final static Field<String> ACTIVO_MONEDA = field(name("activo", "moneda"), String.class);
  protected final static Field<String> ACTIVO_IMPORTE = field(name("activo", "importe"), String.class);
  protected final static Field<String> ACTIVO_DESCRIPCION = field(name("activo", "descripcion"), String.class);
  protected final static Table<Record> RESPONSABLE_TABLE = DSL.table("custodio");
  protected static final Field<String> RESPONSABLE_TABLE_COLUMNA = DSL.field("arearesponsable", String.class);
  protected final static Table<Record> ARTICULO_TABLE = table("articulo");
  protected static final Field<String> ARTICULO_TABLE_COLUMNA = DSL.field("nombrearticulo", String.class);
  protected final static Table<Record> TIPO_TABLE = DSL.table("tipo");
  protected static final Field<String> TIPO_TABLE_COLUMNA = DSL.field("nombretipo", String.class);
  protected final static Table<Record> GRUPO_TABLE = DSL.table("categoria");
  protected static final Field<String> GRUPO_TABLE_COLUMNA = DSL.field("nombregrupo", String.class);
  protected final static Table<Record> PROVEEDOR_TABLE = DSL.table("proveedor");
  protected static final Field<String> PROVEEDOR_TABLE_COLUMNA = DSL.field("razonsocial", String.class);
  protected final static Field<Long> ACTIVO_TIPO_ID = field(name("activo", "tipoid"), Long.class);
  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract Result<Record19<Long, Long, Long, Long, Long, Long, Long, Long, String, String, String, String, String, String, LocalDate, String, String, String, String>> read() throws DatabaseOperationException;
  protected abstract String mostrarCustodio(Long id) throws DatabaseOperationException;
  protected abstract String mostrarArticulo(Long id) throws DatabaseOperationException;
  protected abstract String mostrarTipoBien(Long id) throws DatabaseOperationException;
  protected abstract String mostrarCategoria(Long id) throws DatabaseOperationException;
  protected abstract String mostrarProveedor(Long id) throws DatabaseOperationException;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent request, final Context context) {
    request.setHeaders(headers);
    LambdaLogger logger = context.getLogger();
    ActivoResponseRest responseRest = new ActivoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
            .withHeaders(headers);
    try {
      /*String authToken = extractAuthToken(request);
      logger.log("authToken utilizado en Activo : ");
      logger.log(authToken);
      AuthorizationInfo authInfo = validateAuthToken(authToken);
      if (authInfo == null) {
        logger.log("Token inválido");
        ///responseRest.setMetadata("Token inválido", "-1", "Token inválido o expirado.");
        ///return response.withBody(GsonFactory.createGson().toJson(responseRest)).withStatusCode(401);
      }else{
        logger.log("###############");
        logger.log("   Token OK");
        logger.log("###############");
      }
      logger.log("authInfo = " + authInfo);
      addAuthorizationHeaders(authInfo, request);*/
      ///if (request.getHttpMethod().equalsIgnoreCase("GET")) {
        logger.log("########### SOLICITUD GET ###########");
        Result<Record19<Long, Long, Long, Long, Long, Long, Long, Long, String, String, String, String, String, String, LocalDate, String, String, String, String>> result = read();
        List<Activo> activosList = convertResultToLista(result);
        responseRest.getActivoResponse().setListaactivos(activosList);
        responseRest.setMetadata("Respuesta ok", "00", "Activos encontrados");
        Gson gson = GsonFactory.createGson();
        String output = gson.toJson(responseRest);
        return response.withStatusCode(200).withBody(output);
      ///}
/*
      if (authInfo.isAdmin()) {
        logger.log("###########");
        logger.log(" ROL ADMIN");
        logger.log("###########");
        if (request.getHttpMethod().equalsIgnoreCase("POST")) {
          CreateActivoHandler createEspecificoHandler = new CreateActivoHandler();
          response = createEspecificoHandler.handleRequest(request, context);
        } else if (request.getHttpMethod().equalsIgnoreCase("PUT")) {
          UpdateActivoHandler updateEspecificoHandler = new UpdateActivoHandler();
          response = updateEspecificoHandler.handleRequest(request, context);
        } else {
          responseRest.setMetadata("No autorizado", "-1", "No autorizado para crear o actualizar activos.");
          return response.withBody(GsonFactory.createGson().toJson(responseRest)).withStatusCode(403);
        }
      } else if (authInfo.isUser()) {
          logger.log("##########");
          logger.log(" ROL USER ");
          logger.log("##########");
          // Lógica específica para usuarios no administradores
          responseRest.setMetadata("No autorizado", "-1", "No autorizado para esta acción.");
          return response.withBody(GsonFactory.createGson().toJson(responseRest)).withStatusCode(403);
      } else {
          logger.log("########## OTRO ROL ##########");
          responseRest.setMetadata("No autorizado", "-1", "No autorizado.");
          return response.withBody(GsonFactory.createGson().toJson(responseRest)).withStatusCode(403);
      }*/
    } catch (Exception ex) {
      logger.log("Error: " + ex.getMessage());
      responseRest.setMetadata("Error", "-1", "Error en la solicitud: " + ex.getMessage());
      return response.withBody(GsonFactory.createGson().toJson(responseRest)).withStatusCode(500);
    }
    // No debería llegar aquí, pero en caso de que llegue, devolvemos un error interno del servidor
    ///responseRest.setMetadata("Error", "-1", "Error inesperado en el servidor.");
    ///return response.withBody(GsonFactory.createGson().toJson(responseRest)).withStatusCode(500);
  }
  protected List<Activo> convertResultToLista(Result<Record19<Long, Long, Long, Long, Long, Long, Long, Long, String, String, String, String, String, String, LocalDate, String, String, String, String>> result) throws DatabaseOperationException {
      Map<Long, Activo> activoMap = new HashMap<>();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      for (Record19<Long, Long, Long, Long, Long, Long, Long, Long, String, String, String, String, String, String, LocalDate, String, String, String, String> record : result) {
        Long especificoId = record.get(ACTIVO_ID);
        Activo activo = activoMap.get(especificoId);
        if (activo == null) {
          activo = new Activo();
          activo.setId(especificoId);
          // Aquí deberías asignar los valores correspondientes a responsable y articulo
          // Consultas adicionales o mapeos deben ser implementados para Responsable y Articulo
          Custodio custodio = new Custodio();
          custodio.setId(record.get(ACTIVO_RESPONSABLE_ID));
          custodio.setArearesponsable(mostrarCustodio(custodio.getId()));
          activo.setCustodio(custodio);
          //especifico.setResponsable(findResponsableById(record.get(ACTIVO_RESPONSABLE_ID)));
          Articulo articulo = new Articulo();
          articulo.setId(record.get(ACTIVO_ARTICULO_ID));
          articulo.setNombrearticulo(mostrarArticulo(articulo.getId()));
          activo.setArticulo(articulo);
          //especifico.setArticulo(findArticuloById(record.get(ACTIVO_ARTICULO_ID)));

          Tipo tipo = new Tipo();
          tipo.setId(record.get(ACTIVO_TIPO_ID));
          tipo.setNombretipo(mostrarTipoBien(tipo.getId()));
          activo.setTipo(tipo);

          Categoria categoria =new Categoria();
          categoria.setId(record.get(ACTIVO_GRUPO_ID));
          categoria.setNombregrupo(mostrarCategoria(categoria.getId()));
          activo.setCategoria(categoria);

          //activo.setProveedorId(record.getValue(ACTIVO_PROVEEDOR_ID));
          Proveedor proveedor =new Proveedor();
          proveedor.setId(record.getValue(ACTIVO_PROVEEDOR_ID));
          proveedor.setRazonsocial(mostrarProveedor(proveedor.getId()));
          activo.setProveedor(proveedor);
           
          activo.setCodinventario(record.getValue("codinventario", String.class));
          activo.setModelo(record.getValue("modelo", String.class));
          activo.setMarca(record.getValue("marca", String.class));
          activo.setNroserie(record.getValue("nroserie", String.class));
          LocalDate fechaIngreso = record.getValue("fechaingreso", LocalDate.class);
          activo.setFechaingreso(fechaIngreso);
          if (fechaIngreso != null) {
            String formattedDate = fechaIngreso.format(formatter);
            activo.setFechaingresostr(formattedDate);
          }
          activo.setMoneda(record.getValue("moneda", String.class));
          activo.setImporte(record.getValue("importe", BigDecimal.class));
          activo.setDescripcion(record.getValue("descripcion", String.class));
          activo.setEspecificaciones(new ArrayList<>());
          activoMap.put(especificoId, activo);
        }
        Long especificacionesId = record.get(ESPECIFICACIONES_ID);
        if (especificacionesId != null) {
          Especificaciones especificaciones = new Especificaciones();
          especificaciones.setId(especificacionesId);
          especificaciones.setEspecificacionid(record.get(ESPECIFICACIONES_ESPECIFICOID));
          especificaciones.setNombreatributo(record.get(ESPECIFICACIONES_NOMBREESPECIFICO));
          especificaciones.setDescripcionatributo(record.get(ESPECIFICACIONES_DESCRIPESPECIFICO));
          activo.getEspecificaciones().add(especificaciones);
        }
      }
      return new ArrayList<>(activoMap.values());
    }
}