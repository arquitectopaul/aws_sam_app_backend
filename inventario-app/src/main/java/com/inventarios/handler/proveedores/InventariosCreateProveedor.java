package com.inventarios.handler.proveedores;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.proveedores.services.InventariosCreateProveedorAbstract;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosCreateProveedor extends InventariosCreateProveedorAbstract {

  protected void save(String ruc,
                      String razonsocial,
                      String direccionfiscal,
                      String contacto,
                      String telefono,
                      String correo,
                      long custodioId) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.insertInto(PROVEEDOR_TABLE)
        .set(DSL.field("ruc"), ruc)
        .set(DSL.field("razonsocial"), razonsocial)
        .set(DSL.field("direccionfiscal"), direccionfiscal)
        .set(DSL.field("contacto"), contacto)
        .set(DSL.field("telefono"), telefono)
        .set(DSL.field("correo"), correo)
        .set(DSL.field("custodioId"), custodioId)
        .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}