package com.fulfilment.application.monolith.stores;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class LegacyStoreManagerGatewayTest {
    @Test
    void testWriteToFile() throws Exception {

        // Arrange
        Store store = new Store();
        store.name = "TestStore";
        store.quantityProductsInStock = 50;

        LegacyStoreManagerGateway service = new LegacyStoreManagerGateway();

        // Access private method using reflection
        Method method = LegacyStoreManagerGateway.class.getDeclaredMethod("writeToFile", Store.class);
        method.setAccessible(true);

        // Act & Assert
        assertDoesNotThrow(() -> {
            method.invoke(service, store);
        });
    }

}
