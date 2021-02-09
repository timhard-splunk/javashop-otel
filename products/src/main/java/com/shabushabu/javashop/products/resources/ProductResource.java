package com.shabushabu.javashop.products.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.shabushabu.javashop.products.services.ProductService;
import com.shabushabu.javashop.products.model.Product;


import io.opentelemetry.api.trace.Span;
import io.opentelemetry.extension.annotations.WithSpan;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.Random;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    private ProductService productService;

    @Inject
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @GET
    @Timed
    @Path("v1")
    @WithSpan
    public Response getAllProducts() { 
        reallyLongLookup();
        Span span = Span.current();
        span.addEvent("API v1 is deprecated. Please use API v2");
        return Response.status(200)
                .entity(productService.getAllProducts())
                .build();
    }

    @GET
    @Timed
    @Path("v2")
    @WithSpan
    public Response getAllProductV2() {
        return Response.status(200)
                .entity(productService.getAllProducts())
                .build();
    }

    public static void reallyLongLookup() {
        Random random = new Random();
        int sleepy = random.nextInt(5000 - 3000) + 3000;
        try{
        Thread.sleep(sleepy);
        } catch (Exception e){}
    }
}
