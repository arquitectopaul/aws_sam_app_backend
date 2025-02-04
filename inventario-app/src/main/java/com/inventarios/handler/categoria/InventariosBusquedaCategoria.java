package com.inventarios.handler.categoria;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.categoria.services.InventariosBusquedaCategoriaAbstract;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class InventariosBusquedaCategoria extends InventariosBusquedaCategoriaAbstract {

  protected Result<Record> busquedaPorNombreGrupo(String filter) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select()
            .from(GRUPO_TABLE)
            .where(GRUPO_TABLE_COLUMNA.like("%" + filter + "%"))
            .fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}