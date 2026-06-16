package by.javaguru.jdmik12.bookingservice.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bookings")
public class Bookings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "user_id", nullable = false)
    private long userId;
    @Column(name = "room_id", nullable = false)
    private long roomId;
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;
    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;
    @Column(name = "guests_count", nullable = false)
    private int guestsCount;
    @Column(name = "special_requests", nullable = false)
    private String specialRequests;
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;
    @Column(name = "status", nullable = false)
    private String status;
}
