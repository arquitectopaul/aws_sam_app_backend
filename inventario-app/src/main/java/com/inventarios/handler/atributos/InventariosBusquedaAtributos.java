package com.inventarios.handler.atributos;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributos.services.InventariosBusquedaAtributosAbstract;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class InventariosBusquedaAtributos extends InventariosBusquedaAtributosAbstract {

  protected Result<Record> busquedaPorNombreAtributos(String filter) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select()
              .from(ATRIBUTOS_TABLE)
              .where(ATRIBUTOS_TABLE_COLUMNA.like("%" + filter + "%"))
              .fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}