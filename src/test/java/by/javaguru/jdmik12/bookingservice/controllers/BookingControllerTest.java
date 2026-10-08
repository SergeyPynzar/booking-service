package by.javaguru.jdmik12.bookingservice.controllers;

import by.javaguru.jdmik12.bookingservice.dto.BookingResponseDto;
import by.javaguru.jdmik12.bookingservice.dto.ResponseDto;
import by.javaguru.jdmik12.bookingservice.dto.enums.BookingStatus;
import by.javaguru.jdmik12.bookingservice.exceptions.handlers.GlobalExceptionHandler;
import by.javaguru.jdmik12.bookingservice.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@Import(GlobalExceptionHandler.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookingService bookingService;

    @Test
    void create_returns201() throws Exception {
        when(bookingService.createBooking(any())).thenReturn(new ResponseDto(7L, "ok"));

        String body = """
                {
                  "userId": 1,
                  "roomId": 2,
                  "checkInDate": "%s",
                  "checkOutDate": "%s",
                  "guestsCount": 2,
                  "totalPrice": 150.00
                }
                """.formatted(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(7));
    }

    @Test
    void create_validationError_returns400() throws Exception {
        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_returns200() throws Exception {
        when(bookingService.getBookingByRequestId(3L)).thenReturn(
                new BookingResponseDto(
                        3L, 1L, 2L,
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        2, "", new BigDecimal("150"),
                        BookingStatus.CREATED
                )
        );

        mockMvc.perform(get("/api/v1/bookings/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    void updateStatus_returns200() throws Exception {
        when(bookingService.updateBookingByRequestId(org.mockito.ArgumentMatchers.eq(3L), any())).thenReturn(
                new BookingResponseDto(3L, 1L, 2L,
                        LocalDate.now().plusDays(1), LocalDate.now().plusDays(2),
                        2, "", new BigDecimal("150"), BookingStatus.PENDING));

        mockMvc.perform(patch("/api/v1/bookings/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"PENDING\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void create_rejectsNonPositivePrice() throws Exception {
        String body = """
                {
                  "userId": 1,
                  "roomId": 2,
                  "checkInDate": "%s",
                  "checkOutDate": "%s",
                  "guestsCount": 2,
                  "totalPrice": 0
                }
                """.formatted(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
