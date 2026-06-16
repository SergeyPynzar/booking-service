package by.javaguru.jdmik12.bookingservice.mapper;

import by.javaguru.jdmik12.bookingservice.dto.BookingRequest;
import by.javaguru.jdmik12.bookingservice.dto.BookingResponseDto;
import by.javaguru.jdmik12.bookingservice.dto.enums.BookingStatus;
import by.javaguru.jdmik12.bookingservice.model.Bookings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(bookingStatus.name())")
    @Mapping(target = "specialRequests", source = "bookingRequest.specialRequests", defaultExpression = "java(\"\")")
    Bookings toBooking(BookingRequest bookingRequest, BookingStatus bookingStatus);

    @Mapping(target = "status", source = "status", qualifiedByName = "parseStatus")
    BookingResponseDto toDto(Bookings bookings);

    @Named("parseStatus")
    default BookingStatus parseStatus(String status) {
        return status == null ? BookingStatus.CREATED : BookingStatus.valueOf(status);
    }
}
