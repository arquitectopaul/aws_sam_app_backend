package com.inventarios.handler.marcas;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.marcas.services.InventariosDeleteMarcaAbstract;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosDeleteMarca extends InventariosDeleteMarcaAbstract {

  protected void delete(long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.deleteFrom(MARCA_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}