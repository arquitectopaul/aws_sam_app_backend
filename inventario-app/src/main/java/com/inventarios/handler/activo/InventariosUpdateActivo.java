package com.inventarios.handler.activo;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.activo.services.InventariosUpdateActivoAbstract;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.inventarios.model.Activo;
import com.inventarios.model.Especificaciones;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record8;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class InventariosUpdateActivo extends InventariosUpdateActivoAbstract {

  @Override
  public void updateActivo(Activo activo, List<Especificaciones> especificacionesList) throws DatabaseOperationException {
    try {
      try (Connection connection = RDSConexion.getConnection()) {
        DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
        context.transaction(configuration -> {
          DSLContext dslContext = DSL.using(configuration);

          for (Especificaciones activoIndividual : especificacionesList) {
            // Actualiza la tabla padre `activo`
            int updatedRowsActivo = dslContext.update(ACTIVO_TABLE)
                    .set(ACTIVO_RESPONSABLE_ID, activo.getCustodio().getId())
                    .set(ACTIVO_ARTICULO_ID, activo.getArticulo().getId())
                    .set(ACTIVO_TIPO_ID, activo.getTipo().getId())
                    .set(ACTIVO_GRUPO_ID, activo.getCategoria().getId())
                    .where(ACTIVO_ID.eq(activoIndividual.getEspecificacionid()))
                    .execute();

            if (updatedRowsActivo == 0) {
              throw new SQLException("Activo no encontrado en la BD para el activoid: " + activoIndividual.getEspecificacionid());
            }

            // Actualiza la tabla hija `especificaciones`
            int updatedRowsEspecificaciones = dslContext.update(ESPECIFICACIONES_TABLE)
                    .set(ESPECIFICACIONES_NOMBREATRIBUTO, activoIndividual.getNombreatributo())
                    .where(ESPECIFICACIONES_ACTIVOID.eq(activoIndividual.getEspecificacionid()))
                    .execute();

            if (updatedRowsEspecificaciones == 0) {
              throw new SQLException("Especificaciones no encontrado en la BD para el activoid: " + activoIndividual.getEspecificacionid());
            }
          }
        });
      }
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

  
  @Override
  protected Result<Record8<Long, Long, Long, Long, Long, Long, Long, String>> read() throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select(
                      ACTIVO_ID,
                      ACTIVO_RESPONSABLE_ID,
                      ACTIVO_ARTICULO_ID,
                      ACTIVO_TIPO_ID,
                      ACTIVO_GRUPO_ID,
                      ESPECIFICACIONES_ID,
                      ESPECIFICACIONES_ACTIVOID,
                      ESPECIFICACIONES_NOMBREATRIBUTO
              ).from(ACTIVO_TABLE)
              .leftJoin(ESPECIFICACIONES_TABLE).on(ACTIVO_ID.eq(ESPECIFICACIONES_ACTIVOID))
              .fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
    
  @Override
  protected String mostrarCustodio(Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      org.jooq.Record record = dsl.select(RESPONSABLE_TABLE_COLUMNA)
              .from(RESPONSABLE_TABLE)
              .where(DSL.field("id", Long.class).eq(id))
              .fetchOne();
      return record != null ? record.getValue(RESPONSABLE_TABLE_COLUMNA) : null;
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
  
  @Override
  protected String mostrarArticulo(Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      org.jooq.Record record = dsl.select(ARTICULO_TABLE_COLUMNA)
              .from(ARTICULO_TABLE)
              .where(DSL.field("id", Long.class).eq(id))
              .fetchOne();
      return record != null ? record.getValue(ARTICULO_TABLE_COLUMNA) : null;
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
  
  @Override
  protected String mostrarTipoBien(Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      org.jooq.Record record = dsl.select(TIPO_TABLE_COLUMNA)
              .from(TIPO_TABLE)
              .where(DSL.field("id", Long.class).eq(id))
              .fetchOne();
      return record != null ? record.getValue(TIPO_TABLE_COLUMNA) : null;
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
  
  @Override
  protected String mostrarCategoria(Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      Record record = dsl.select(GRUPO_TABLE_COLUMNA)
              .from(GRUPO_TABLE)
              .where(DSL.field("id", Long.class).eq(id))
              .fetchOne();
      return record != null ? record.getValue(GRUPO_TABLE_COLUMNA) : null;
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}