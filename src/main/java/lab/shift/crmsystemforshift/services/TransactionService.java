package lab.shift.crmsystemforshift.services;

import lab.shift.crmsystemforshift.data.dto.requests.TransactionCreateRequest;
import lab.shift.crmsystemforshift.data.dto.response.TransactionResponse;

import java.util.List;

public interface TransactionService {
    List<TransactionResponse> getAllTransactions();
    List<TransactionResponse> getAllTransactionsWithDeleted();
    TransactionResponse getTransactionById(Long id);
    List<TransactionResponse> getTransactionsBySellerId(Long sellerId);
    TransactionResponse createTransaction(TransactionCreateRequest request);
    void softDeleteTransaction(long id);
}