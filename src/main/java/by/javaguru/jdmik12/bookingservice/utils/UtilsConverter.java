package by.javaguru.jdmik12.bookingservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UtilsConverter {
    private final ObjectMapper objectMapper;
    private final Tracer tracer;

    @Named("toJson")
    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON: " + object.getClass().getSimpleName(), e);
        }
    }

    @Named("fromJson")
    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON to: " + clazz.getSimpleName(), e);
        }
    }

    @Named("updateRetryCount")
    public Integer fromJson(Integer retryCount) {
        return retryCount + 1;
    }


    @Named("getTraceIdFromTracer")
    public String getTraceIdFromTracer(Long id) {
        log.debug("tracer traceId id: {}", id);
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            return currentSpan.context().traceId();
        }
        return null;
    }

    @Named("getSpanIdFromTracer")
    public String getSpanIdFromTracer(Long id) {
        log.debug("tracer spanId id: {}", id);
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            return currentSpan.context().spanId();
        }
        return null;
    }
}
