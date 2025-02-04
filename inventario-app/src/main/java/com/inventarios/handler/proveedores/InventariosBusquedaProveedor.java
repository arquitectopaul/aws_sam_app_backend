package com.inventarios.handler.proveedores;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.proveedores.services.InventariosBusquedaProveedorAbstract;
import java.sql.SQLException;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
public class InventariosBusquedaProveedor extends InventariosBusquedaProveedorAbstract {
  @Override
  protected Result<Record8<Long, String, String, Long, String, String, String, String>> busquedaPorNombreProveedor(String areaResponsable) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select(
                PROVEEDOR_ID,
                PROVEEDOR_TABLE_RAZONSOCIAL,
                PROVEEDOR_TABLE_RUC,
                PROVEEDOR_CUSTODIO_ID,
                PROVEEDOR_TABLE_DIRECCION,
                PROVEEDOR_TABLE_CONTACTO,
                PROVEEDOR_TABLE_TELEFONO,
                PROVEEDOR_TABLE_CORREO
              )
              .from(PROVEEDOR_TABLE)
              .join(CUSTODIO_TABLE)
              .on(PROVEEDOR_CUSTODIO_ID.eq(CUSTODIO_ID))
              .where(CUSTODIO_TABLE_COLUMNA.likeIgnoreCase("%" + areaResponsable + "%"))
              .fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

  @Override
  protected String mostrarCustodio(Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      Record record = dsl.select(CUSTODIO_TABLE_COLUMNA)
              .from(CUSTODIO_TABLE)
              .where(DSL.field("id", Long.class).eq(id))
              .fetchOne();
      return record != null ? record.getValue(CUSTODIO_TABLE_COLUMNA) : null;
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }


  /*
  @Override
  protected String mostrarCustodio(Long id) throws DatabaseOperationException {
    var dsl = RDSConexion.getDSL();
    return dsl.select(PROVEEDOR_TABLE_RAZONSOCIAL)
            .from(PROVEEDOR_TABLE)
            .where(DSL.field(PROVEEDOR_CUSTODIO_ID).eq(id))
            .fetchOne();
    return record != null ? record.getValue(CUSTODIO_TABLE_COLUMNA) : null;
  }

/*
  @Override
  protected Result<Record> autocompletarProveedor(String filter) throws DatabaseOperationException {
    var dsl = RDSConexion.getDSL();
    return dsl.select()
            .from(PROVEEDOR_TABLE)
            .where(PROVEEDOR_TABLE_COLUMNA.likeIgnoreCase("%" + filter + "%"))
            .limit(10) // Limita el número de resultados a 10
            .fetch();
  }*/
}