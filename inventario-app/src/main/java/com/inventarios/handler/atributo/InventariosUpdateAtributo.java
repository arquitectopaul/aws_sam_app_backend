package com.inventarios.handler.atributo;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.atributo.services.InventariosUpdateAtributoAbstract;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.inventarios.model.Atributo;
import com.inventarios.model.Atributos;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record8;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class InventariosUpdateAtributo extends InventariosUpdateAtributoAbstract {

    @Override
    public void updateAtributo(Atributo atributo, List<Atributos> atributosList) throws DatabaseOperationException{
        try{
            try (Connection connection = RDSConexion.getConnection()) {
                DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);
                context.transaction(configuration -> {
                DSLContext dslContext = DSL.using(configuration);

                for (Atributos atributoIndividual : atributosList) {
                    // Actualiza la tabla padre `atributo`
                    int updatedRowsAtributo = dslContext.update(ATRIBUTO_TABLE)
                            .set(ATRIBUTO_RESPONSABLE_ID, atributo.getCustodio().getId())
                            .set(ATRIBUTO_ARTICULO_ID, atributo.getArticulo().getId())
                            .set(ATRIBUTO_TIPO_ID, atributo.getTipo().getId())
                            .set(ATRIBUTO_GRUPO_ID, atributo.getCategoria().getId())
                            .where(ATRIBUTO_ID.eq(atributoIndividual.getAtributoid()))
                            .execute();

                    if (updatedRowsAtributo == 0) {
                        throw new SQLException("Atributo no encontrado en la BD para el atributoid: " + atributoIndividual.getAtributoid());
                    }

                    // Actualiza la tabla hija `atributos`
                    int updatedRowsAtributos = dslContext.update(ATRIBUTOS_TABLE)
                            .set(ATRIBUTOS_NOMBREATRIBUTO, atributoIndividual.getNombreatributo())
                            .where(ATRIBUTOS_ATRIBUTOID.eq(atributoIndividual.getAtributoid()))
                            .execute();

                    if (updatedRowsAtributos == 0) {
                        throw new SQLException("Atributos no encontrado en la BD para el atributoid: " + atributoIndividual.getAtributoid());
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
                        ATRIBUTO_ID,
                        ATRIBUTO_RESPONSABLE_ID,
                        ATRIBUTO_ARTICULO_ID,
                        ATRIBUTO_TIPO_ID,
                        ATRIBUTO_GRUPO_ID,
                        ATRIBUTOS_ID,
                        ATRIBUTOS_ATRIBUTOID,
                        ATRIBUTOS_NOMBREATRIBUTO
                ).from(ATRIBUTO_TABLE)
                .leftJoin(ATRIBUTOS_TABLE).on(ATRIBUTO_ID.eq(ATRIBUTOS_ATRIBUTOID))
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
