package com.inventarios.handler.especificaciones;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.especificaciones.services.InventariosReadEspecificacionesAbstract;
import org.jooq.*;
import org.jooq.impl.DSL;
import java.sql.SQLException;
import static org.jooq.impl.DSL.field;

public class InventariosReadEspecificaciones extends InventariosReadEspecificacionesAbstract {

  @Override
  protected Result<Record3<Long, Long, String>> read(long custodioId, long articuloId, long tipoId, long categoriaId) throws DatabaseOperationException {
    try{
      DSLContext dsl = RDSConexion.getDSL();
      Condition condition = DSL.trueCondition();

      if (custodioId > 0) {
        condition = condition.and(field("atributo.custodioid").eq(custodioId));
      }
      if (articuloId > 0) {
        condition = condition.and(field("atributo.articuloid").eq(articuloId));
      }
      if (tipoId > 0) {
        condition = condition.and(field("atributo.tipoid").eq(tipoId));
      }
      if (categoriaId > 0) {
        condition = condition.and(field("atributo.categoriaid").eq(categoriaId));
      }

      return dsl.select(
                      ATRIBUTOS_ID,
                      ATRIBUTOS_ATRIBUTOID,
                      ATRIBUTOS_NOMBREATRIBUTO
              )
              .from(ATRIBUTOS_TABLE)
              .join(ATRIBUTO_TABLE)
              .on(ATRIBUTO_ID.eq(ATRIBUTOS_ATRIBUTOID))
              .where(condition)
              .fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

}