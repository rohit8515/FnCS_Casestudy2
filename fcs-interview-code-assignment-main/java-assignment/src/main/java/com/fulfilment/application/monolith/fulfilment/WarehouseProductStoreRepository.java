package com.fulfilment.application.monolith.fulfilment;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WarehouseProductStoreRepository
        implements PanacheRepository<WarehouseProductStore> {

    public boolean exists(
            Store store,
            DbWarehouse warehouse,
            Product product) {

        return count(
                "store = ?1 and warehouse = ?2 and product = ?3",
                store,
                warehouse,
                product)
                > 0;
    }

    public boolean existsForStoreAndWarehouse(
            Store store,
            DbWarehouse warehouse) {

        return count(
                "store = ?1 and warehouse = ?2",
                store,
                warehouse)
                > 0;
    }

    public boolean existsForWarehouseAndProduct(
            DbWarehouse warehouse,
            Product product) {

        return count(
                "warehouse = ?1 and product = ?2",
                warehouse,
                product)
                > 0;
    }

    public long countWarehousesForStore(Store store) {
        return getEntityManager()
                .createQuery(
                        """
                        select count(distinct w.warehouse.id)
                        from WarehouseProductStore w
                        where w.store = :store
                        """,
                        Long.class)
                .setParameter("store", store)
                .getSingleResult();
    }

    public long countProductsForWarehouse(DbWarehouse warehouse) {
        return getEntityManager()
                .createQuery(
                        """
                        select count(distinct w.product.id)
                        from WarehouseProductStore w
                        where w.warehouse = :warehouse
                        """,
                        Long.class)
                .setParameter("warehouse", warehouse)
                .getSingleResult();
    }

    public long countWarehousesForStoreAndProduct(
            Store store,
            Product product) {

        return getEntityManager()
                .createQuery(
                        """
                        select count(distinct w.warehouse.id)
                        from WarehouseProductStore w
                        where w.store = :store
                          and w.product = :product
                        """,
                        Long.class)
                .setParameter("store", store)
                .setParameter("product", product)
                .getSingleResult();
    }
}
