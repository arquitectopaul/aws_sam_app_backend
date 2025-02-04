package com.inventarios.handler.parametros;

import com.inventarios.core.RDSConexion;
import com.inventarios.core.DatabaseOperationException;
import com.inventarios.handler.parametros.services.InventariosUpdateParametroAbstract;
import java.sql.SQLException;
import java.util.Optional;
import com.inventarios.model.Parametro;
import org.jooq.impl.DSL;

public class InventariosUpdateParametro extends InventariosUpdateParametroAbstract {

  protected Optional<Parametro> parametroSearch(Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select()
              .from(PARAMETRO_TABLE)
              .where(DSL.field("id", Long.class).eq(id))
              .fetchOptionalInto(Parametro.class);
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

  protected void update(Long id, String nombre, String descripcion) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.update(PARAMETRO_TABLE)
              .set(DSL.field("nombre"), nombre)
              .set(DSL.field("descripcion"), descripcion)
              //
              .where(DSL.field("id", Long.class).eq(id))
              .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}