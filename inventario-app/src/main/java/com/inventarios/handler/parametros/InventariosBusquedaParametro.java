package com.inventarios.handler.parametros;

import com.inventarios.core.RDSConexion;
import com.inventarios.core.DatabaseOperationException;
import com.inventarios.handler.parametros.services.InventariosBusquedaParametroAbstract;
import java.sql.SQLException;
import com.inventarios.model.AtributosFiltro;
import org.jooq.Record;
import org.jooq.Result;

public class InventariosBusquedaParametro extends InventariosBusquedaParametroAbstract {

  protected Result<Record> filterAtributos(AtributosFiltro filter) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select()
            .from(PARAMETRO_TABLE)
            .where(PARAMETRO_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}