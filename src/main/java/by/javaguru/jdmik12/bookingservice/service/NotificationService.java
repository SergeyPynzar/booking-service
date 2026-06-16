package by.javaguru.jdmik12.bookingservice.service;

import by.javaguru.jdmik12.bookingservice.model.Bookings;

public interface NotificationService {

    void sendMessage(Bookings bookings);

}