package com.mt.demo.otel.dptinhbooyoteljaeger.controllers;

import com.mt.demo.otel.dptinhbooyoteljaeger.payloads.Order;
import com.mt.demo.otel.dptinhbooyoteljaeger.payloads.OrderResponse;
import com.mt.demo.otel.dptinhbooyoteljaeger.services.OrderService;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final Tracer tracer;
    private final MeterRegistry meterRegistry;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder() {
        meterRegistry.counter("order.created", "endpoint", "hello").increment();

        Span span = tracer.spanBuilder("create-order-controller")
                .setAttribute("http.method", "POST")
                .setAttribute("http.route", "/order/create")
                .startSpan();

        try (Scope scope = span.makeCurrent()) {
            log.info("Received order creation request");
            Order order = orderService.createOrder(1, "demo-order");
            return ResponseEntity.ok(new OrderResponse("200", "Order with name: " + order.name() + "created"));
        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            span.end();
        }
    }
}
