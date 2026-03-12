package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore, LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  @Override
  public void create(Warehouse warehouse) {
    //Task-3.1 :: Business Unit Code Verification
    Warehouse existing = warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode);
    if (existing != null) {
      throw new IllegalArgumentException(
              "Warehouse with business unit code '" + warehouse.businessUnitCode + "' already exists");
    }

    //Task-3.2 :: Location Validation
    Location location = locationResolver.resolveByIdentifier(warehouse.location);
    if (location == null) {
      throw new IllegalArgumentException(
              "Location '" + warehouse.location + "' is not valid");
    }

    //Task-3.3 :: Warehouse Creation Feasibility
    if (warehouse.capacity > location.maxCapacity) {
      throw new IllegalArgumentException(
              "Warehouse capacity (" + warehouse.capacity +
                      ") exceeds location max capacity (" + location.maxCapacity + ")");
    }

    //Task-3.4 :: Capacity and Stock Validation
    if (warehouse.stock > warehouse.capacity) {
      throw new IllegalArgumentException(
              "Warehouse stock (" + warehouse.stock +
                      ") exceeds warehouse capacity (" + warehouse.capacity + ")");
    }

//Task-Bonus-1 ::Each Product can be fulfilled by a maximum of 2 different Warehouses per Store
    if (warehouse.stock > warehouse.capacity) {
      throw new IllegalArgumentException(
              "Warehouse stock (" + warehouse.stock +
                      ") exceeds warehouse capacity (" + warehouse.capacity + ")");
    }

    // Set creation timestamp
    warehouse.createdAt = java.time.LocalDateTime.now();

    // All validations passed, create the warehouse
    warehouseStore.create(warehouse);
  }

}
