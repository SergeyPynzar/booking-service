package by.javaguru.jdmik12.bookingservice.outbox.mapper;

import by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus;
import by.javaguru.jdmik12.bookingservice.outbox.model.Outbox;
import by.javaguru.jdmik12.bookingservice.outbox.model.enums.PayloadType;
import by.javaguru.jdmik12.bookingservice.utils.UtilsConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UtilsConverter.class})
public interface OutboxMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestMessageId", ignore = true)
    @Mapping(target = "payloadType", source = "payloadType")
    @Mapping(target = "payload", source = "checkSecurityCommand", qualifiedByName = "toJson")
    @Mapping(target = "status", source = "outboxStatus")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "retryCount", source = "retryCount", qualifiedByName = "updateRetryCount")
    @Mapping(target = "processedAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "traceId", source = "checkSecurityCommand.requestId", qualifiedByName = "getTraceIdFromTracer")
    @Mapping(target = "spanId", source = "checkSecurityCommand.requestId", qualifiedByName = "getSpanIdFromTracer")
    void toOutboxCheckSecurityCommand(@MappingTarget Outbox outbox, CheckSecurityCommand checkSecurityCommand,
                                      PayloadType payloadType, OutboxStatus outboxStatus, Integer retryCount);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestMessageId", ignore = true)
    @Mapping(target = "payloadType", ignore = true)
    @Mapping(target = "payload", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "traceId", ignore = true)
    @Mapping(target = "spanId", ignore = true)
    @Mapping(target = "retryCount", source = "retryCount", qualifiedByName = "updateRetryCount")
    @Mapping(target = "processedAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "status", source = "outboxStatus")
    void toOutboxUpdate(@MappingTarget Outbox outbox, Integer retryCount, OutboxStatus outboxStatus);

}
