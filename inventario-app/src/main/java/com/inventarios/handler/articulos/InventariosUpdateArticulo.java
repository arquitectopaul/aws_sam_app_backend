package com.inventarios.handler.articulos;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.articulos.services.InventariosUpdateArticuloAbstract;
import com.inventarios.model.Articulo;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosUpdateArticulo extends InventariosUpdateArticuloAbstract {
  protected void update(Articulo articulo, Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.update(ARTICULO_TABLE)
      .set(DSL.field("nombrearticulo"), articulo.getNombrearticulo())
      .set(DSL.field("descriparticulo"), articulo.getDescriparticulo())
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}