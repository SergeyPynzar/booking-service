package by.javaguru.jdmik12.bookingservice.outbox.executor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestIdExtractor {
    private final ObjectMapper objectMapper;

    public Long extract(Object message) {
        if (!(message instanceof StreamingCommand command)) return null;
        try {
            JsonNode payloadNode = objectMapper.valueToTree(command.payload());
            JsonNode innerNode = parseInnerPayload(payloadNode);
            return extractId(innerNode);
        } catch (Exception ex) {
            log.debug("Failed to extract requestId from message: {}", message, ex);
            return null;
        }
    }

    private JsonNode parseInnerPayload(JsonNode payloadNode) throws JsonProcessingException {
        return objectMapper.readTree(payloadNode.asText());
    }

    private Long extractId(JsonNode node) {
        if (node.has("requestId")) return node.get("requestId").asLong();
        if (node.has("requestMessageId")) return node.get("requestMessageId").asLong();
        return null;
    }

}
