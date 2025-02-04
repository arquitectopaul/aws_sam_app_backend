package com.inventarios.handler.tipos;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.tipos.services.InventariosDeleteTipoAbstract;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosDeleteTipo extends InventariosDeleteTipoAbstract {

  protected void delete(long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.deleteFrom(TIPO_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}
