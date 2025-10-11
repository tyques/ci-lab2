package lab.shift.crmsystemforshift.services;

import lab.shift.crmsystemforshift.data.dto.requests.SellerCreateRequest;
import lab.shift.crmsystemforshift.data.dto.requests.SellerUpdateRequest;
import lab.shift.crmsystemforshift.data.dto.response.SellerResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface SellerService {
    List<SellerResponse> getAllSellers();
    SellerResponse getSellerById(Long id);
    SellerResponse createSeller(SellerCreateRequest request);
    SellerResponse updateSeller(Long id, SellerUpdateRequest request);
    void deleteSeller(Long id);
    SellerResponse getMostProductiveSeller(LocalDateTime start, LocalDateTime end);
    List<SellerResponse> getSellersBelowAmount(LocalDateTime start, LocalDateTime end, double amount);
}
