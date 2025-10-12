package lab.shift.crmsystemforshift.data.dto.requests;

public record SellerUpdateRequest(
        long sellerId,
        String name,
        String contactInfo
) {
}
