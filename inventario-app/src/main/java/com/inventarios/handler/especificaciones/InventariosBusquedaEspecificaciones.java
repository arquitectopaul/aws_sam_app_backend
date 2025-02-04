package com.inventarios.handler.especificaciones;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import java.sql.SQLException;
import com.inventarios.handler.especificaciones.services.InventariosBusquedaEspecificacionesAbstract;
import org.jooq.Record;
import org.jooq.Result;

public class InventariosBusquedaEspecificaciones extends InventariosBusquedaEspecificacionesAbstract {

  protected Result<Record> busquedaPorNombreespecificaciones(String filter) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select()
              .from(ESPECIFICACIONES_TABLE)
              .where(ESPECIFICACIONES_TABLE_COLUMNA.like("%" + filter + "%"))
              .fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}