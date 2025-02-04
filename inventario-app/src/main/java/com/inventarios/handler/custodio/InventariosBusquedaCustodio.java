package com.inventarios.handler.custodio;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.custodio.services.InventariosBusquedaCustodioAbstract;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class InventariosBusquedaCustodio extends InventariosBusquedaCustodioAbstract {

  protected Result<Record> busquedaPorNombreResponsable(String filter) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select()
            .from(RESPONSABLE_TABLE)
            .where(RESPONSABLE_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}