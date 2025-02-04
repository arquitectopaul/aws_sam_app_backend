package com.inventarios.handler.especificaciones;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especificaciones.services.InventariosCreateEspecificacionesAbstract;
import com.inventarios.model.Especificaciones;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosCreateEspecificaciones extends InventariosCreateEspecificacionesAbstract {

  @Override
  protected void save(Especificaciones especificaciones, Long activoID) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.insertInto(ESPECIFICACIONES_TABLE)
            .set(DSL.field("nombreatributo"), especificaciones.getNombreatributo().toUpperCase())
            .set(DSL.field("descripcionatributo"), especificaciones.getDescripcionatributo().toUpperCase())
            .set(DSL.field("especificacionid"), activoID)
            .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}