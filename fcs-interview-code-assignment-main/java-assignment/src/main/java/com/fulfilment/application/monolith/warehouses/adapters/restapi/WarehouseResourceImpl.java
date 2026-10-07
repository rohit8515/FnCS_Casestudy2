
package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.warehouse.api.WarehouseResource;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;

import java.time.LocalDateTime;
import java.util.List;

@RequestScoped
public class WarehouseResourceImpl implements WarehouseResource {

  @Inject private WarehouseRepository warehouseRepository;

  @Override
  public List<Warehouse> listAllWarehousesUnits() {
    return warehouseRepository.getAll().stream().map(this::toWarehouseResponse).toList();
  }

  @Override
  @Transactional
  public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
    // TODO Auto-generated method stub
      com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse =
              new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();

      warehouse.businessUnitCode = data.getBusinessUnitCode();
      warehouse.location = data.getLocation();
      warehouse.capacity = data.getCapacity();
      warehouse.stock = data.getStock();
      warehouse.createdAt = LocalDateTime.now();

      warehouseRepository.create(warehouse);

      return toWarehouseResponse(warehouse);

  }



  @Override
  public Warehouse getAWarehouseUnitByID(String id) {
      com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse =
              warehouseRepository.findByBusinessUnitCode(id);
      if (warehouse == null) {
          throw new WebApplicationException(
                  "Warehouse with business unit code " + id + " does not exist.",
                  404);
      }

      return toWarehouseResponse(warehouse);
  }

  @Override
  public void archiveAWarehouseUnitByID(String id) {
      com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse = warehouseRepository.findByBusinessUnitCode(id);

      if (warehouse == null) {
          throw new WebApplicationException(
                  "Warehouse with business unit code " + id + " does not exist.",
                  404);
      }

      warehouse.archivedAt = LocalDateTime.now();

      warehouseRepository.update(warehouse);
  }

  @Override
  public Warehouse replaceTheCurrentActiveWarehouse(
      String businessUnitCode, @NotNull Warehouse data) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
        "Unimplemented method 'replaceTheCurrentActiveWarehouse'");
  }

  private Warehouse toWarehouseResponse(
      com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse) {
    var response = new Warehouse();
    response.setBusinessUnitCode(warehouse.businessUnitCode);
    response.setLocation(warehouse.location);
    response.setCapacity(warehouse.capacity);
    response.setStock(warehouse.stock);

    return response;
  }
}

