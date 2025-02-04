package com.inventarios.handler.tipos;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.tipos.services.InventariosUpdateTipoAbstract;
import com.inventarios.model.Tipo;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosUpdateTipo extends InventariosUpdateTipoAbstract {

  protected void update(Tipo tipo, Long id) throws DatabaseOperationException {
    try {
      var dsl = RDSConexion.getDSL();
    dsl.update(TIPO_TABLE)
      .set(DSL.field("nombretipo"), tipo.getNombretipo())
      .set(DSL.field("descriptipo"), tipo.getDescriptipo())
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}
