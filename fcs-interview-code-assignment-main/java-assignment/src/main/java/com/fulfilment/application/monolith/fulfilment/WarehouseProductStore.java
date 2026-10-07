package com.fulfilment.application.monolith.fulfilment;

import com.fulfilment.application.monolith.products.Product;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "warehouse_product_store",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"store_id", "warehouse_id", "product_id"})
        })
public class WarehouseProductStore {

    @Id
    @GeneratedValue
    public Long id;

    @ManyToOne
    public Store store;

    @ManyToOne
    public DbWarehouse warehouse;

    @ManyToOne
    public Product product;

    public WarehouseProductStore() {}

    public WarehouseProductStore(
            Store store,
            DbWarehouse warehouse,
            Product product) {
        this.store = store;
        this.warehouse = warehouse;
        this.product = product;
    }
}