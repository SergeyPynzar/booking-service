package by.javaguru.jdmik12.bookingservice.service;

import by.javaguru.jdmik12.bookingservice.dto.BookingRequest;
import by.javaguru.jdmik12.bookingservice.dto.BookingResponseDto;
import by.javaguru.jdmik12.bookingservice.dto.BookingRequestStatusUpdateDto;
import by.javaguru.jdmik12.bookingservice.dto.ResponseDto;

public interface BookingService {

    ResponseDto createBooking(BookingRequest bookingRequest);

    BookingResponseDto getBookingByRequestId(Long requestId);

    BookingResponseDto updateBookingByRequestId(Long requestId, BookingRequestStatusUpdateDto status);

}