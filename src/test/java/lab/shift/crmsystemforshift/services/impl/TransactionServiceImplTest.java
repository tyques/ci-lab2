package lab.shift.crmsystemforshift.services.impl;

import lab.shift.crmsystemforshift.data.dto.requests.TransactionCreateRequest;
import lab.shift.crmsystemforshift.data.dto.response.TransactionResponse;
import lab.shift.crmsystemforshift.data.entities.PaymentType;
import lab.shift.crmsystemforshift.data.entities.SellerEntity;
import lab.shift.crmsystemforshift.data.entities.TransactionEntity;
import lab.shift.crmsystemforshift.exceptions.ResourceNotFoundException;
import lab.shift.crmsystemforshift.mappers.Mapper;
import lab.shift.crmsystemforshift.repositories.SellerRepository;
import lab.shift.crmsystemforshift.repositories.TransactionRepository;
import lab.shift.crmsystemforshift.services.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private Mapper<TransactionEntity, TransactionResponse> transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private SellerEntity sellerEntity;
    private TransactionEntity transactionEntity;
    private TransactionResponse transactionResponse;

    @BeforeEach
    void setUp() {
        sellerEntity = SellerEntity.builder().id(1L).name("Test Seller").build();
        transactionEntity = TransactionEntity.builder()
                .id(100L)
                .seller(sellerEntity)
                .amount(150.0)
                .paymentType(PaymentType.CARD)
                .transactionDate(LocalDateTime.now())
                .build();
        transactionResponse = new TransactionResponse(100L, 1L, 150.0, PaymentType.CARD, transactionEntity.getTransactionDate());
    }

    @Test
    void getTransactionById_whenTransactionExists_shouldReturnTransaction() {
        when(transactionRepository.findById(100L)).thenReturn(Optional.of(transactionEntity));
        when(transactionMapper.mapToDto(transactionEntity)).thenReturn(transactionResponse);

        TransactionResponse found = transactionService.getTransactionById(100L);

        assertNotNull(found);
        assertEquals(100L, found.getId());
        verify(transactionRepository).findById(100L);
    }

    @Test
    void getTransactionById_whenTransactionDoesNotExist_shouldThrowException() {
        when(transactionRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.getTransactionById(100L));
    }

    @Test
    void createTransaction_whenSellerExists_shouldCreateTransaction() {
        TransactionCreateRequest request = new TransactionCreateRequest(1L, 200.0, PaymentType.CASH);
        TransactionEntity savedEntity = TransactionEntity.builder().id(101L).seller(sellerEntity).amount(200.0).build();
        TransactionResponse response = new TransactionResponse(101L, 1L, 200.0, PaymentType.CASH, LocalDateTime.now());

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(sellerEntity));
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(savedEntity);
        when(transactionMapper.mapToDto(savedEntity)).thenReturn(response);

        TransactionResponse created = transactionService.createTransaction(request);

        assertNotNull(created);
        assertEquals(200.0, created.getAmount());
        verify(sellerRepository).findById(1L);
        verify(transactionRepository).save(any(TransactionEntity.class));
    }

    @Test
    void createTransaction_whenSellerDoesNotExist_shouldThrowException() {
        TransactionCreateRequest request = new TransactionCreateRequest(1L, 200.0, PaymentType.CASH);

        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.createTransaction(request));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void softDeleteTransaction_whenTransactionExists_shouldDelete() {
        when(transactionRepository.existsById(100L)).thenReturn(true);
        doNothing().when(transactionRepository).deleteById(100L);

        assertDoesNotThrow(() -> transactionService.softDeleteTransaction(100L));

        verify(transactionRepository).existsById(100L);
        verify(transactionRepository).deleteById(100L);
    }

    @Test
    void softDeleteTransaction_whenTransactionDoesNotExist_shouldThrowException() {
        when(transactionRepository.existsById(100L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> transactionService.softDeleteTransaction(100L));
        verify(transactionRepository, never()).deleteById(anyLong());
    }
}