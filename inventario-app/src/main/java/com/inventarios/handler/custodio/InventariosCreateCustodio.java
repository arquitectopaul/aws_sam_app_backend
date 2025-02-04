package com.inventarios.handler.custodio;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.custodio.services.InventariosCreateCustodioAbstract;
import java.sql.SQLException;
import org.jooq.impl.DSL;

public class InventariosCreateCustodio extends InventariosCreateCustodioAbstract {

  protected void save(String arearesponsable, String nombresyapellidos) throws DatabaseOperationException {
    try{
      var dsl = RDSConexion.getDSL();
      dsl.insertInto(RESPONSABLE_TABLE)
      .set(DSL.field("arearesponsable"), arearesponsable)
      .set(DSL.field("nombresyapellidos"), nombresyapellidos)
      .execute();
    } catch (SQLException e) {
      throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
    }
  }

/*
  @Override
  public void save(Custodio custodio, List<Proveedor> proveedoresList) throws SQLException{
    DSLContext dsl = RDSConexion.getDSL();
    try {
      dsl.transaction(configuration -> {
        DSLContext transactionalDsl = DSL.using(configuration);
        long custodioid = custodio.getId();
        System.out.println("custodioid = "+ custodioid.getId());
        custodio.setId(custodioid);
        for (Proveedor proveedores : proveedoresList) {
          proveedores.setCustodioid(custodio.getId());
          System.out.println("proveedores.getCustodioid() para insertProveedores = "+proveedores.getCustodioid());
          insertAtributos(transactionalDsl, atributos);
        }
      });
    } catch (Exception e) {
    }
  }*/
}