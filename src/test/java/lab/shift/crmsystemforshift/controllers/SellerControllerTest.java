package lab.shift.crmsystemforshift.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lab.shift.crmsystemforshift.data.dto.requests.SellerCreateRequest;
import lab.shift.crmsystemforshift.data.dto.requests.SellerUpdateRequest;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class SellerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private SellerEntity seller1;
    private SellerEntity seller2;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        sellerRepository.deleteAll();

        seller1 = SellerEntity.builder().name("John Doe").contactInfo("john@mail.com").build();
        seller2 = SellerEntity.builder().name("Jane Smith").contactInfo("jane@mail.com").build();
        sellerRepository.save(seller1);
        sellerRepository.save(seller2);
    }

    @Test
    void testGetAllSellers() throws Exception {
        mockMvc.perform(get("/api/sellers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John Doe")));
    }

    @Test
    void testGetSellerById_Success() throws Exception {
        mockMvc.perform(get("/api/sellers/{id}", seller1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.contactInfo", is("john@mail.com")));
    }

    @Test
    void testGetSellerById_NotFound() throws Exception {
        mockMvc.perform(get("/api/sellers/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateSeller() throws Exception {
        SellerCreateRequest request = new SellerCreateRequest("New Guy", "new@guy.com");

        mockMvc.perform(post("/api/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("New Guy")));
    }

    @Test
    void testUpdateSeller() throws Exception {
        SellerUpdateRequest request = new SellerUpdateRequest(seller1.getId(), "John Updated", "john-upd@mail.com");

        mockMvc.perform(put("/api/sellers/{id}", seller1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Updated")))
                .andExpect(jsonPath("$.contactInfo", is("john-upd@mail.com")));
    }

    @Test
    void testDeleteSeller() throws Exception {
        mockMvc.perform(delete("/api/sellers/{id}", seller1.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/sellers/{id}", seller1.getId()))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/sellers/all-with-deleted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))); // seller1 и seller2

        SellerEntity deletedSeller = sellerRepository.findAllWithDeleted().stream()
                .filter(s -> s.getId().equals(seller1.getId())).findFirst().orElseThrow();
        assertNotNull(deletedSeller.getDeletionDate());
    }

    @Test
    void testGetMostProductiveSeller() throws Exception {
        TransactionEntity tx1 = TransactionEntity.builder().seller(seller1).amount(100.0).paymentType(PaymentType.CASH).transactionDate(LocalDateTime.now()).build();
        TransactionEntity tx2 = TransactionEntity.builder().seller(seller2).amount(200.0).paymentType(PaymentType.CARD).transactionDate(LocalDateTime.now()).build();
        TransactionEntity tx3 = TransactionEntity.builder().seller(seller2).amount(50.0).paymentType(PaymentType.CARD).transactionDate(LocalDateTime.now()).build();
        transactionRepository.save(tx1);
        transactionRepository.save(tx2);
        transactionRepository.save(tx3);

        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        mockMvc.perform(get("/api/sellers/analytics/most-productive")
                        .param("start", start.toString())
                        .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Jane Smith"))); // 200 + 50 > 100
    }
}