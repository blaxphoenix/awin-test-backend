package com.example.awintestbackend.transaction.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionAdapterTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionAdapter transactionAdapter;

    private TransactionRepositoryDto repoDto;
    private TransactionEntity transactionEntity;

    @BeforeEach
    void setUp() {
        repoDto = new TransactionRepositoryDto(1L, 100L, 50.0, "Detail 1", LocalDate.now());
        transactionEntity = new TransactionEntity(1L, 100L, 50.0, "Detail 1", LocalDate.now());
    }

    @Test
    void save_ShouldReturnSavedDto() {
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(transactionEntity);

        TransactionRepositoryDto result = transactionAdapter.save(repoDto);

        assertNotNull(result);
        assertEquals(repoDto.id(), result.id());
        assertEquals(repoDto.userid(), result.userid());
        verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
    }

    @Test
    void findById_WhenExists_ShouldReturnDto() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transactionEntity));

        Optional<TransactionRepositoryDto> result = transactionAdapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(repoDto.id(), result.get().id());
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void findByUserid_ShouldReturnListOfDtos() {
        when(transactionRepository.findByUserid(100L)).thenReturn(List.of(transactionEntity));

        List<TransactionRepositoryDto> result = transactionAdapter.findByUserid(100L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.getFirst().userid());
        verify(transactionRepository, times(1)).findByUserid(100L);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        doNothing().when(transactionRepository).deleteById(1L);

        transactionAdapter.deleteById(1L);

        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteByUserid_ShouldCallRepositoryDeleteByUserid() {
        doNothing().when(transactionRepository).deleteByUserid(100L);

        transactionAdapter.deleteByUserid(100L);

        verify(transactionRepository, times(1)).deleteByUserid(100L);
    }
}
