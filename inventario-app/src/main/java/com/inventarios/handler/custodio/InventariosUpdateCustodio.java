package com.inventarios.handler.custodio;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.custodio.services.InventariosUpdateCustodioAbstract;
import com.inventarios.model.Custodio;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosUpdateCustodio extends InventariosUpdateCustodioAbstract {

  protected void update(Custodio custodio, Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.update(RESPONSABLE_TABLE)
        .set(DSL.field("arearesponsable"), custodio.getArearesponsable())
        .set(DSL.field("nombresyapellidos"), custodio.getNombresyapellidos())
        .where(DSL.field("id", Long.class).eq(id))
        .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}