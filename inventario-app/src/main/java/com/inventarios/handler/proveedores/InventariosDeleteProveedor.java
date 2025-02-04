package com.inventarios.handler.proveedores;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.proveedores.services.InventariosDeleteProveedorAbstract;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosDeleteProveedor extends InventariosDeleteProveedorAbstract {

  protected void delete(long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.deleteFrom(PROVEEDOR_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}
