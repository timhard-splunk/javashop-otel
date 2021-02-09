# Splunk APM: Span Attributes 101

Goals:

* Demonstrate OpenTelemetry span enrichment capabilities using span attributes
* Review OpenTelemetry span attribute code examples in a Java based micro-service application
* Demonstrate Splunk APM's ability to use span tags to filter
* Demonstrate Splunk APM's ability to use a tag to search for an individual or group of transactions.

In OpenTelemetry an [attribute](https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/common/common.md#attributes) is a key-value pair which can be utilized to enrich a span. The attribute key must be a non-null, non-empty string. The attribute value can be either a primitive type (string, boolean, double precision floating point (IEEE 754-1985) or signed 64 bit integer.) or an array of homogenous primitive types, meaning the array must not contain values of different types.

There are a number of span attributes that are collected by default via auto-instrumentation. These vary by language and framework but are typically well-known (e.g. http.status_code). Additionally, when using the [Splunk distribution of OpenTelemetry Collector](https://github.com/signalfx/splunk-otel-collector) or the [SignalFx Smart Agent](https://github.com/signalfx/signalfx-agent) to collect traces, additional environment context is added the spans automatically (i.e. host, kubernetes_pod_name, os.description) enabling corelation with other sources such as infrastructure. 

Utilizing custom span attributes enable developers to include context that is specific to the application, service, environment, transaction, et cetera. 

Because Splunk APM is collecting, analyzing, and retaining :100: 100% :100: of the traces, span attributes can be used to filter, group, and search for specific transactions. A few examples of custom attributes include usernames, product or transaction IDs, application/service versions, and so-on. 

**TL;DR** Span attributes enrich application traces with valuable context about the discrete transaction they are associated with. Because Splunk APM is collecting, analyzing, and retaining :100: 100% :100: of the traces, these attributes can be used to filter, group, and search for individual or groups of transactions significantly increasing troubleshooting efficiency. 

In this overview we will:
1. Review how to add custom attributes to spans in a Java application
2. Deploy a micro-service application via Docker Compose
3. Execute transactions against the applications and observe slowness
4. Use Splunk APM and the custom attributes to identify the root cause of the slowness.
5. Use Splunk APM to search for a specific transaction using a span attribute. 

---

## Requirements: 
System Dependencies:
* Windows or Linux
* Git
* Docker
* Docker Compose
* IDE or Text Editor for code review
* Web Browser

Optional:
> To build the project from source 
* Java 8 or later
* Maven

## Prerequisites:
Please ensure you have a Splunk Infrastructure Monitoring and APM environment. To make the overview as simple as possible we will be using prebuilt docker images deployed with docker-compose. If you would like to modify the code you will have to build new docker images that can be run locally or pushed to a repo of your choosing. 

## 1: Review Span Attributes Code

>TODO Provide details of code snippets. 


```java
// Instantiate a Tracer object 
private static final Tracer s_tracer =
        GlobalOpenTelemetry.getTracer("javasshop.tracer");
```

Creating span via the s_tracer object
```java
@PostMapping("/getProducts")
public String getProducts(@ModelAttribute Store store, Model model) {
    	 
    // Create Span
    Span span = s_tracer.spanBuilder("getProductsController").startSpan();
    // Put the span into the current Context
    try (Scope scope = span.makeCurrent()) {
	     
        // Set custom store.location and product.category attributes 
	span.setAttribute("store.location",store.getLocation());
	span.setAttribute("product.category", store.getCategory());
        
        //  Do some work
	model.addAttribute("products", productService.getProductsByCategory(loadBalanceAPIVersion(true), store.getCategory()));
        model.addAttribute("traceId", APM_URL + Span.current().getSpanContext().getTraceIdAsHexString());
		    
	} finally { 
	    span.end(); // End the span 
	}
    	
    return "index";
}
```

creating a span using the ```@WithSpan``` annotation

```java
@WithSpan //Use the @WithSpan annotation to automatically instrument the method. 
public String loadBalanceAPIVersion(Boolean loadbalance) {
    
    // Get the current span so that attributes can be added to it.
    Span span = Span.current();
    String apiVersion;

    if (loadbalance){
        Random pickapi = new Random();
        if(pickapi.nextBoolean()){
            apiVersion = "v1";
	} else apiVersion = "v2";
    } else apiVersion = "v2";
        
    //Set a custom attribute and event 
    span.setAttribute("api.version", apiVersion);
    span.addEvent("Our Crystal Ball Chose API Version: " + apiVersion);

    return apiVersion;
}

```

## 2: Deploy Buttercup Games Inventory Application :horse:
>TODO Add an instance of the smart agent to the docker-compose configuration.

From the root directory of the project run:

```
docker-compose up -d 
```
The `-d` flag runs the containers in detached mode.  
If everything is successful, you should receive the following output:
```bash
$ docker-compose up -d
Starting javashop-otel_products_1 ... done
Starting javashop-otel_stock_1    ... done
Recreating javashop-otel_shop_1   ... done
```

## 3: Execute Transactions
The ```shop``` service is the entrypoint for the Buttercup Games Inventory application. This service exposes port ```8010```. You can access the application from your favorite browser at 
```
http://localhost:8010
``` 
From the home page enter any value in the ```Store Location``` field, chose a ```Category```, and hit ```Submit```. You can do this as many times as you would like. Each time the submit button is hit, a new trace will be generated. 

The values used for ```Store Location``` and ```Category``` will be the values that are applied to the custom attributes ```store.location``` and ```product.category``` respectively.

You should notice about half of the time when clicking the submit button the application will hang for few seconds. This is user experience is unacceptable so next we will investigate the problem using Splunk APM.

## 4: Troubleshoot Slow Transactions with Splunk APM

Within SIM & APM navigate to the Troubleshooting view on the APM tab. Filter the traces down to the transaction that were executed in the previous step by selecting ```shop:/getProducts``` from the Workflow dropdown.

![Buttercup Service Overview](/images/buttercup_service_overview.png)

Right away you should notice the high latency between the ```External Client```, ```shop```, and ```products``` services. 

With the ```shop``` service selected apply the ```store.location``` and ```product.category``` as a breakdown. This allows you view the service topology using the custom attributes. Notice that all of the locations and categories are impacted by the high latency issue. We have immediately identified that the issue is impacting all stores and products so the fault domain must be somewhere else. 

Breakdown using ```store.location```
![Store Location Breakdown](/images/store_location_breakdown.png)

Breakdown using ```product.category```
![Product Category Breakdown](/images/product_category_breakdown.png)

Now breakdown the ```shop``` service by the ```api.version``` attribute.  

![API Version Breakdown](/images/api_version_breakdown.png)

Alas! It appears we have found the issue! All of the transactions that are using the ```v1``` version of the API are experiencing extremely high latency. We can further corroborate this by applying the ```Endpoint``` breakdown to the ```product``` service. ```Endpoint``` is an example of a default span attribute which Splunk collects automatically. Every transaction hitting the ```/api/v1``` endpoint of the ```product``` service is experiencing really high latency. 

We could have identified the bottleneck even faster using the Tag Spotlight view which gives a breakdown of all the tags for a given service broken out by failure rate and latency. Notice the difference in response time between the API versions.

![Tag Spotlight](/images/tag_spotlight.png)

## 5: Search for Individual Transactions with Splunk APM
We've already seen a few examples of how Splunk APM can utilize both default and custom attributes to filter and group services streamlining the troubleshooting process by providing much needed critical context on a per-trace basis. But we can take that a step further.

Because Splunk APM is collecting, analyzing, and retaining :100: 100% :100: of the traces, we can use those attributes to search for an individual or group of transactions.  

Here we will cover two examples of searching for a specific transaction:
* Using a span attribute value
* Using the traceId

From the Troubleshooting view on the APM tab, select the ```Tags``` field across the top. Select the ```store.location``` attribute. The fields will update to the values that are available for this attribute. You can select from the populated list, or begin typing to filter the list down to the selection of your choice. Once selected the service map will update to only include the traces that contain the value you selected. 

![Tag Filter](/images/tag_filter.png)

In the bottom right corner select ```Show Traces``` to view the individual transactions associated with the attribute you have filtered to.

![Filtered Traces](/images/filtered_traces.png)

>TODO traceID search example (update homepage to include traceID)



# Additional Resources
>TODO Provide links to additional resources, examples, etc 
