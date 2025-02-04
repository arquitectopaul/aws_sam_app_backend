package com.inventarios.handler.activo.services;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.inventarios.handler.activo.response.ActivoResponseRest;
import com.inventarios.core.DatabaseOperationException;
import java.util.*;
import com.inventarios.model.*;
import com.inventarios.util.GsonFactory;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class InventariosCreateActivoAbstract implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    protected final static Table<Record> ACTIVO_TABLE = DSL.table("activo");
    protected final static Table<Record> ESPECIFICACIONES_TABLE = DSL.table("especificaciones");
    final static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "content-type,X-Custom-Header,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
        headers.put("Access-Control-Allow-Methods", "POST");
    }

    protected abstract int getEspecificacionID(Activo activo) throws DatabaseOperationException;
    protected abstract void save(Activo activo, List<Especificaciones> especificacionesList) throws DatabaseOperationException;
    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        //input.setHeaders(headers);
        ActivoResponseRest responseRest = new ActivoResponseRest();
        LambdaLogger logger = context.getLogger();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        List<Activo> list = new ArrayList<>();
        List<Especificaciones> especificacionesList = new ArrayList<>();
        String output ="";
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(input.getBody());

            String body = input.getBody();
            //String body = "{\"custodioId\":4,\"articuloId\":3,\"tipoId\":1,\"categoriaId\":1,\"codinventario\":\"VQVREE\",\"modelo\":\"NINAS\",\"marca\":\"ZZDZCSCW\",\"nroserie\":\"V1VV3A\",\"fechaingreso\":\"2024-06-12\",\"fechaingresostr\":\"2024-06-12\",\"importe\":\"522515\",\"moneda\":\"S/\",\"descripcion\":\"As\",\"proveedorId\":1,\"especificaciones\":[{\"nombreatributo\":\"COLOR\",\"descripcionatributo\":\"AZUL\"}]}";

            //String body = "{\"custodioId\":4,\"articuloId\": 4,\"tipoId\": 1,\"categoriaId\": 2,\"codinventario\":\"AFAFS\",\"modelo\":\"FDAWEX\",\"marca\":\"\",\"nroserie\":\"ASC\",\"fechaingreso\":\"2024-07-05\",\"fechaingresostr\":\"2024-07-05\",\"importe\":\"53225\",\"moneda\":\"S/\",\"descripcion\":\"As\",\"proveedorId\": 4,\"especificaciones\":[{\"nombreatributo\":\"COLOR\",\"descripcionatributo\":\"ROJO\"}]}";
            if (body != null && !body.isEmpty()) {
                logger.log(body);
                Activo activo = GsonFactory.createGson().fromJson(body, Activo.class);
                ///Especifico especifico = mapper.readValue(body, Especifico.class);
                if (activo == null) {
                    return response
                            .withBody("El cuerpo de la solicitud no contiene datos válidos para un activo")
                            .withStatusCode(400);
                }
                if (activo != null) {
                    List<Especificaciones> especificacionesListaInicial = new ArrayList<>();

                    // Initialize default value for especificoid
                    Long defaultEspecificacionId = 0L;

                    // Create a default especificaciones object
                    Especificaciones defaultespecificaciones = new Especificaciones();
                    defaultespecificaciones.setEspecificacionid(defaultEspecificacionId);
                    defaultespecificaciones.setNombreatributo(""); // You can set other properties if necessary
                    defaultespecificaciones.setDescripcionatributo("");
                    // Add the default especificaciones object to especificacionesList
                    especificacionesListaInicial.add(defaultespecificaciones);

                    activo.setEspecificaciones(especificacionesListaInicial);

                    logger.log("especifico = "+ activo);

                    Long custodioId = jsonNode.get("custodioId").asLong();
                    logger.log("custodioId = "+custodioId);
                    Long articuloId = jsonNode.get("articuloId").asLong();
                    logger.log("articuloId = "+articuloId);
                    Long tipoId = jsonNode.get("tipoId").asLong();
                    logger.log("tipoId = "+tipoId);
                    Long categoriaId = jsonNode.get("categoriaId").asLong();
                    logger.log("categoriaId = "+categoriaId);
                    Long proveedorId = jsonNode.get("proveedorId").asLong();
                    logger.log("proveedorId = "+proveedorId);
                    String marca = jsonNode.get("marcaId").asText();
                    logger.log("marca = "+marca);
                    
                    Custodio custodio =new Custodio();
                    custodio.setId(custodioId);

                    Articulo articulo=new Articulo();
                    articulo.setId(articuloId);

                    Tipo tipo=new Tipo();
                    tipo.setId(tipoId);

                    Categoria categoria =new Categoria();
                    categoria.setId(categoriaId);

                    ///Proveedor proveedor=new Proveedor();
                    ///proveedor.setId(proveedorId);

                    activo.setCustodio(custodio);
                    activo.setArticulo(articulo);
                    activo.setTipo(tipo);
                    activo.setCategoria(categoria);
                    ///activo.setProveedor(proveedor);

                    activo.getCustodio().setId(custodioId);
                    activo.getArticulo().setId(articuloId);
                    activo.getTipo().setId(tipoId);
                    activo.getCategoria().setId(categoriaId);
                    ///activo.getProveedor().setId(proveedorId);

                    //activo.setProveedorId(proveedorId);
                    Proveedor proveedor =new Proveedor();
                    proveedor.setId(proveedorId);
                    //proveedor.setRazonsocial(mostrarProveedor(proveedor.getId()));
                    activo.setProveedor(proveedor);

                    activo.setMarca(marca);
                    long especificacionID = getEspecificacionID(activo);
                    activo.setId(especificacionID);

                    JsonNode especificacionesNode = jsonNode.get("especificaciones");
                    if (especificacionesNode != null && especificacionesNode.isArray()) {
                        for (JsonNode especificoNode : especificacionesNode) {
                            Especificaciones especificaciones=new Especificaciones();
                            especificaciones.setNombreatributo(especificoNode.get("nombreatributo")!=null?especificoNode.get("nombreatributo").asText():"");
                            especificaciones.setDescripcionatributo(especificoNode.get("descripcionatributo")!=null?especificoNode.get("descripcionatributo").asText():"");
                            especificacionesList.add(especificaciones);
                        }
                    }
                    activo.setEspecificaciones(especificacionesList);
                    logger.log(":::::::::::::::::::::::::::::::::: PREPARANDO PARA INSERTAR ::::::::::::::::::::::::::::::::::");
                    save(activo, especificacionesList);
                    logger.log(":::::::::::::::::::::::::::::::::: INSERCIÓN COMPLETA ::::::::::::::::::::::::::::::::::");
                    list.add(activo);

                }
            }
            responseRest.getActivoResponse().setListaactivos(list);
            //responseRest.getActivoResponse().setListaactivos(convertResultToLista(result));
            responseRest.setMetadata("Respuesta ok", "00", "Activos encontrados");
            output = GsonFactory.createGson().toJson(responseRest);
            logger.log(output);
            return response.withStatusCode(200)
                    .withBody(output);
        } catch (Exception e) {
            responseRest.setMetadata("Respuesta nok", "-1", "Error al guardar");
            return response
                    .withBody(e.toString())
                    .withStatusCode(500);
        }
    }
}
