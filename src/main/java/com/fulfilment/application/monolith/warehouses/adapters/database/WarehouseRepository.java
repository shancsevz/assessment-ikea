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
    getEntityManager().createQuery(
                    "UPDATE DbWarehouse w SET w.location = :loc, w.capacity = :cap, " +
                            "w.stock = :stock, w.archivedAt = :archived WHERE w.businessUnitCode = :code")
            .setParameter("loc", warehouse.location)
            .setParameter("cap", warehouse.capacity)
            .setParameter("stock", warehouse.stock)
            .setParameter("archived", warehouse.archivedAt)
            .setParameter("code", warehouse.businessUnitCode)
            .executeUpdate();

    // Clear persistence context to see updates in subsequent queries
    getEntityManager().flush();
    getEntityManager().clear();
  }

  @Override
  public void updateStock(Warehouse warehouse) {
    getEntityManager().createQuery(
                    "UPDATE DbWarehouse w SET w.stock = :stock WHERE w.businessUnitCode = :code")
            .setParameter("stock", warehouse.stock)
            .executeUpdate();

    // Clear persistence context to see updates in subsequent queries
    getEntityManager().flush();
    getEntityManager().clear();
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
