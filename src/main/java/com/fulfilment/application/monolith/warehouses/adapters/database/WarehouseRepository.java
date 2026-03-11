package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

  @Override
  public List<Warehouse> getAll() {
    return this.listAll().stream().map(DbWarehouse::toWarehouse).toList();
  }

  @Override
  public void create(Warehouse warehouse) {
    // Task-3
    DbWarehouse dbWarehouse = new DbWarehouse(
            warehouse.businessUnitCode,
            warehouse.location,
            warehouse.capacity,
            warehouse.stock,
            warehouse.createdAt,
            warehouse.archivedAt);
    this.persist(dbWarehouse);
  }

  @Override
  public void update(Warehouse warehouse) {
    // Task-3
    DbWarehouse dbWarehouse = find("businessUnitCode", warehouse.businessUnitCode).firstResult();
    if(dbWarehouse  != null){
      String Query = "UPDATE DbWarehouse dw SET dw.location = :loc, dw.capacity = :cap, " +
              "dw.stock = :stk, dw.archivedAt = :archat WHERE dw.businessUnitCode = :bucode";

      Parameters parameters = new Parameters();
      parameters.and("loc",warehouse.location);
      parameters.and("cap",warehouse.capacity);
      parameters.and("stk",warehouse.stock);
      parameters.and("archat",warehouse.archivedAt);
      parameters.and("bucode",warehouse.businessUnitCode);
      this.update(Query,parameters);
    }
  }

  @Override
  public void remove(Warehouse warehouse) {
    // Task-3
    DbWarehouse dbWarehouse = find("businessUnitCode", warehouse.businessUnitCode).firstResult();
    this.deleteById(dbWarehouse.id);
  }

  @Override
  public Warehouse findByBusinessUnitCode(String buCode) {
    // Task-3
    DbWarehouse dbWarehouse = find("businessUnitCode", buCode).firstResult();
    return dbWarehouse != null ? dbWarehouse.toWarehouse() : null;
  }
}
