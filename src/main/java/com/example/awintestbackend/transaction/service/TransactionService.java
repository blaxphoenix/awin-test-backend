package com.example.awintestbackend.transaction.service;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    TransactionServiceDto createTransaction(TransactionServiceDto transaction);
    Optional<TransactionServiceDto> getTransactionById(Long id);
    List<TransactionServiceDto> getAllTransactions();
    List<TransactionServiceDto> getTransactionsByUserid(Long userid);
    TransactionServiceDto updateTransaction(Long id, TransactionServiceDto transaction);
    void deleteTransaction(Long id);
    void deleteTransactionsByUserid(Long userid);
}
