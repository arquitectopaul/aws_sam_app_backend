package com.inventarios.handler.atributos;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributos.services.InventariosUpdateAtributosAbstract;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosUpdateAtributos extends InventariosUpdateAtributosAbstract {
  protected void update(Long id, String descripatributo, String nombreatributo) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.update(ATRIBUTOS_TABLE)
        .set(DSL.field("nombreatributo"), nombreatributo)
        .where(DSL.field("id", Long.class).eq(id))
        .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}