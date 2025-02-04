package com.inventarios.handler.proveedores;

import com.inventarios.core.DatabaseOperationException;
import com.inventarios.core.RDSConexion;
import com.inventarios.handler.proveedores.services.InventariosReadProveedorAbstract;
import java.sql.SQLException;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

public class InventariosReadProveedor extends InventariosReadProveedorAbstract {

    protected Result<Record9<Long, String, String, String, String, String, String, Long, String>> read() throws DatabaseOperationException {
        try{
            var dsl = RDSConexion.getDSL();
            return dsl.select(
                    PROVEEDOR_TABLE_ID, PROVEEDOR_TABLE_RAZONSOCIAL, PROVEEDOR_TABLE_RUC,
                    PROVEEDOR_TABLE_DIRECCIONFISCAL, PROVEEDOR_TABLE_CONTACTO, PROVEEDOR_TABLE_TELEFONO,
                    PROVEEDOR_TABLE_CORREO,
                    PROVEEDOR_CUSTODIO_ID, CUSTODIO_AREA_RESPONSABLE
            )
            .from(PROVEEDOR_TABLE)
            .leftJoin(CUSTODIO_TABLE)
            .on(PROVEEDOR_CUSTODIO_ID.eq(CUSTODIO_TABLE_ID))
            .fetch();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
        }
    }

    @Override
    protected String mostrarCustodio(Long id) throws DatabaseOperationException {
        try{
            var dsl = RDSConexion.getDSL();
            Record record = dsl.select(CUSTODIO_AREA_RESPONSABLE)
                .from(CUSTODIO_TABLE)
                .where(DSL.field("id", Long.class).eq(id))
                .fetchOne();
            return record != null ? record.getValue(CUSTODIO_AREA_RESPONSABLE) : null;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error al guardar el tipo de inventario", e);
        }
    }

}