package com.inventarios.handler.atributos;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributos.services.InventariosCreateAtributosAbstract;
import com.inventarios.model.Atributos;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosCreateAtributos extends InventariosCreateAtributosAbstract {

  @Override
  protected void save(Atributos atributos, Long categoriaID) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.insertInto(ATRIBUTOS_TABLE)
              .set(DSL.field("nombreatributo"), atributos.getNombreatributo().toUpperCase())
              .set(DSL.field("categoriaId"), categoriaID)
              .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}
