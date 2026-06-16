package by.javaguru.jdmik12.bookingservice.outbox.model.enums;

import lombok.Getter;

@Getter
public enum PayloadType {
    ACCOUNTING("by.javaguru.jdmik12.common.accounting.message.command.AllocateBudgetCommand"),
    SECURITY("by.javaguru.jdmik12.common.security.message.command.CheckSecurityCommand"),
    RESOURCES("by.javaguru.jdmik12.common.resources.message.command.CheckResourcesCommand"),
    PROFILER("by.javaguru.jdmik12.common.profiler.message.command.CurriculumVitaeCreateCommand"),
    EDUCATION("by.javaguru.jdmik12.common.profiler.message.command.EducationCreateCommand"),
    NOTIFICATION("by.javaguru.jdmik12.common.notification.message.command.NotificationCommand");

    private final String payloadType;

    PayloadType(String payloadType) {
        this.payloadType = payloadType;
    }

}
