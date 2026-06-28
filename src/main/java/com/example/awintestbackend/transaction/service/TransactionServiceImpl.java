package com.example.awintestbackend.transaction.service;

import com.example.awintestbackend.transaction.TransactionMapper;
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
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionAdapter transactionAdapter, TransactionMapper transactionMapper) {
        this.transactionAdapter = transactionAdapter;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public TransactionData createTransaction(TransactionData transaction) {
        log.info("Creating transaction for user: {}", transaction.userid());
        TransactionRepositoryDto repoDto = transactionMapper.toRepoDto(transaction);
        TransactionRepositoryDto savedRepoDto = transactionAdapter.save(repoDto);
        return transactionMapper.toData(savedRepoDto);
    }

    @Override
    public Optional<TransactionData> getTransactionById(Long id) {
        log.info("Fetching transaction with id: {}", id);
        return transactionAdapter.findById(id).map(transactionMapper::toData);
    }

    @Override
    public List<TransactionData> getAllTransactions() {
        log.info("Fetching all transactions");
        return transactionAdapter.findAll().stream()
                .map(transactionMapper::toData)
                .toList();
    }

    @Override
    public List<TransactionData> getTransactionsByUserid(Long userid) {
        log.info("Fetching transactions for user: {}", userid);
        return transactionAdapter.findByUserid(userid).stream()
                .map(transactionMapper::toData)
                .toList();
    }

    @Override
    public TransactionData updateTransaction(Long id, TransactionData transaction) {
        log.info("Updating transaction with id: {}", id);
        TransactionRepositoryDto repoDto = transactionMapper.toRepoDto(id, transaction);
        TransactionRepositoryDto updatedRepoDto = transactionAdapter.save(repoDto);
        return transactionMapper.toData(updatedRepoDto);
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
}
