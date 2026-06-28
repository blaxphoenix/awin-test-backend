package com.example.awintestbackend.transaction.service;

import com.example.awintestbackend.transaction.repository.TransactionAdapter;
import com.example.awintestbackend.transaction.repository.TransactionRepositoryDto;
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
class TransactionServiceImplTest {

    @Mock
    private TransactionAdapter transactionAdapter;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private TransactionServiceDto serviceDto;
    private TransactionRepositoryDto repoDto;

    @BeforeEach
    void setUp() {
        serviceDto = new TransactionServiceDto(1L, 100L, 50.0, "Detail 1", LocalDate.now());
        repoDto = new TransactionRepositoryDto(1L, 100L, 50.0, "Detail 1", LocalDate.now());
    }

    @Test
    void createTransaction_ShouldReturnCreatedTransaction() {
        when(transactionAdapter.save(any(TransactionRepositoryDto.class))).thenReturn(repoDto);

        TransactionServiceDto result = transactionService.createTransaction(serviceDto);

        assertNotNull(result);
        assertEquals(serviceDto.id(), result.id());
        assertEquals(serviceDto.userid(), result.userid());
        assertEquals(serviceDto.value(), result.value());
        assertEquals(serviceDto.details(), result.details());
        assertEquals(serviceDto.date(), result.date());
        verify(transactionAdapter, times(1)).save(any(TransactionRepositoryDto.class));
    }

    @Test
    void getTransactionById_WhenTransactionExists_ShouldReturnTransaction() {
        when(transactionAdapter.findById(1L)).thenReturn(Optional.of(repoDto));

        Optional<TransactionServiceDto> result = transactionService.getTransactionById(1L);

        assertTrue(result.isPresent());
        assertEquals(serviceDto.id(), result.get().id());
        verify(transactionAdapter, times(1)).findById(1L);
    }

    @Test
    void getTransactionsByUserid_ShouldReturnListOfTransactions() {
        when(transactionAdapter.findByUserid(100L)).thenReturn(List.of(repoDto));

        List<TransactionServiceDto> result = transactionService.getTransactionsByUserid(100L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.getFirst().userid());
        verify(transactionAdapter, times(1)).findByUserid(100L);
    }

    @Test
    void updateTransaction_ShouldReturnUpdatedTransaction() {
        when(transactionAdapter.save(any(TransactionRepositoryDto.class))).thenReturn(repoDto);

        TransactionServiceDto result = transactionService.updateTransaction(1L, serviceDto);

        assertNotNull(result);
        assertEquals(serviceDto.id(), result.id());
        verify(transactionAdapter, times(1)).save(any(TransactionRepositoryDto.class));
    }

    @Test
    void deleteTransaction_ShouldCallAdapterDelete() {
        doNothing().when(transactionAdapter).deleteById(1L);

        transactionService.deleteTransaction(1L);

        verify(transactionAdapter, times(1)).deleteById(1L);
    }

    @Test
    void deleteTransactionsByUserid_ShouldCallAdapterDeleteByUserid() {
        doNothing().when(transactionAdapter).deleteByUserid(100L);

        transactionService.deleteTransactionsByUserid(100L);

        verify(transactionAdapter, times(1)).deleteByUserid(100L);
    }
}
