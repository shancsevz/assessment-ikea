package com.fulfilment.application.monolith.products;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ProductFulfillmentRepository implements PanacheRepository<ProductFulfillment> {

  /**
   * Find all warehouses fulfilling a specific product in a specific store
   */
  public List<ProductFulfillment> findFulfillmentsByProductAndStore(Long productId, Long storeId) {
    return find("productId = :productId AND storeId = :storeId",
        Parameters.with("productId", productId).and("storeId", storeId))
        .list();
  }

  /**
   * Count how many warehouses are fulfilling a product for a store
   */
  public long countWarehousesForProductInStore(Long productId, Long storeId) {
    return find("productId = :productId AND storeId = :storeId",
        Parameters.with("productId", productId).and("storeId", storeId))
        .count();
  }

  /**
   * Find all products fulfilled by a specific warehouse in a specific store
   */
  public List<ProductFulfillment> findProductsByWarehouseAndStore(String warehouseBusinessUnitCode, Long storeId) {
    return find("warehouseBusinessUnitCode = :buCode AND storeId = :storeId",
        Parameters.with("buCode", warehouseBusinessUnitCode).and("storeId", storeId))
        .list();
  }

  /**
   * Count how many different stores a warehouse is fulfilling for
   */
  public long countStoresForWarehouse(String warehouseBusinessUnitCode) {
    return find("warehouseBusinessUnitCode", warehouseBusinessUnitCode)
        .stream()
        .map(pf -> pf.storeId)
        .distinct()
        .count();
  }

  /**
   * Find all products fulfilled for a specific store
   */
  public List<ProductFulfillment> findAllFulfillmentsForStore(Long storeId) {
    return find("storeId = :storeId", Parameters.with("storeId", storeId))
        .list();
  }

  /**
   * Count how many different warehouses fulfill a specific store
   */
  public long countWarehousesForStore(Long storeId) {
    return find("storeId", storeId)
        .stream()
        .map(pf -> pf.warehouseBusinessUnitCode)
        .distinct()
        .count();
  }

  /**
   * Count how many different product types a warehouse stores
   */
  public long countProductTypesForWarehouse(String warehouseBusinessUnitCode) {
    return find("warehouseBusinessUnitCode", warehouseBusinessUnitCode)
        .stream()
        .map(pf -> pf.productId)
        .distinct()
        .count();
  }

  /**
   * Check if a specific fulfillment relationship exists
   */
  public boolean exists(Long productId, String warehouseBusinessUnitCode, Long storeId) {
    long count = find("productId = :productId AND warehouseBusinessUnitCode = :buCode AND storeId = :storeId",
        Parameters.with("productId", productId)
            .and("buCode", warehouseBusinessUnitCode)
            .and("storeId", storeId))
        .count();
    return count > 0;
  }

  /**
   * Remove fulfillment relationship
   */
  public void removeFulfillment(Long productId, String warehouseBusinessUnitCode, Long storeId) {
    delete("productId = :productId AND warehouseBusinessUnitCode = :buCode AND storeId = :storeId",
        Parameters.with("productId", productId)
            .and("buCode", warehouseBusinessUnitCode)
            .and("storeId", storeId));
  }
}
