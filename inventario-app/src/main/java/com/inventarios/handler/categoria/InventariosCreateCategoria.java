package com.inventarios.handler.categoria;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.categoria.services.InventariosCreateCategoriaAbstract;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosCreateCategoria extends InventariosCreateCategoriaAbstract {
  protected void save(String nombregrupo, String descripgrupo) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.insertInto(GRUPO_TABLE)
        .set(DSL.field("nombregrupo"), nombregrupo)
        .set(DSL.field("descripgrupo"), descripgrupo)
        .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}