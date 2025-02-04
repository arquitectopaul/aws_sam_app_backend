package com.inventarios.handler.activo.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.inventarios.handler.activo.response.ActivoResponseRest;
import com.inventarios.model.*;
import java.math.BigDecimal;
import com.inventarios.core.DatabaseOperationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.inventarios.util.Conversiones;
import com.inventarios.util.GsonFactory;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.DSL.name;

public abstract class InventariosBusquedaActivoAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

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
  protected final static Table<Record> CUSTODIO_TABLE = DSL.table("custodio");
  protected static final Field<String> CUSTODIO_TABLE_COLUMNA = DSL.field("arearesponsable", String.class);
  protected final static Table<Record> ARTICULO_TABLE = table("articulo");
  protected static final Field<String> ARTICULO_TABLE_COLUMNA = DSL.field("nombrearticulo", String.class);
  protected final static Table<Record> TIPO_TABLE = DSL.table("tipo");
  protected static final Field<String> TIPO_TABLE_COLUMNA = DSL.field("nombretipo", String.class);
  protected final static Table<Record> GRUPO_TABLE = DSL.table("categoria");
  protected static final Field<String> GRUPO_TABLE_COLUMNA = DSL.field("nombregrupo", String.class);
  protected final static Table<Record> PROVEEDOR_TABLE = DSL.table("proveedor");
  protected static final Field<String> PROVEEDOR_TABLE_COLUMNA = DSL.field("razonsocial", String.class);

  protected final static Field<Long> ACTIVO_TIPO_ID = field(name("activo", "tipoid"), Long.class);
  //protected static final org.jooq.Field<String> ACTIVO_TABLE_COLUMNA = DSL.field("nombreespecifico", String.class);
  final static Map<String, String> headers = new HashMap<>();

  static {
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
    headers.put("Access-Control-Allow-Methods", "GET");
  }

  protected abstract Result<Record19<Long, Long, Long, Long, Long, Long, Long, Long, String, String, String, String, String, String, LocalDate, String, String, String, String>>
  busquedaActivo(long custodioId, String codinventario, String modelo, String marca, String nroSerie, LocalDate fechaCompraDesde, LocalDate fechaCompraHasta, long proveedorId) throws DatabaseOperationException;
  protected abstract String mostrarCustodio(Long id) throws DatabaseOperationException;
  protected abstract String mostrarArticulo(Long id) throws DatabaseOperationException;
  protected abstract String mostrarTipoBien(Long id) throws DatabaseOperationException;
  protected abstract String mostrarCategoria(Long id) throws DatabaseOperationException;
  protected abstract String mostrarProveedor(Long id) throws DatabaseOperationException;

  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
    input.setHeaders(headers);
    LambdaLogger logger = context.getLogger();
    ActivoResponseRest responseRest = new ActivoResponseRest();
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headers);

    Map<String, String> queryParameters = input.getQueryStringParameters();
    long custodioId = 0;
    long proveedorId = 0;

    // Verificar y convertir parámetros de consulta a long
    try {
      if (queryParameters != null && queryParameters.get("custodioId") != null) {
        custodioId = Long.parseLong(queryParameters.get("custodioId"));
      }
      if (queryParameters != null && queryParameters.get("proveedorId") != null) {
        proveedorId = Long.parseLong(queryParameters.get("proveedorId"));
      }
    } catch (NumberFormatException e) {
      logger.log("Error al convertir parámetros de consulta a long: " + e.getMessage());
      responseRest.setMetadata("Respuesta nok", "-1", "Error al convertir parámetros de consulta a long");
      String output = GsonFactory.createGson().toJson(responseRest);
      return response.withStatusCode(400).withBody(output);
    }
    logger.log("custodioId: " + custodioId);
    logger.log("proveedorId: " + proveedorId);

    String codinventario = queryParameters != null ? queryParameters.get("codinventario") : null;
    logger.log("codinventario: " + codinventario);
    String modelo = queryParameters != null ? queryParameters.get("modelo") : null;
    logger.log("modelo: " + modelo);
    String marca = queryParameters != null ? queryParameters.get("marca") : null;
    logger.log("marca: " + marca);
    String nroSerie = queryParameters != null ? queryParameters.get("nroserie") : null;
    logger.log("nroserie: " + nroSerie);
    String fechaCompraDesde = queryParameters != null ? queryParameters.get("fechadesde") : null;
    logger.log("fechadesde: " + fechaCompraDesde);
    String fechaCompraHasta = queryParameters != null ? queryParameters.get("fechahasta") : null;
    logger.log("fechahasta: " + fechaCompraHasta);
    LocalDate fechaDesde = new Conversiones().convertirALocalDate(fechaCompraDesde);
    logger.log("fechadesde: " + fechaCompraDesde);
    LocalDate fechaHasta = new Conversiones().convertirALocalDate(fechaCompraHasta);
    logger.log("fechahasta: " + fechaCompraHasta);
    try {
      Result<Record19<Long, Long, Long, Long, Long, Long, Long, Long, String, String, String, String, String, String, LocalDate, String, String, String, String>> result =
              busquedaActivo(custodioId, codinventario, modelo, marca, nroSerie, fechaDesde, fechaHasta, proveedorId);
      responseRest.getActivoResponse().setListaactivos(convertResultToLista(result));
      responseRest.setMetadata("Respuesta ok", "00", "Activos encontrados");
      String output = GsonFactory.createGson().toJson(responseRest);
      return response.withStatusCode(200)
              .withBody(output);
    } catch (Exception e) {
      responseRest.setMetadata("Respuesta nok", "-1", "Error al guardar");
      return response
              .withBody(e.toString())
              .withStatusCode(500);
    }
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

        //activo.setProveedorId(record.get(ACTIVO_PROVEEDOR_ID));
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

        activoMap.put(especificoId, activo);
      }
      Long especificacionesId = record.get(ESPECIFICACIONES_ID);
      List<Especificaciones> listaEspecificaciones = new ArrayList<>();
      if (especificacionesId != null) {
        Especificaciones especificaciones = new Especificaciones();
        especificaciones.setId(especificacionesId);
        especificaciones.setEspecificacionid(record.get(ESPECIFICACIONES_ESPECIFICOID));
        especificaciones.setNombreatributo(record.get(ESPECIFICACIONES_NOMBREESPECIFICO));
        especificaciones.setDescripcionatributo(record.get(ESPECIFICACIONES_DESCRIPESPECIFICO));
        listaEspecificaciones.add(especificaciones);
        activo.setEspecificaciones(listaEspecificaciones);
      }
    }
    return new ArrayList<>(activoMap.values());
  }
  protected List<Activo> convertResultToListY(Result<Record> result) {
    List<Activo> listaActivos = new ArrayList<>();
    for (Record record : result) {
      Activo activo = new Activo();
      activo.setId(record.getValue("id", Long.class));
      activo.setCodinventario(record.getValue("codinventario", String.class));
      activo.setModelo(record.getValue("modelo", String.class));
      activo.setMarca(record.getValue("marca", String.class));
      activo.setNroserie(record.getValue("nroserie", String.class));
      activo.setFechaingreso(record.getValue("fechaingreso", LocalDate.class));
      activo.setMoneda(record.getValue( "nroserie", String.class));
      activo.setImporte(record.getValue("importe", BigDecimal.class));
      //Responsable responsable = new Responsable();
      //activo.setResponsable(responsable);
      ///activo.setResponsable(record.getValue("responsableId", Responsable.class));
      ///activo.setGrupo(record.getValue("grupoId", Grupo.class));
      listaActivos.add(activo);
    }
    return listaActivos;
  }
  protected List<Activo> convertResultToListX(Result<Record> result) {
    List<Activo> listaActivos = new ArrayList<>();
    for (Record record : result) {
      Activo activo = new Activo();
      activo.setId(record.getValue("id", Long.class));
      activo.setCustodio(record.getValue("custodioId", Custodio.class));
      activo.setArticulo(record.getValue("articuloId", Articulo.class));
      ///especifico.setespecificaciones(record.getValue("descripespecifico", List.class));
      listaActivos.add(activo);
    }
    return listaActivos;
  }
}

