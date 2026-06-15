package by.javaguru.jdmik12.bookingservice.mapper;

import by.javaguru.jdmik12.bookingservice.dto.BookingRequest;
import by.javaguru.jdmik12.bookingservice.dto.BookingResponseDto;
import by.javaguru.jdmik12.bookingservice.dto.enums.BookingStatus;
import by.javaguru.jdmik12.bookingservice.model.Bookings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "readyToRelocate", ignore = true)
    @Mapping(target = "readyForRemoteWork", ignore = true)
    @Mapping(target = "status", source = "bookingStatus")
    Bookings toBooking(BookingRequest bookingRequest, BookingStatus bookingStatus);

    @Mapping(target = "isReadyToRelocate", ignore = true)
    @Mapping(target = "isReadyForRemoteWork", ignore = true)
    BookingResponseDto toDto(Bookings bookings);

}
