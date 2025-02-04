package com.inventarios.handler.custodio;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.custodio.services.InventariosDeleteCustodioAbstract;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosDeleteCustodio extends InventariosDeleteCustodioAbstract {

  protected void delete(long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.deleteFrom(RESPONSABLE_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}
