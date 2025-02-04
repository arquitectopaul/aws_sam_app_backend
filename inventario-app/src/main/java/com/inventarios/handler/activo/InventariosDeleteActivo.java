package com.inventarios.handler.activo;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import java.sql.SQLException;
import com.inventarios.handler.activo.services.InventariosDeleteActivoAbstract;
import org.jooq.impl.DSL;

public class InventariosDeleteActivo extends InventariosDeleteActivoAbstract {
  protected void delete(long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();

      // Elimina registros dependientes en la tabla 'especificaciones'
      dsl.deleteFrom(ESPECIFICACIONES_TABLE)
              .where(DSL.field("especificacionid", Long.class).eq(id))
              .execute();

      // Elimina el registro en la tabla 'activos'
      dsl.deleteFrom(ACTIVO_TABLE)
              .where(DSL.field("id", Long.class).eq(id))
              .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}
