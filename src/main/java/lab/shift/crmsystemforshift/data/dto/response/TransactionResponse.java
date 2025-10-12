package lab.shift.crmsystemforshift.data.dto.response;

import lab.shift.crmsystemforshift.data.entities.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private Long sellerId;
    private double amount;
    private PaymentType paymentType;
    private LocalDateTime transactionDate;
}