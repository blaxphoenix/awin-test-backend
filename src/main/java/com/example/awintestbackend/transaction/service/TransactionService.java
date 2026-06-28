package com.example.awintestbackend.transaction.service;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    TransactionData createTransaction(TransactionData transaction);

    Optional<TransactionData> getTransactionById(Long id);

    List<TransactionData> getAllTransactions();

    List<TransactionData> getTransactionsByUserid(Long userid);

    TransactionData updateTransaction(Long id, TransactionData transaction);

    void deleteTransaction(Long id);

    void deleteTransactionsByUserid(Long userid);
}
