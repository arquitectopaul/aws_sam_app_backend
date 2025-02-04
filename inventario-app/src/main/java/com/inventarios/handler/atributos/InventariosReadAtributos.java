package com.inventarios.handler.atributos;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributos.services.InventariosReadAtributosAbstract;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class InventariosReadAtributos extends InventariosReadAtributosAbstract {

  protected Result<Record> read() throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select().from(ATRIBUTOS_TABLE).fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}