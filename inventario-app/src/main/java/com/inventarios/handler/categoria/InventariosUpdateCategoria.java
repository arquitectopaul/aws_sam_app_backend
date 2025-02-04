package com.inventarios.handler.categoria;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.categoria.services.InventariosUpdateCategoriaAbstract;
import com.inventarios.model.Categoria;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosUpdateCategoria extends InventariosUpdateCategoriaAbstract {

  protected void update(Categoria categoria, Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.update(GRUPO_TABLE)
        .set(DSL.field("nombregrupo"), categoria.getNombregrupo())
        .set(DSL.field("descripgrupo"), categoria.getDescripgrupo())
        .where(DSL.field("id", Long.class).eq(id))
        .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}
