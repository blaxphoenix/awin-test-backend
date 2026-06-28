package com.example.awintestbackend.transaction.controller;

import com.example.awintestbackend.exception.ResourceNotFoundException;
import com.example.awintestbackend.transaction.service.TransactionData;
import com.example.awintestbackend.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/u2m/v{version}/transactions", version = "1")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionControllerDto> createTransaction(@Valid @RequestBody TransactionControllerDto transactionDto) {
        TransactionData serviceDto = toServiceDto(transactionDto);
        TransactionData createdTransaction = transactionService.createTransaction(serviceDto);
        return new ResponseEntity<>(toControllerDto(createdTransaction), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionControllerDto> getTransactionById(@PathVariable Long id) {
        TransactionControllerDto transaction = transactionService.getTransactionById(id)
                .map(this::toControllerDto)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        return ResponseEntity.ok(transaction);
    }

    @GetMapping
    public ResponseEntity<List<TransactionControllerDto>> getAllTransactions(@RequestParam(required = false) Long userid) {
        List<TransactionData> transactions;
        if (userid != null) {
            transactions = transactionService.getTransactionsByUserid(userid);
        } else {
            transactions = transactionService.getAllTransactions();
        }
        List<TransactionControllerDto> result = transactions.stream()
                .map(this::toControllerDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionControllerDto> updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionControllerDto transactionDto) {
        TransactionData serviceDto = toServiceDto(transactionDto);
        TransactionData updatedTransaction = transactionService.updateTransaction(id, serviceDto);
        return ResponseEntity.ok(toControllerDto(updatedTransaction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    private TransactionData toServiceDto(TransactionControllerDto dto) {
        return new TransactionData(dto.id(), dto.userid(), dto.value(), dto.details(), dto.date());
    }

    private TransactionControllerDto toControllerDto(TransactionData dto) {
        return new TransactionControllerDto(dto.id(), dto.userid(), dto.value(), dto.details(), dto.date());
    }
}
