package com.inventarios.handler.categoria;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.categoria.services.InventariosDeleteCategoriaAbstract;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosDeleteCategoriaHandler extends InventariosDeleteCategoriaAbstract {

  protected void delete(long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.deleteFrom(GRUPO_TABLE)
        .where(DSL.field("id", Long.class).eq(id))
        .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}