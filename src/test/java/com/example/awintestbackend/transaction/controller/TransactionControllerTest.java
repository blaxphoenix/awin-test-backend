package com.example.awintestbackend.transaction.controller;

import com.example.awintestbackend.config.SecurityConfig;
import com.example.awintestbackend.exception.GlobalExceptionHandler;
import com.example.awintestbackend.transaction.service.TransactionService;
import com.example.awintestbackend.transaction.service.TransactionServiceDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTransaction_ShouldReturnCreatedTransaction() throws Exception {
        TransactionControllerDto inputDto = new TransactionControllerDto(null, 1L, 50.0, "Detail 1", LocalDate.now());
        TransactionServiceDto createdServiceDto = new TransactionServiceDto(1L, 1L, 50.0, "Detail 1", LocalDate.now());

        when(transactionService.createTransaction(any(TransactionServiceDto.class))).thenReturn(createdServiceDto);

        mockMvc.perform(post("/u2m/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.value").value(50.0))
                .andExpect(jsonPath("$.userid").value(1L));

        verify(transactionService, times(1)).createTransaction(any(TransactionServiceDto.class));
    }

    @Test
    void getTransactionById_WhenExists_ShouldReturnTransaction() throws Exception {
        TransactionServiceDto transactionDto = new TransactionServiceDto(1L, 1L, 50.0, "Detail 1", LocalDate.now());

        when(transactionService.getTransactionById(1L)).thenReturn(Optional.of(transactionDto));

        mockMvc.perform(get("/u2m/v1/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.value").value(50.0));
    }

    @Test
    void getAllTransactions_ShouldReturnList() throws Exception {
        TransactionServiceDto transactionDto = new TransactionServiceDto(1L, 1L, 50.0, "Detail 1", LocalDate.now());
        when(transactionService.getAllTransactions()).thenReturn(List.of(transactionDto));

        mockMvc.perform(get("/u2m/v1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getTransactionsByUserid_ShouldReturnFilteredList() throws Exception {
        TransactionServiceDto transactionDto = new TransactionServiceDto(1L, 1L, 50.0, "Detail 1", LocalDate.now());
        when(transactionService.getTransactionsByUserid(1L)).thenReturn(List.of(transactionDto));

        mockMvc.perform(get("/u2m/v1/transactions?userid=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userid").value(1L));
    }

    @Test
    void updateTransaction_ShouldReturnUpdatedTransaction() throws Exception {
        TransactionControllerDto inputDto = new TransactionControllerDto(1L, 1L, 100.0, "Updated Detail", LocalDate.now());
        TransactionServiceDto updatedServiceDto = new TransactionServiceDto(1L, 1L, 100.0, "Updated Detail", LocalDate.now());

        when(transactionService.updateTransaction(eq(1L), any(TransactionServiceDto.class))).thenReturn(updatedServiceDto);

        mockMvc.perform(put("/u2m/v1/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(100.0))
                .andExpect(jsonPath("$.details").value("Updated Detail"));
    }

    @Test
    void deleteTransaction_ShouldReturnNoContent() throws Exception {
        doNothing().when(transactionService).deleteTransaction(1L);

        mockMvc.perform(delete("/u2m/v1/transactions/1"))
                .andExpect(status().isNoContent());

        verify(transactionService, times(1)).deleteTransaction(1L);
    }

    @Test
    void getTransactionById_WhenNotFound_ShouldReturnNotFoundWithBody() throws Exception {
        when(transactionService.getTransactionById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/u2m/v1/transactions/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Transaction not found with id: 1"));
    }
}
