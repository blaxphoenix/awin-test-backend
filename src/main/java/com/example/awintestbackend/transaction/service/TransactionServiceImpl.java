package com.example.awintestbackend.transaction.service;

import com.example.awintestbackend.transaction.repository.TransactionAdapter;
import com.example.awintestbackend.transaction.repository.TransactionRepositoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionAdapter transactionAdapter;

    public TransactionServiceImpl(TransactionAdapter transactionAdapter) {
        this.transactionAdapter = transactionAdapter;
    }

    @Override
    public TransactionData createTransaction(TransactionData transaction) {
        log.info("Creating transaction for user: {}", transaction.userid());
        TransactionRepositoryDto repoDto = toRepoDto(transaction);
        TransactionRepositoryDto savedRepoDto = transactionAdapter.save(repoDto);
        return toServiceDto(savedRepoDto);
    }

    @Override
    public Optional<TransactionData> getTransactionById(Long id) {
        log.info("Fetching transaction with id: {}", id);
        return transactionAdapter.findById(id).map(this::toServiceDto);
    }

    @Override
    public List<TransactionData> getAllTransactions() {
        log.info("Fetching all transactions");
        return transactionAdapter.findAll().stream()
                .map(this::toServiceDto)
                .toList();
    }

    @Override
    public List<TransactionData> getTransactionsByUserid(Long userid) {
        log.info("Fetching transactions for user: {}", userid);
        return transactionAdapter.findByUserid(userid).stream()
                .map(this::toServiceDto)
                .toList();
    }

    @Override
    public TransactionData updateTransaction(Long id, TransactionData transaction) {
        log.info("Updating transaction with id: {}", id);
        TransactionRepositoryDto repoDto = new TransactionRepositoryDto(id, transaction.userid(), transaction.value(), transaction.details(), transaction.date());
        TransactionRepositoryDto updatedRepoDto = transactionAdapter.save(repoDto);
        return toServiceDto(updatedRepoDto);
    }

    @Override
    public void deleteTransaction(Long id) {
        log.info("Deleting transaction with id: {}", id);
        transactionAdapter.deleteById(id);
    }

    @Override
    public void deleteTransactionsByUserid(Long userid) {
        log.info("Deleting transactions for user: {}", userid);
        transactionAdapter.deleteByUserid(userid);
    }

    private TransactionRepositoryDto toRepoDto(TransactionData dto) {
        return new TransactionRepositoryDto(dto.id(), dto.userid(), dto.value(), dto.details(), dto.date());
    }

    private TransactionData toServiceDto(TransactionRepositoryDto dto) {
        return new TransactionData(dto.id(), dto.userid(), dto.value(), dto.details(), dto.date());
    }
}
