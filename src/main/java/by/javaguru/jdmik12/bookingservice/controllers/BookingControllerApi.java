package by.javaguru.jdmik12.bookingservice.controllers;

import by.javaguru.jdmik12.bookingservice.dto.BookingRequest;
import by.javaguru.jdmik12.bookingservice.dto.BookingResponseDto;
import by.javaguru.jdmik12.bookingservice.dto.BookingRequestStatusUpdateDto;
import by.javaguru.jdmik12.bookingservice.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Tag(name = "Create booking in system")
public interface BookingControllerApi {

    @Operation(summary = "Designed booking to process")
    @ApiResponse(
            responseCode = "201",
            description = "CREATED",
            content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "BAD REQUEST - Invalid input data")
    @ApiResponse(responseCode = "404", description = "Get request NOT FOUND")
    @PostMapping
    ResponseEntity<ResponseDto> create(@RequestBody @Valid BookingRequest bookingRequest);

    @Operation(summary = "Get booking from system")
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content =
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BookingResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Get request NOT FOUND")
    ResponseEntity<BookingResponseDto> getByRequestId(@PathVariable(name = "requestId") Long requestId);

    @Operation(summary = "Update status in system")
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content =
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = BookingResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Update request NOT FOUND")
    ResponseEntity<BookingResponseDto> updateStatus(@PathVariable Long requestId,
                                                    @RequestBody BookingRequestStatusUpdateDto bookingRequestStatusUpdateDto);

}
