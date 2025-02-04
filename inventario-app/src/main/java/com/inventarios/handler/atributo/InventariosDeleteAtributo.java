package com.inventarios.handler.atributo;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributo.services.InventariosDeleteAtributoAbstract;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosDeleteAtributo extends InventariosDeleteAtributoAbstract {

  protected void delete(long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      // Elimina registros dependientes en la tabla 'atributos'
      dsl.deleteFrom(ATRIBUTOS_TABLE)
              .where(DSL.field("atributoid", Long.class).eq(id))
              .execute();

      // Elimina el registro en la tabla 'atributo'
      dsl.deleteFrom(ATRIBUTO_TABLE)
              .where(DSL.field("id", Long.class).eq(id))
              .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}