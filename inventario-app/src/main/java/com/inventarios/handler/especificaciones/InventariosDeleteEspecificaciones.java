package com.inventarios.handler.especificaciones;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import java.sql.SQLException;
import com.inventarios.handler.especificaciones.services.InventariosDeleteEspecificacionesAbstract;
import org.jooq.impl.DSL;

public class InventariosDeleteEspecificaciones extends InventariosDeleteEspecificacionesAbstract {

  protected void delete(long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.deleteFrom(ESPECIFICACIONES_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}
