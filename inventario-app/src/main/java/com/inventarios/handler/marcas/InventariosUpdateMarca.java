package com.inventarios.handler.marcas;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.marcas.services.InventariosUpdateMarcaAbstract;
import java.sql.SQLException;
import java.util.Optional;
import com.inventarios.model.Marca;
import org.jooq.impl.DSL;

public class InventariosUpdateMarca extends InventariosUpdateMarcaAbstract {

  protected Optional<Marca> marcaSearch(Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select()
            .from(MARCA_TABLE)
            .where(DSL.field("id", Long.class).eq(id))
            .fetchOptionalInto(Marca.class);
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

  protected void update(Long id, String nombre, String descripcion) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.update(MARCA_TABLE)
      .set(DSL.field("nombre"), nombre)
      .set(DSL.field("descripcion"), descripcion)
      .where(DSL.field("id", Long.class).eq(id))
      .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}