package lab.shift.crmsystemforshift.data.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на создание нового продавца")
public record SellerCreateRequest(

        @Schema(description = "ФИО или название продавца", example = "Иванов Иван Иванович")
        @NotBlank(message = "Имя продавца не может быть пустым")
        @Size(min = 2, max = 100, message = "Длина имени должна быть от 2 до 100 символов")
        String name,

        @Schema(description = "Контактная информация (email, телефон, адрес)", example = "ivanov@example.com, +79990001122")
        @NotBlank(message = "Контактная информация не может быть пустой")
        @Size(max = 255, message = "Контактная информация не должна превышать 255 символов")
        String contactInfo
){}