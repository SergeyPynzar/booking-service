package by.javaguru.jdmik12.bookingservice.service;

import by.javaguru.jdmik12.bookingservice.dto.CheckSecurityEvent;

public interface SecurityService {
    void handleSecurityCheckProcess(CheckSecurityEvent checkSecurityEvent);
}