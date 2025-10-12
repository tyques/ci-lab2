package lab.shift.crmsystemforshift.data.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lab.shift.crmsystemforshift.data.entities.PaymentType;

@Schema(description = "Запрос на создание новой транзакции")
public record TransactionCreateRequest(

        @Schema(description = "ID продавца, совершившего транзакцию", example = "1")
        @NotNull(message = "ID продавца обязательно")
        @Positive(message = "ID продавца должен быть положительным числом")
        long sellerId,

        @Schema(description = "Сумма транзакции", example = "1500.50", minimum = "0.01")
        @Positive(message = "Сумма транзакции должна быть больше нуля")
        double amount,

        @Schema(description = "Тип оплаты", example = "CARD")
        @NotNull(message = "Тип оплаты обязателен")
        PaymentType paymentType
) {
}