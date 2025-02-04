package com.inventarios.handler.tipos;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.tipos.services.InventariosCreateTipoAbstract;
import com.inventarios.model.Tipo;
import org.jooq.impl.DSL;
import java.sql.SQLException;

public class InventariosCreateTipo extends InventariosCreateTipoAbstract {

  @Override
  protected void save(Tipo tipo) throws DatabaseOperationException {
    try {
      var dsl = RDSConexion.getDSL();
      dsl.insertInto(TIPO_TABLE)
      .set(DSL.field("nombretipo"), tipo.getNombretipo().toUpperCase())
      .set(DSL.field("descriptipo"), tipo.getDescriptipo().toUpperCase())
      .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}