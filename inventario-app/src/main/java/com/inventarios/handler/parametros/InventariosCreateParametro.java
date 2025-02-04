package com.inventarios.handler.parametros;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.parametros.services.InventariosCreateParametroAbstract;

import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosCreateParametro extends InventariosCreateParametroAbstract {
  @Override
  protected void save(String nombre, String descripcion) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.insertInto(PARAMETRO_TABLE)
            .set(DSL.field("nombre"), nombre)
            .set(DSL.field("descripcion"), descripcion)
            .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}