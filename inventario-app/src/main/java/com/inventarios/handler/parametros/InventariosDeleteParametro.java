package com.inventarios.handler.parametros;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.parametros.services.InventariosDeleteParametroAbstract;
import java.sql.SQLException;
import java.util.Optional;
import com.inventarios.model.Parametro;
import org.jooq.impl.DSL;

public class InventariosDeleteParametro extends InventariosDeleteParametroAbstract {
//Probar con una base de datos Postgres versión de más alto performance

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

  protected void delete(long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.deleteFrom(PARAMETRO_TABLE)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}
