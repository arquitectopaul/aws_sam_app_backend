package com.inventarios.handler.parametros;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.parametros.services.InventariosReadParametroAbstract;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class InventariosReadParametro extends InventariosReadParametroAbstract {

  protected Result<Record> read() throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select().from(PARAMETRO_TABLE).fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}