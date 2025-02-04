package com.inventarios.handler.activo;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.activo.services.InventariosCreateActivoAbstract;
import com.inventarios.model.Activo;
import com.inventarios.model.Especificaciones;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import java.sql.SQLException;
import java.util.List;

public class InventariosCreateActivo extends InventariosCreateActivoAbstract {
  @Override
  public int getEspecificacionID(Activo activo) throws DatabaseOperationException {
    try {
      DSLContext dsl = RDSConexion.getDSL();
      int especificacionid = dsl.transactionResult(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);
        return insertActivo(transactionalDsl, activo);
      });
      return especificacionid;
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }

  @Override
  public void save(Activo activo, List<Especificaciones> especificacionesList) throws DatabaseOperationException {
    try {
      DSLContext dsl = RDSConexion.getDSL();
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);
        long especificacionid = activo.getId();//updateEspecifico(transactionalDsl, especifico);
        System.out.println("especificacionid = " + activo.getId());
        activo.setId(especificacionid);
        for (Especificaciones especificaciones : especificacionesList) {
          especificaciones.setEspecificacionid(activo.getId());
          insertespecificaciones(transactionalDsl, especificaciones);
        }
      });
    } catch (SQLException e) {
      // Manejar el error de la transacción
    }
  }

  private int insertActivo(DSLContext dsl, Activo activo) {
    try{
      return dsl.insertInto(ACTIVO_TABLE)
            .set(DSL.field("custodioid"), activo.getCustodio().getId())
            .set(DSL.field("articuloid"), activo.getArticulo().getId())
            .set(DSL.field("categoriaid"), activo.getCategoria().getId())
            .set(DSL.field("tipoid"), activo.getTipo().getId())
            .set(DSL.field("codinventario"), activo.getCodinventario().toUpperCase())
            .set(DSL.field("modelo"), activo.getModelo().toUpperCase())
            .set(DSL.field("marca"), activo.getMarca())
            .set(DSL.field("nroserie"), activo.getNroserie().toUpperCase())
            .set(DSL.field("fechaingreso"), activo.getFechaingreso())
            .set(DSL.field("fechaingresostr"), activo.getFechaingresostr())
            .set(DSL.field("importe"), activo.getImporte())
            .set(DSL.field("moneda"), activo.getMoneda())
            .set(DSL.field("descripcion"), activo.getDescripcion().toUpperCase())
            .set(DSL.field("proveedorid"), activo.getProveedor().getId())
            .returningResult(DSL.field("id"))
            .fetchOne()
            .getValue(DSL.field("id", Integer.class));
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
      //throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

  private int updateEspecifico(DSLContext dsl, Activo activo) {
    try{
      return dsl.update(ACTIVO_TABLE)
            .set(DSL.field("custodioid"), activo.getCustodio().getId())
            .set(DSL.field("articuloid"), activo.getArticulo().getId())
            .set(DSL.field("categoriaid"), activo.getCategoria().getId())
            .set(DSL.field("tipoid"), activo.getTipo().getId())
            .where(DSL.field("id").eq(activo.getId())) // Especificar la condición de actualización
            .execute(); // Ejecutar la actualización y devolver el número de filas afectadas
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
      //throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

  private void insertespecificaciones(DSLContext dsl, Especificaciones especificaciones) {
    try{
      dsl.insertInto(ESPECIFICACIONES_TABLE)
            //.set(DSL.field("especificoid"), especificaciones.getEspecifico().getId())
            .set(DSL.field("especificacionid"), especificaciones.getEspecificacionid())
            .set(DSL.field("nombreatributo"), especificaciones.getNombreatributo().toUpperCase())
            .set(DSL.field("descripcionatributo"), especificaciones.getDescripcionatributo().toUpperCase())
            .execute();
    } catch (Exception e) {
      //throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }
}