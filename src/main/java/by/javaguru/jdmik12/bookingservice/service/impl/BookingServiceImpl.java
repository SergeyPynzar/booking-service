package by.javaguru.jdmik12.bookingservice.service.impl;

import by.javaguru.jdmik12.bookingservice.mapper.BookingMapper;
import by.javaguru.jdmik12.bookingservice.dto.BookingRequest;
import by.javaguru.jdmik12.bookingservice.dto.enums.BookingStatus;
import by.javaguru.jdmik12.bookingservice.exceptions.DataIntegrationNotFoundException;
import by.javaguru.jdmik12.bookingservice.exceptions.ServiceIntegrationException;
import by.javaguru.jdmik12.bookingservice.dto.BookingResponseDto;
import by.javaguru.jdmik12.bookingservice.dto.BookingRequestStatusUpdateDto;
import by.javaguru.jdmik12.bookingservice.dto.ResponseDto;
import by.javaguru.jdmik12.bookingservice.outbox.CommandOutboxFactory;
import by.javaguru.jdmik12.bookingservice.repository.BookingRepository;
import by.javaguru.jdmik12.bookingservice.model.Bookings;
import by.javaguru.jdmik12.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static by.javaguru.jdmik12.bookingservice.dto.enums.OutboxStatus.PENDING;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final static String DEFAULT_REQUEST_MESSAGE = "Ваша заявка отправлена в обработку!";
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CommandOutboxFactory commandOutboxFactory;

    @Override
    @Transactional
    public ResponseDto createBooking(BookingRequest bookingRequest) {
        return Optional.ofNullable(bookingRequest)
                .map(this::convertAndSaveRequest)
                .map(this::saveRequestOutbox)
                .orElseThrow(ServiceIntegrationException::new);
    }

    private Bookings convertAndSaveRequest(BookingRequest dto) {
        Bookings request = bookingMapper.toBooking(
                dto,
                BookingStatus.CREATED
        );
        return bookingRepository.save(request);
    }

    private ResponseDto saveRequestOutbox(Bookings request) {
        try {
            commandOutboxFactory.buildBookingCommandOutbox(request, PENDING);
            return new ResponseDto(request.getId(), DEFAULT_REQUEST_MESSAGE);
        } catch (Exception exception) {
            log.error("Ошибка отправки сообщения о бронировании для заявки ID: {}", request.getId(), exception);
            throw new ServiceIntegrationException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto getBookingByRequestId(Long requestId) {
        return bookingMapper.toDto(bookingRepository
                .findById(requestId).orElseThrow(DataIntegrationNotFoundException::new));
    }

    @Override
    @Transactional
    public BookingResponseDto updateBookingByRequestId(Long requestId, BookingRequestStatusUpdateDto bookingRequestStatusUpdateDto) {
        Bookings bookings = bookingRepository
                .findById(requestId).orElseThrow(DataIntegrationNotFoundException::new);
        bookings.setStatus(bookingRequestStatusUpdateDto.status());

        return bookingMapper.toDto(bookingRepository.save(bookings));
    }

}
