package com.inventarios.handler.atributos;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributos.services.InventariosDeleteAtributosAbstract;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosDeleteAtributos extends InventariosDeleteAtributosAbstract {

  protected void delete(long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.deleteFrom(ATRIBUTOS_TABLE)
        .where(DSL.field("id", Long.class).eq(id))
        .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}
