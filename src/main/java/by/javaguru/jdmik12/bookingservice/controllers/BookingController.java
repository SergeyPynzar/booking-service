package by.javaguru.jdmik12.bookingservice.controllers;

import by.javaguru.jdmik12.bookingservice.dto.BookingRequest;
import by.javaguru.jdmik12.bookingservice.dto.BookingResponseDto;
import by.javaguru.jdmik12.bookingservice.dto.BookingRequestStatusUpdateDto;
import by.javaguru.jdmik12.bookingservice.dto.ResponseDto;
import by.javaguru.jdmik12.bookingservice.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/bookings")
@Slf4j
public class BookingController implements BookingControllerApi {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ResponseDto> create(@RequestBody @Valid BookingRequest bookingRequest) {
        log.debug("Received booking request: {}", bookingRequest);
        ResponseDto responseDto = bookingService.createBooking(bookingRequest);
        log.debug("Response booking request: {}", responseDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<BookingResponseDto> getByRequestId(@PathVariable(name = "requestId") Long requestId) {
        log.debug("Received get booking requestId: {}", requestId);
        BookingResponseDto bookingResponseDto = bookingService.getBookingByRequestId(requestId);
        log.debug("Response get booking request: {}", bookingResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(bookingResponseDto);
    }

    @PatchMapping("/{requestId}")
    public ResponseEntity<BookingResponseDto> updateStatus(@PathVariable Long requestId,
                                                           @RequestBody BookingRequestStatusUpdateDto bookingRequestStatusUpdateDto) {
        log.debug("Received update status booking requestId: {}", requestId);
        BookingResponseDto updateRequestStatus = bookingService.updateBookingByRequestId(requestId, bookingRequestStatusUpdateDto);
        log.debug("Response update status booking request: {}", updateRequestStatus);
        return ResponseEntity.ok(updateRequestStatus);
    }

}
