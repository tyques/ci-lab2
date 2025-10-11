package lab.shift.crmsystemforshift.controllers;

import lab.shift.crmsystemforshift.data.dto.requests.SellerCreateRequest;
import lab.shift.crmsystemforshift.data.dto.requests.SellerUpdateRequest;
import lab.shift.crmsystemforshift.data.dto.response.SellerResponse;
import lab.shift.crmsystemforshift.data.dto.response.TransactionResponse;
import lab.shift.crmsystemforshift.services.SellerService;
import lab.shift.crmsystemforshift.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<SellerResponse>> getAllSellers() {
        return ResponseEntity.ok(sellerService.getAllSellers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerResponse> getSellerById(@PathVariable Long id) {
        return ResponseEntity.ok(sellerService.getSellerById(id));
    }

    @PostMapping
    public ResponseEntity<SellerResponse> createSeller(@RequestBody SellerCreateRequest request) {
        SellerResponse createdSeller = sellerService.createSeller(request);
        return new ResponseEntity<>(createdSeller, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SellerResponse> updateSeller(@PathVariable Long id, @RequestBody SellerUpdateRequest request) {
        return ResponseEntity.ok(sellerService.updateSeller(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{sellerId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactionsBySellerId(@PathVariable Long sellerId) {
        return ResponseEntity.ok(transactionService.getTransactionsBySellerId(sellerId));
    }

    @GetMapping("/analytics/most-productive")
    public ResponseEntity<SellerResponse> getMostProductiveSeller(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(sellerService.getMostProductiveSeller(start, end));
    }

    @GetMapping("/analytics/below-amount")
    public ResponseEntity<List<SellerResponse>> getSellersBelowAmount(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam double amount) {
        return ResponseEntity.ok(sellerService.getSellersBelowAmount(start, end, amount));
    }
}