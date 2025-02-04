package com.inventarios.handler.articulos;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.articulos.services.InventariosReadArticuloAbstract;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class InventariosReadArticulo extends InventariosReadArticuloAbstract {

  protected Result<Record> read() throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select().from(ARTICULO_TABLE).fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}