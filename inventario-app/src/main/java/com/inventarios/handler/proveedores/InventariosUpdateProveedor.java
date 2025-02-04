package com.inventarios.handler.proveedores;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.proveedores.services.InventariosUpdateProveedorAbstract;
import com.inventarios.model.Proveedor;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosUpdateProveedor extends InventariosUpdateProveedorAbstract {

  protected void update(Proveedor proveedor, Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
    dsl.update(PROVEEDOR_TABLE)
      .set(DSL.field("ruc"), proveedor.getRuc())
      .set(DSL.field("razonsocial"), proveedor.getRazonsocial())
      .set(DSL.field("direccionfiscal"), proveedor.getDireccionfiscal())
      .set(DSL.field("contacto"), proveedor.getContacto())
      .set(DSL.field("telefono"), proveedor.getTelefono())
      .set(DSL.field("correo"), proveedor.getCorreo())
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}