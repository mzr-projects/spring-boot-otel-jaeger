package com.mt.demo.otel.dptinhbooyoteljaeger.services;

import com.mt.demo.otel.dptinhbooyoteljaeger.payloads.Order;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final Tracer tracer;

    @Override
    @WithSpan
    public Order createOrder(int id, String name) {

        Span span = tracer.spanBuilder("in-create-order-method")
                .setAttribute("order.id", id)
                .setAttribute("order.name", name)
                .startSpan();

        try (Scope scope = span.makeCurrent()) {
            log.info("Processing order: {}", id);

            span.addEvent("order_processed", Attributes.of(
                    AttributeKey.stringKey("order.status"), "COMPLETED"
            ));

            return new Order(id, name);
        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR, e.getMessage());
            throw e;
        } finally {
            span.end();
        }
    }
}
