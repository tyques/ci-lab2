package lab.shift.crmsystemforshift.data.dto.response;

import lab.shift.crmsystemforshift.data.entities.PaymentType;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        Long sellerId,
        double amount,
        PaymentType paymentType,
        LocalDateTime transactionDate
) {
}
