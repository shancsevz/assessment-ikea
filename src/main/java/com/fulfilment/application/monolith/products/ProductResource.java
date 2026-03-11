package com.fulfilment.application.monolith.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.List;
import org.jboss.logging.Logger;

@Path("product")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class ProductResource {

  @Inject ProductRepository productRepository;
  @Inject ProductFulfillmentRepository fulfillmentRepository;
  @Inject AddProductFulfillmentUseCase addFulfillmentUseCase;

  private static final Logger LOGGER = Logger.getLogger(ProductResource.class.getName());

  @GET
  public List<Product> get() {
    return productRepository.listAll(Sort.by("name"));
  }

  @GET
  @Path("{id}")
  public Product getSingle(Long id) {
    Product entity = productRepository.findById(id);
    if (entity == null) {
      throw new WebApplicationException("Product with id of " + id + " does not exist.", 404);
    }
    return entity;
  }

  @POST
  @Transactional
  public Response create(Product product) {
    if (product.id != null) {
      throw new WebApplicationException("Id was invalidly set on request.", 422);
    }

    productRepository.persist(product);
    return Response.ok(product).status(201).build();
  }

  @PUT
  @Path("{id}")
  @Transactional
  public Product update(Long id, Product product) {
    if (product.name == null) {
      throw new WebApplicationException("Product Name was not set on request.", 422);
    }

    Product entity = productRepository.findById(id);

    if (entity == null) {
      throw new WebApplicationException("Product with id of " + id + " does not exist.", 404);
    }

    entity.name = product.name;
    entity.description = product.description;
    entity.price = product.price;
    entity.stock = product.stock;

    productRepository.persist(entity);

    return entity;
  }

  @DELETE
  @Path("{id}")
  @Transactional
  public Response delete(Long id) {
    Product entity = productRepository.findById(id);
    if (entity == null) {
      throw new WebApplicationException("Product with id of " + id + " does not exist.", 404);
    }
    productRepository.delete(entity);
    return Response.status(204).build();
  }

  // Fulfillment endpoints
  @GET
  @Path("{productId}/fulfillment")
  public List<ProductFulfillment> getProductFulfillments(Long productId) {
    Product product = productRepository.findById(productId);
    if (product == null) {
      throw new WebApplicationException("Product with id of " + productId + " does not exist.", 404);
    }
    return fulfillmentRepository.find("productId", productId).list();
  }

  @GET
  @Path("{productId}/fulfillment/store/{storeId}")
  public List<ProductFulfillment> getProductFulfillmentsForStore(Long productId, Long storeId) {
    Product product = productRepository.findById(productId);
    if (product == null) {
      throw new WebApplicationException("Product with id of " + productId + " does not exist.", 404);
    }
    return fulfillmentRepository.findFulfillmentsByProductAndStore(productId, storeId);
  }

  @POST
  @Path("{productId}/fulfillment")
  @Transactional
  public Response addFulfillment(Long productId, ProductFulfillmentRequest request) {
    if (request.warehouseBusinessUnitCode == null || request.warehouseBusinessUnitCode.isBlank()) {
      throw new WebApplicationException("warehouseBusinessUnitCode is required.", 422);
    }
    if (request.storeId == null) {
      throw new WebApplicationException("storeId is required.", 422);
    }

    try {
      addFulfillmentUseCase.addFulfillment(productId, request.warehouseBusinessUnitCode, request.storeId);
      ProductFulfillment fulfillment = fulfillmentRepository
          .find("productId = :pid AND warehouseBusinessUnitCode = :wbc AND storeId = :sid",
              io.quarkus.panache.common.Parameters
                  .with("pid", productId)
                  .and("wbc", request.warehouseBusinessUnitCode)
                  .and("sid", request.storeId))
          .firstResult();
      return Response.ok(fulfillment).status(201).build();
    } catch (IllegalArgumentException e) {
      throw new WebApplicationException(e.getMessage(), 400);
    }
  }

  @DELETE
  @Path("{productId}/fulfillment/warehouse/{warehouseBusinessUnitCode}/store/{storeId}")
  @Transactional
  public Response removeFulfillment(Long productId, String warehouseBusinessUnitCode, Long storeId) {
    try {
      addFulfillmentUseCase.removeFulfillment(productId, warehouseBusinessUnitCode, storeId);
      return Response.status(204).build();
    } catch (IllegalArgumentException e) {
      throw new WebApplicationException(e.getMessage(), 400);
    }
  }

  // Request/Response DTOs
  public static class ProductFulfillmentRequest {
    public String warehouseBusinessUnitCode;
    public Long storeId;

    public ProductFulfillmentRequest() {}

    public ProductFulfillmentRequest(String warehouseBusinessUnitCode, Long storeId) {
      this.warehouseBusinessUnitCode = warehouseBusinessUnitCode;
      this.storeId = storeId;
    }
  }

  @Provider
  public static class ErrorMapper implements ExceptionMapper<Exception> {

    @Inject ObjectMapper objectMapper;

    @Override
    public Response toResponse(Exception exception) {
      LOGGER.error("Failed to handle request", exception);

      int code = 500;
      if (exception instanceof WebApplicationException) {
        code = ((WebApplicationException) exception).getResponse().getStatus();
      }

      ObjectNode exceptionJson = objectMapper.createObjectNode();
      exceptionJson.put("exceptionType", exception.getClass().getName());
      exceptionJson.put("code", code);

      if (exception.getMessage() != null) {
        exceptionJson.put("error", exception.getMessage());
      }

      return Response.status(code).entity(exceptionJson).build();
    }
  }
}
