package com.mt.demo.otel.dptinhbooyoteljaeger.configs;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterRegistryConfig;
import io.micrometer.core.instrument.observation.DefaultMeterObservationHandler;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OpenTelemetryConfig {

    private final MeterRegistry meterRegistry;

    @Bean
    public Tracer tracer(OpenTelemetry openTelemetry) {
        return openTelemetry.getTracer(getClass().getName());
    }

    @Bean
    ObservationHandler<Observation.Context> syncHandler() {
        // This handler helps correlate metrics with traces
        return new DefaultMeterObservationHandler(meterRegistry);
    }
}
