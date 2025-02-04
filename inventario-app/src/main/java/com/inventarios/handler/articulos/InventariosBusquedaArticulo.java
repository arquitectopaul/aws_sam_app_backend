package com.inventarios.handler.articulos;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.articulos.services.InventariosBusquedaArticuloAbstract;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;


public class InventariosBusquedaArticulo extends InventariosBusquedaArticuloAbstract {

  protected Result<Record> busquedaPorNombreArticulo(String filter) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select()
            .from(ARTICULO_TABLE)
            .where(ARTICULO_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}