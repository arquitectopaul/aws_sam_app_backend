package com.inventarios.handler.marcas;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.marcas.services.InventariosBusquedaMarcaAbstract;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class InventariosBusquedaMarca extends InventariosBusquedaMarcaAbstract {
  protected Result<Record> busquedaPorNombreMarca(String filter) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select()
            .from(MARCA_TABLE)
            .where(MARCA_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}