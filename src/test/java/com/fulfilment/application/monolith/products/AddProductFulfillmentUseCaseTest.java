package com.fulfilment.application.monolith.products;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class AddProductFulfillmentUseCaseTest {

    @Inject
    AddProductFulfillmentUseCase useCase;
    @Inject
    ProductRepository productRepository;
    @Inject
    ProductFulfillmentRepository fulfillmentRepository;

    private Long productId1;
    private Long productId2;
    private Long productId3;
    private Long productId4;
    private Long productId5;
    private Long productId6;
    private Long storeId1;
    private Long storeId2;
    private String warehouse1;
    private String warehouse2;
    private String warehouse3;
    private String warehouse4;

    @BeforeEach
    @Transactional
    public void setUp() {
        // Clean up existing fulfillments
        fulfillmentRepository.deleteAll();

        // Create test products
        Product p1 = new Product("TEST-PRODUCT-1");
        productRepository.persist(p1);
        productId1 = p1.id;

        Product p2 = new Product("TEST-PRODUCT-2");
        productRepository.persist(p2);
        productId2 = p2.id;

        Product p3 = new Product("TEST-PRODUCT-3");
        productRepository.persist(p3);
        productId3 = p3.id;

        Product p4 = new Product("TEST-PRODUCT-4");
        productRepository.persist(p4);
        productId4 = p4.id;

        Product p5 = new Product("TEST-PRODUCT-5");
        productRepository.persist(p5);
        productId5 = p5.id;

        Product p6 = new Product("TEST-PRODUCT-6");
        productRepository.persist(p6);
        productId6 = p6.id;

        // Use existing stores and warehouses from the database
        storeId1 = 1L;
        storeId2 = 2L;
        warehouse1 = "MWH.001";
        warehouse2 = "MWH.012";
        warehouse3 = "MWH.023";
        warehouse4 = "MWH.004"; // Non-existent warehouse (but that's OK for this test)
    }

    @Test
    @DisplayName("Should allow same warehouse to fulfill multiple products in same store")
    @Transactional
    public void testAddFulfillmentSameWarehouseMultipleProducts() {
        // First fulfillment: warehouse1 fulfills productId1 in storeId1
        useCase.addFulfillment(productId1, warehouse1, storeId1);
        assertTrue(fulfillmentRepository.exists(productId1, warehouse1, storeId1));

        // Second fulfillment: same warehouse1 fulfills productId2 in same storeId1
        // This should succeed because warehouse1 is not new, just fulfilling a different product
        assertDoesNotThrow(() ->
                useCase.addFulfillment(productId2, warehouse1, storeId1)
        );

        // Verify both fulfillments exist
        assertTrue(fulfillmentRepository.exists(productId1, warehouse1, storeId1));
        assertTrue(fulfillmentRepository.exists(productId2, warehouse1, storeId1));

        // Verify warehouse count for store is still 1 (not 2)
        long warehouseCount = fulfillmentRepository.countWarehousesForStore(storeId1);
        assertEquals(1, warehouseCount);

        // Verify product count for warehouse is 2
        long productCount = fulfillmentRepository.countProductTypesForWarehouse(warehouse1);
        assertEquals(2, productCount);
    }

    @AfterEach
    @Transactional
    public void tearDown() {
        // Clean up after each test - wrap in try-catch to avoid ARC proxy issues
        try {
            fulfillmentRepository.deleteAll();
        } catch (Exception e) {
            // Ignore any errors during cleanup
        }

    }
}


