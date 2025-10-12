package lab.shift.crmsystemforshift.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerResponse {
    private long sellerId;
    private String name;
    private String contactInfo;
    private LocalDateTime registrationDate;
    private LocalDateTime deletionDate;
}