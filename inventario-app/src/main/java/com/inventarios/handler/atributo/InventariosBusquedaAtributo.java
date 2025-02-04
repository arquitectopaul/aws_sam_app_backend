package com.inventarios.handler.atributo;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributo.services.InventariosBusquedaAtributoAbstract;
import java.sql.SQLException;
import java.util.List;
import com.inventarios.model.Proveedor;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

public class InventariosBusquedaAtributo extends InventariosBusquedaAtributoAbstract {

  @Override
  protected Result<Record6<Long, Long, Long, Long, Long, String>> busquedaPorArticuloId(String articuloId)
          throws DatabaseOperationException {
    if (articuloId == null || articuloId.isEmpty()) {
      throw new IllegalArgumentException("El articuloId no puede ser nulo o vacío");
    }
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select(
                      ATRIBUTO_ID, ATRIBUTO_RESPONSABLE_ID, ATRIBUTO_ARTICULO_ID,
                      ATRIBUTO_TIPO_ID, ATRIBUTO_GRUPO_ID, PROVEEDOR_TABLE_RAZONSOCIAL
              )
              .from(ATRIBUTO_TABLE)
              .join(PROVEEDOR_TABLE).on(ATRIBUTO_RESPONSABLE_ID.eq(CUSTODIOID_PROVEEDOR_TABLE))
              .where(ATRIBUTO_ARTICULO_ID.eq(Long.parseLong(articuloId)))
              .fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

  @Override
  protected List<Proveedor> listaCustodioProveedores(Long id) throws DatabaseOperationException {
    try{
      DSLContext dsl = RDSConexion.getDSL();
      Result<Record7<Long, String, String, String, String, String, String>> result = dsl.select(
                      PROVEEDOR_TABLE_ID,
                      PROVEEDOR_TABLE_RAZONSOCIAL,
                      PROVEEDOR_TABLE_RUC,
                      PROVEEDOR_TABLE_DIRECCIONFISCAL,
                      PROVEEDOR_TABLE_CONTACTO,
                      PROVEEDOR_TABLE_TELEFONO,
                      PROVEEDOR_TABLE_CORREO
              )
              .from(PROVEEDOR_TABLE)
              .where(CUSTODIOID_PROVEEDOR_TABLE.eq(id))
              .fetch();

      return result.into(Proveedor.class);
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

  @Override
  protected String mostrarCustodio(Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      Record record = dsl.select(RESPONSABLE_TABLE_COLUMNA)
              .from(RESPONSABLE_TABLE)
              .where(DSL.field("id", Long.class).eq(id))
              .fetchOne();
      return record != null ? record.getValue(RESPONSABLE_TABLE_COLUMNA) : null;
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

  @Override
  protected String mostrarArticulo(Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      Record record = dsl.select(ARTICULO_TABLE_COLUMNA)
              .from(ARTICULO_TABLE)
              .where(DSL.field("id", Long.class).eq(id))
              .fetchOne();
      return record != null ? record.getValue(ARTICULO_TABLE_COLUMNA) : null;
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

  @Override
  protected String mostrarTipoBien(Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      Record record = dsl.select(TIPO_TABLE_COLUMNA)
              .from(TIPO_TABLE)
              .where(DSL.field("id", Long.class).eq(id))
              .fetchOne();
      return record != null ? record.getValue(TIPO_TABLE_COLUMNA) : null;
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

  @Override
  protected String mostrarCategoria(Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      Record record = dsl.select(GRUPO_TABLE_COLUMNA)
              .from(GRUPO_TABLE)
              .where(DSL.field("id", Long.class).eq(id))
              .fetchOne();
      return record != null ? record.getValue(GRUPO_TABLE_COLUMNA) : null;
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}

/*
  protected List<Proveedor> listaCustodioProveedores1(Long id) throws DatabaseOperationException {
    DSLContext dsl = RDSConexion.getDSL();
    Result<Record7<Long, String, String, String, String, String, String>> result = dsl.select(
                    PROVEEDOR_TABLE_ID,
                    PROVEEDOR_TABLE_RAZONSOCIAL,
                    PROVEEDOR_TABLE_RUC,
                    PROVEEDOR_TABLE_DIRECCIONFISCAL,
                    PROVEEDOR_TABLE_CONTACTO,
                    PROVEEDOR_TABLE_TELEFONO,
                    PROVEEDOR_TABLE_CORREO
            )
            .from(PROVEEDOR_TABLE)
            .where(CUSTODIOID_PROVEEDOR_TABLE.eq(id))
            .fetch();

    return result.into(Proveedor.class);
  }
  protected List<Proveedor> listaCustodioProveedores0(Long id) throws DatabaseOperationException {
    DSLContext dsl = RDSConexion.getDSL();
    Result<Record> result = dsl.select(

            )
            .from(PROVEEDOR_TABLE)
            //.where(CUSTODIOID_PROVEEDOR_TABLE.eq(id))
            .where(DSL.field("id", Long.class).eq(id))
            .fetch();
    return result.into(Proveedor.class);
  }*/


  /*@Override
  protected List<Proveedor> listaCustodioProveedores(Long id) throws DatabaseOperationException {
    DSLContext dsl = RDSConexion.getDSL();
    Result<Record2<Long, String>> result = dsl.select(PROVEEDOR_TABLE.ID, PROVEEDOR_TABLE.COLUMN)
            .from(PROVEEDOR_TABLE)
            .leftJoin(RESPONSABLE_TABLE)
            .on(PROVEEDOR_TABLE.CUSTODIOID.eq(RESPONSABLE_TABLE.ID))
            .where(PROVEEDOR_TABLE.ID.eq(PROVEEDOR_TABLE.CUSTODIOID))
            .fetch();
    return result.into(Proveedor.class);
  }*/

  /*protected List<Proveedor> listaCustodioProveedores(Long id) throws DatabaseOperationException {
    DSLContext dsl = RDSConexion.getDSL();
    Result<Record2<Long, String>> result = dsl.select(PROVEEDOR_TABLE_ID, PROVEEDOR_TABLE_COLUMNA)
            .from(PROVEEDOR_TABLE)
            .leftJoin(RESPONSABLE_TABLE)
            .on(CUSTODIOID_PROVEEDOR_TABLE.eq(id))
            .fetch();

    return result.into(Proveedor.class);
  }*/