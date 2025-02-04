package com.inventarios.handler.especificaciones;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import java.sql.SQLException;
import com.inventarios.handler.especificaciones.services.InventariosUpdateEspecificacionesAbstract;
import org.jooq.impl.DSL;

public class InventariosUpdateEspecificaciones extends InventariosUpdateEspecificacionesAbstract {

  protected void update(Long id, String descripespecifico, String nombreespecifico) throws DatabaseOperationException {
      try{
        var dsl = RDSConexion.getDSL();
        dsl.update(ESPECIFICACIONES_TABLE)
        .set(DSL.field("nombreespecifico"), nombreespecifico)
        .where(DSL.field("id", Long.class).eq(id))
        .execute();
      } catch (SQLException e) {
        throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
      }
  }

}
