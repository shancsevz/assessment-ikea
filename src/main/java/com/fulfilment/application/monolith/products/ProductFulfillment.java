package com.fulfilment.application.monolith.products;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "product_fulfillment",
    uniqueConstraints = {
      @UniqueConstraint(columnNames = {"productId", "warehouseBusinessUnitCode", "storeId"},
          name = "uk_product_warehouse_store")
    })
public class ProductFulfillment extends PanacheEntity {

  @Column(nullable = false)
  public Long productId;

  @Column(nullable = false, length = 40)
  public String warehouseBusinessUnitCode;

  @Column(nullable = false)
  public Long storeId;

  public ProductFulfillment() {}

  public ProductFulfillment(Long productId, String warehouseBusinessUnitCode, Long storeId) {
    this.productId = productId;
    this.warehouseBusinessUnitCode = warehouseBusinessUnitCode;
    this.storeId = storeId;
  }

  @Override
  public String toString() {
    return "ProductFulfillment{" +
        "id=" + id +
        ", productId=" + productId +
        ", warehouseBusinessUnitCode='" + warehouseBusinessUnitCode + '\'' +
        ", storeId=" + storeId +
        '}';
  }
}

