package com.fulfilment.application.monolith.products;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AddProductFulfillmentUseCase {

  private static final int MAX_WAREHOUSES_PER_PRODUCT_PER_STORE = 2;
  private static final int MAX_WAREHOUSES_PER_STORE = 3;
  private static final int MAX_PRODUCT_TYPES_PER_WAREHOUSE = 5;

  @Inject ProductRepository productRepository;
  @Inject ProductFulfillmentRepository fulfillmentRepository;

  /**
   * Adds a warehouse as a fulfillment unit for a product in a store.
   * 
   * @param productId the product ID
   * @param warehouseBusinessUnitCode the warehouse business unit code
   * @param storeId the store ID
   * @throws IllegalArgumentException if any constraint is violated
   */
  @Transactional
  public void addFulfillment(Long productId, String warehouseBusinessUnitCode, Long storeId) {
    // Validate product exists
    Product product = productRepository.findById(productId);
    if (product == null) {
      throw new IllegalArgumentException("Product with id " + productId + " does not exist.");
    }

    // Check if fulfillment already exists
    if (fulfillmentRepository.exists(productId, warehouseBusinessUnitCode, storeId)) {
      throw new IllegalArgumentException(
          "Fulfillment already exists for product " + productId +
          ", warehouse " + warehouseBusinessUnitCode +
          ", and store " + storeId);
    }

    // Constraint 1: Check max warehouses per product per store
    long warehouseCountForProduct = fulfillmentRepository.countWarehousesForProductInStore(productId, storeId);
    if (warehouseCountForProduct >= MAX_WAREHOUSES_PER_PRODUCT_PER_STORE) {
      throw new IllegalArgumentException(
          "Product " + productId + " already has the maximum (" + MAX_WAREHOUSES_PER_PRODUCT_PER_STORE +
          ") number of warehouses fulfilling it in store " + storeId);
    }

    // Constraint 2: Check max warehouses per store
    // Count distinct warehouses already fulfilling products in this store
    long warehouseCountForStore = fulfillmentRepository.countWarehousesForStore(storeId);
    // Check if adding a new warehouse would exceed the limit
    boolean isNewWarehouse = fulfillmentRepository.findProductsByWarehouseAndStore(warehouseBusinessUnitCode, storeId).isEmpty();
    if (isNewWarehouse && warehouseCountForStore >= MAX_WAREHOUSES_PER_STORE) {
      throw new IllegalArgumentException(
          "Store " + storeId + " already has the maximum (" + MAX_WAREHOUSES_PER_STORE +
          ") number of warehouses fulfilling it");
    }

    // Constraint 3: Check max product types per warehouse
    long productTypeCountForWarehouse = fulfillmentRepository.countProductTypesForWarehouse(warehouseBusinessUnitCode);
    // Check if adding a new product type would exceed the limit
    boolean isNewProductType = !fulfillmentRepository.findProductsByWarehouseAndStore(warehouseBusinessUnitCode, storeId)
        .stream()
        .anyMatch(pf -> pf.productId.equals(productId));
    if (isNewProductType && productTypeCountForWarehouse >= MAX_PRODUCT_TYPES_PER_WAREHOUSE) {
      throw new IllegalArgumentException(
          "Warehouse " + warehouseBusinessUnitCode + " already stores the maximum (" + MAX_PRODUCT_TYPES_PER_WAREHOUSE +
          ") number of product types");
    }

    // All validations passed, create the fulfillment
    ProductFulfillment fulfillment = new ProductFulfillment(productId, warehouseBusinessUnitCode, storeId);
    fulfillmentRepository.persist(fulfillment);
  }

  /**
   * Removes a warehouse as a fulfillment unit for a product in a store.
   * 
   * @param productId the product ID
   * @param warehouseBusinessUnitCode the warehouse business unit code
   * @param storeId the store ID
   * @throws IllegalArgumentException if the fulfillment doesn't exist
   */
  @Transactional
  public void removeFulfillment(Long productId, String warehouseBusinessUnitCode, Long storeId) {
    if (!fulfillmentRepository.exists(productId, warehouseBusinessUnitCode, storeId)) {
      throw new IllegalArgumentException(
          "Fulfillment does not exist for product " + productId +
          ", warehouse " + warehouseBusinessUnitCode +
          ", and store " + storeId);
    }

    fulfillmentRepository.removeFulfillment(productId, warehouseBusinessUnitCode, storeId);
  }
}

