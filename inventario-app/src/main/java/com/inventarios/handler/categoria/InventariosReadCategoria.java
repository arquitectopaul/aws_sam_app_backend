package com.inventarios.handler.categoria;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.categoria.services.InventariosReadCategoriaAbstract;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class InventariosReadCategoria extends InventariosReadCategoriaAbstract {

  @Override
  protected Result<Record> read() throws DatabaseOperationException{
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select().from(GRUPO_TABLE).fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}