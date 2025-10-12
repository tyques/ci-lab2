package lab.shift.crmsystemforshift.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab.shift.crmsystemforshift.data.dto.requests.TransactionCreateRequest;
import lab.shift.crmsystemforshift.data.entities.PaymentType;
import lab.shift.crmsystemforshift.data.entities.SellerEntity;
import lab.shift.crmsystemforshift.data.entities.TransactionEntity;
import lab.shift.crmsystemforshift.repositories.SellerRepository;
import lab.shift.crmsystemforshift.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private SellerEntity seller;
    private TransactionEntity transaction;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        sellerRepository.deleteAll();

        seller = sellerRepository.save(SellerEntity.builder().name("Default Seller").contactInfo("seller@mail.com").build());
        transaction = transactionRepository.save(TransactionEntity.builder()
                .seller(seller)
                .amount(120.50)
                .paymentType(PaymentType.CARD)
                .transactionDate(LocalDateTime.now())
                .build());
    }

    @Test
    void getAllTransactions_shouldReturnTransactionList() throws Exception {
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].amount", is(120.50)));
    }

    @Test
    void getTransactionById_whenExists_shouldReturnTransaction() throws Exception {
        mockMvc.perform(get("/api/transactions/{id}", transaction.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(transaction.getId().intValue())))
                .andExpect(jsonPath("$.amount", is(120.50)))
                .andExpect(jsonPath("$.paymentType", is("CARD")));
    }

    @Test
    void getTransactionById_whenNotExists_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/transactions/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTransaction_withValidData_shouldCreateAndReturnTransaction() throws Exception {
        TransactionCreateRequest request = new TransactionCreateRequest(seller.getId(), 500.0, PaymentType.TRANSFER);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sellerId", is(seller.getId().intValue())))
                .andExpect(jsonPath("$.amount", is(500.0)))
                .andExpect(jsonPath("$.paymentType", is("TRANSFER")));
    }

    @Test
    void createTransaction_withNonExistentSeller_shouldReturnNotFound() throws Exception {
        TransactionCreateRequest request = new TransactionCreateRequest(999L, 100.0, PaymentType.CASH);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTransaction_whenExists_shouldPerformSoftDelete() throws Exception {
        mockMvc.perform(delete("/api/transactions/{id}", transaction.getId()))
                .andExpect(status().isNoContent());

        assertTrue(transactionRepository.findById(transaction.getId()).isEmpty());
    }
}