package com.inventarios.handler.tipos;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.tipos.services.InventariosBusquedaTipoAbstract;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class InventariosBusquedaTipo extends InventariosBusquedaTipoAbstract {
  protected Result<Record> busquedaPorNombreTipo(String filter) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select()
            .from(TIPO_TABLE)
            .where(TIPO_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}