package com.inventarios.handler.atributo;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributo.services.InventariosBusquedaPorIdsAbstract;
import com.inventarios.model.AtributosFiltro;
import org.jooq.Record;
import org.jooq.Result;
import java.sql.SQLException;

public class InventariosBusquedaPorIds extends InventariosBusquedaPorIdsAbstract {

  protected Result<Record> filtraPorIds(AtributosFiltro filter) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select()
            .from(ATRIBUTO_TABLE)
            .where(ATRIBUTO_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}