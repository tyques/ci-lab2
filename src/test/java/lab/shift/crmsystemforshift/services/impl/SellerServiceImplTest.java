package lab.shift.crmsystemforshift.services.impl;

import lab.shift.crmsystemforshift.data.dto.requests.SellerCreateRequest;
import lab.shift.crmsystemforshift.data.dto.requests.SellerUpdateRequest;
import lab.shift.crmsystemforshift.data.dto.response.SellerResponse;
import lab.shift.crmsystemforshift.data.entities.SellerEntity;
import lab.shift.crmsystemforshift.exceptions.ResourceNotFoundException;
import lab.shift.crmsystemforshift.mappers.Mapper;
import lab.shift.crmsystemforshift.repositories.SellerRepository;
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
class SellerServiceImplTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private Mapper<SellerEntity, SellerResponse> sellerMapper;

    @InjectMocks
    private SellerServiceImpl sellerService;

    private SellerEntity sellerEntity;
    private SellerResponse sellerResponse;

    @BeforeEach
    void setUp() {
        sellerEntity = SellerEntity.builder()
                .id(1L)
                .name("John Doe")
                .contactInfo("john.doe@example.com")
                .registrationDate(LocalDateTime.now())
                .build();

        sellerResponse = new SellerResponse(1L, "John Doe", "john.doe@example.com", sellerEntity.getRegistrationDate(), null);
    }

    @Test
    void getSellerById_whenSellerExists_shouldReturnSeller() {
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(sellerEntity));
        when(sellerMapper.mapToDto(sellerEntity)).thenReturn(sellerResponse);

        SellerResponse foundSeller = sellerService.getSellerById(1L);

        assertNotNull(foundSeller);
        assertEquals(1L, foundSeller.getSellerId());
        verify(sellerRepository).findById(1L);
    }

    @Test
    void getSellerById_whenSellerDoesNotExist_shouldThrowException() {
        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sellerService.getSellerById(1L));
        verify(sellerRepository).findById(1L);
    }

    @Test
    void createSeller_shouldCreateAndReturnSeller() {
        SellerCreateRequest createRequest = new SellerCreateRequest("Jane Doe", "jane.doe@example.com");
        SellerEntity newSellerEntity = SellerEntity.builder().name("Jane Doe").contactInfo("jane.doe@example.com").build();
        SellerEntity savedSellerEntity = SellerEntity.builder().id(2L).name("Jane Doe").contactInfo("jane.doe@example.com").build();
        SellerResponse newSellerResponse = new SellerResponse(2L, "Jane Doe", "jane.doe@example.com", LocalDateTime.now(), null);


        when(sellerRepository.save(any(SellerEntity.class))).thenReturn(savedSellerEntity);
        when(sellerMapper.mapToDto(savedSellerEntity)).thenReturn(newSellerResponse);

        SellerResponse createdSeller = sellerService.createSeller(createRequest);

        assertNotNull(createdSeller);
        assertEquals("Jane Doe", createdSeller.getName());
        verify(sellerRepository).save(any(SellerEntity.class));
    }

    @Test
    void updateSeller_whenSellerExists_shouldUpdateAndReturnSeller() {
        SellerUpdateRequest updateRequest = new SellerUpdateRequest(1L, "John Doe Updated", "john.doe.new@example.com");

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(sellerEntity));
        when(sellerRepository.save(any(SellerEntity.class))).thenReturn(sellerEntity);
        when(sellerMapper.mapToDto(sellerEntity)).thenReturn(sellerResponse);

        sellerResponse.setName(updateRequest.name()); // Simulate update
        SellerResponse updatedSeller = sellerService.updateSeller(1L, updateRequest);

        assertNotNull(updatedSeller);
        assertEquals("John Doe Updated", updatedSeller.getName());
        verify(sellerRepository).findById(1L);
        verify(sellerRepository).save(sellerEntity);
    }

    @Test
    void updateSeller_whenSellerDoesNotExist_shouldThrowException() {
        SellerUpdateRequest updateRequest = new SellerUpdateRequest(1L, "John Doe Updated", "john.doe.new@example.com");
        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sellerService.updateSeller(1L, updateRequest));
        verify(sellerRepository).findById(1L);
        verify(sellerRepository, never()).save(any());
    }

    @Test
    void deleteSeller_whenSellerExists_shouldDeleteSeller() {
        when(sellerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(sellerRepository).deleteById(1L);

        assertDoesNotThrow(() -> sellerService.deleteSeller(1L));

        verify(sellerRepository).existsById(1L);
        verify(sellerRepository).deleteById(1L);
    }

    @Test
    void deleteSeller_whenSellerDoesNotExist_shouldThrowException() {
        when(sellerRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> sellerService.deleteSeller(1L));

        verify(sellerRepository).existsById(1L);
        verify(sellerRepository, never()).deleteById(anyLong());
    }
}