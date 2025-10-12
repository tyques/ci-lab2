package lab.shift.crmsystemforshift.services.impl;

import lab.shift.crmsystemforshift.data.dto.requests.TransactionCreateRequest;
import lab.shift.crmsystemforshift.data.dto.response.TransactionResponse;
import lab.shift.crmsystemforshift.data.entities.SellerEntity;
import lab.shift.crmsystemforshift.data.entities.TransactionEntity;
import lab.shift.crmsystemforshift.exceptions.ResourceNotFoundException;
import lab.shift.crmsystemforshift.mappers.Mapper;
import lab.shift.crmsystemforshift.repositories.SellerRepository;
import lab.shift.crmsystemforshift.repositories.TransactionRepository;
import lab.shift.crmsystemforshift.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;
    private final Mapper<TransactionEntity, TransactionResponse> mapper;

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransactionsWithDeleted() {
        return transactionRepository.findAllWithDeleted().stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(mapper::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction with id " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsBySellerId(Long sellerId) {
        if (!sellerRepository.existsById(sellerId)) {
            throw new ResourceNotFoundException("Seller with id " + sellerId + " not found");
        }
        return transactionRepository.findAllBySellerId(sellerId).stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TransactionResponse createTransaction(TransactionCreateRequest request) {
        SellerEntity seller = sellerRepository.findById(request.sellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Cannot create transaction: Seller with id " + request.sellerId() + " not found"));

        TransactionEntity transaction = TransactionEntity.builder()
                .seller(seller)
                .amount(request.amount())
                .paymentType(request.paymentType())
                .transactionDate(LocalDateTime.now())
                .build();

        TransactionEntity savedTransaction = transactionRepository.save(transaction);
        return mapper.mapToDto(savedTransaction);
    }

    @Override
    @Transactional
    public void softDeleteTransaction(long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction with id " + id + " not found");
        }
        transactionRepository.deleteById(id);
    }
}