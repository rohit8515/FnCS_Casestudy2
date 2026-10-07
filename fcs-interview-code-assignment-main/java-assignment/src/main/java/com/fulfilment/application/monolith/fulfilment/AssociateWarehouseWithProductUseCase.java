package com.fulfilment.application.monolith.fulfilment;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AssociateWarehouseWithProductUseCase {

    private final WarehouseProductStoreRepository repository;

    public AssociateWarehouseWithProductUseCase(
            WarehouseProductStoreRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void associate(
            Store store,
            DbWarehouse warehouse,
            Product product) {

        // Same association already exists.
        if (repository.exists(store, warehouse, product)) {
            return;
        }

        /*
         * RULE 1
         *
         * A Product can have at most 2 different Warehouses
         * for the same Store.
         */
        long warehousesForProduct =
                repository.countWarehousesForStoreAndProduct(
                        store,
                        product);

        if (warehousesForProduct >= 2) {
            throw new IllegalArgumentException(
                    "A product can be fulfilled by a maximum of 2 warehouses per store.");
        }

        /*
         * RULE 2
         *
         * A Store can use at most 3 different Warehouses.
         *
         * Only count the warehouse if this Store doesn't
         * already use it.
         */
        if (!repository.existsForStoreAndWarehouse(store, warehouse)) {

            long warehousesForStore =
                    repository.countWarehousesForStore(store);

            if (warehousesForStore >= 3) {
                throw new IllegalArgumentException(
                        "A store can be fulfilled by a maximum of 3 warehouses.");
            }
        }

        /*
         * RULE 3
         *
         * A Warehouse can store at most 5 different Products.
         *
         * Only count the product if this Warehouse doesn't
         * already store it.
         */
        if (!repository.existsForWarehouseAndProduct(warehouse, product)) {

            long productsForWarehouse =
                    repository.countProductsForWarehouse(warehouse);

            if (productsForWarehouse >= 5) {
                throw new IllegalArgumentException(
                        "A warehouse can store a maximum of 5 different products.");
            }
        }

        repository.persist(
                new WarehouseProductStore(
                        store,
                        warehouse,
                        product));
    }
}
