package com.inventarios.handler.custodio;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.custodio.services.InventariosReadCustodioAbstract;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Result;

public class InventariosReadCustodio extends InventariosReadCustodioAbstract {

  protected Result<Record> read() throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select().from(CUSTODIO_TABLE).fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}
