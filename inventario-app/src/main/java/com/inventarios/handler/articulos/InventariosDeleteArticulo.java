package com.inventarios.handler.articulos;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.articulos.services.InventariosDeleteArticuloAbstract;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosDeleteArticulo extends InventariosDeleteArticuloAbstract {

  protected void delete(long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.deleteFrom(ARTICULO_TABLE)
        .where(DSL.field("id", Long.class).eq(id))
        .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}
