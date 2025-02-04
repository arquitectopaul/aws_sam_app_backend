package com.inventarios.handler.atributo;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributo.services.InventariosReadAtributoAbstract;
import java.sql.SQLException;
import org.jooq.Record;
import org.jooq.Record8;
import org.jooq.Result;
import org.jooq.impl.DSL;

public class InventariosReadAtributo extends InventariosReadAtributoAbstract {

  protected Result<Record8<Long, Long, Long, Long, Long, Long, Long, String>> read() throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      return dsl.select(
                    ATRIBUTO_ID, ATRIBUTO_RESPONSABLE_ID, ATRIBUTO_ARTICULO_ID,
                    ATRIBUTO_TIPO_ID, ATRIBUTO_GRUPO_ID,
                    ATRIBUTOS_ID, ATRIBUTOS_ATRIBUTOID, ATRIBUTOS_NOMBREATRIBUTO
            )
            .from(ATRIBUTO_TABLE)
            .leftJoin(ATRIBUTOS_TABLE)
            .on(ATRIBUTO_ID.eq(ATRIBUTOS_ATRIBUTOID))
            .fetch();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

  @Override
  protected String mostrarCustodio(Long id) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      Record record = dsl.select(RESPONSABLE_TABLE_COLUMNA)
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
      Record record = dsl.select(ARTICULO_TABLE_COLUMNA)
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
      Record record = dsl.select(TIPO_TABLE_COLUMNA)
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