package lab.shift.crmsystemforshift.data.dto.response;

import java.time.LocalDateTime;

public record SellerResponse(
        long sellerId,
        String name,
        String contactInfo,
        LocalDateTime registrationDate,
        LocalDateTime deletionDate
) {
}
