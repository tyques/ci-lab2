package lab.shift.crmsystemforshift.data.dto.requests;

import lab.shift.crmsystemforshift.data.entities.PaymentType;

public record TransactionCreateRequest(
        long sellerId,
        double amount,
        PaymentType paymentType
) {
}
