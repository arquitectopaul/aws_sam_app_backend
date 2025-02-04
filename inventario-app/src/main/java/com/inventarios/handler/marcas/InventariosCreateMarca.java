package com.inventarios.handler.marcas;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.marcas.services.InventariosCreateMarcaAbstract;
import com.inventarios.model.Marca;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosCreateMarca extends InventariosCreateMarcaAbstract {

  @Override
  protected void save(Marca marca) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.insertInto(MARCA_TABLE)
            .set(DSL.field("nombre"), marca.getNombre().toUpperCase())
            .set(DSL.field("descripcion"), marca.getDescripcion().toUpperCase())
            .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}
