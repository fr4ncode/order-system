package dev.francode.ordersystem.dto.order;

import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderAdminFilter {

    private Long userId;

    private String status;

    @PastOrPresent(message = "La fecha de inicio no puede estar en el futuro.")
    private LocalDateTime startDate;

    @PastOrPresent(message = "La fecha de fin no puede estar en el futuro.")
    private LocalDateTime endDate;

    public boolean isDateRangeValid() {
        return (startDate == null || endDate == null || !startDate.isAfter(endDate));
    }
}

