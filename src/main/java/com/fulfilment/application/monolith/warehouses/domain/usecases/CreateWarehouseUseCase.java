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
    Warehouse isWHExist = warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode);
    if (isWHExist != null) {
      throw new IllegalArgumentException(
              "Warehouse already exists for '" + warehouse.businessUnitCode);
    }

    //Task-3.2 :: Location Validation
    Location isLocExist = locationResolver.resolveByIdentifier(warehouse.location);
    if (isLocExist == null) {
      throw new IllegalArgumentException(
              "Location not valid for '" + warehouse.location);
    }

    //Task-3.3 :: Warehouse Creation Feasibility
    if (warehouse.capacity > isLocExist.maxCapacity) {
      throw new IllegalArgumentException( "Warehouse maximum number of warehouses has already been reached (" + isLocExist.maxCapacity + ")");
    }

    //Task-3.4 :: Capacity and Stock Validation
    if (warehouse.stock > warehouse.capacity) {
      throw new IllegalArgumentException("The maximum capacity exceeds warehouse capacity(" + warehouse.capacity + ")");
    }

//Task-Bonus-1 ::Each Product can be fulfilled by a maximum of 2 different Warehouses per Store
    List<Warehouse> allWarehouse = warehouseStore.getAll();


    warehouse.createdAt = LocalDateTime.now();

    // if all went well, create the warehouse
    warehouseStore.create(warehouse);
  }
}
