package com.inventarios.handler.articulos;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.articulos.services.InventariosCreateArticuloAbstract;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosCreateArticulo extends InventariosCreateArticuloAbstract {

  protected void save(String nombrearticulo, String descriparticulo) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.insertInto(ARTICULO_TABLE)
        .set(DSL.field("nombrearticulo"), nombrearticulo)
        .set(DSL.field("descriparticulo"), descriparticulo)
        .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}