package com.shabushabu.javashop.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanId;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.extension.annotations.WithSpan;

import java.util.Random;

import com.shabushabu.javashop.shop.services.ProductService;

@Controller
@RequestMapping(value = "/")
public class HomeController {

	//Instantiate the Tracer interface 
	private static final Tracer s_tracer =
			GlobalOpenTelemetry.getTracer("javasshop.tracer");

    @Autowired
    private ProductService productService;

	@GetMapping
    public String loadPage(@ModelAttribute() Store store, Model model) {
		model.addAttribute("store", new Store());
		model.addAttribute("products", productService.getProducts("v2"));
		model.addAttribute("traceId", Span.current().getSpanContext().getTraceIdAsHexString());
		model.addAttribute("traceIdURL", APM_URL + Span.current().getSpanContext().getTraceIdAsHexString());
		return "index";
	}

	@PostMapping("/getProducts")
    public String getProducts(@ModelAttribute Store store, Model model) {
    	 
	   // Create Span
	   Span span = s_tracer.spanBuilder("getProductsController").startSpan();
	   // Put the span into the current Context
	   try (Scope scope = span.makeCurrent()) {
	     
		    // Set custom store.location and product.category attributes 
			span.setAttribute("store.location",store.getLocation());
			span.setAttribute("product.category", store.getCategory());
            
			// Do some work
		    model.addAttribute("products", productService.getProductsByCategory(loadBalanceAPIVersion(true), store.getCategory()));
			model.addAttribute("traceId", Span.current().getSpanContext().getTraceIdAsHexString());
            model.addAttribute("traceIdURL", APM_URL + Span.current().getSpanContext().getTraceIdAsHexString());
    
		} finally {
	          span.end(); // End the span
	   	}

    	  return "index";
	}

    @WithSpan
    public String loadBalanceAPIVersion(Boolean loadbalance) {
		Span span = Span.current();
		String apiVersion;

	    if (loadbalance){
			Random pickapi = new Random();
		    if(pickapi.nextBoolean()){
			    apiVersion = "v1";
			} else apiVersion = "v2";
		} else apiVersion = "v2";
			
		span.setAttribute("api.version", apiVersion);
		span.addEvent("Our Crystal Ball Chose API Version: " + apiVersion);
		return apiVersion;
    }

	static final String APM_URL = "https://app.us1.signalfx.com/#/apm/traces/";
}
