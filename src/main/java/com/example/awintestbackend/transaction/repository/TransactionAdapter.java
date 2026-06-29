package com.example.awintestbackend.transaction.repository;
import com.example.awintestbackend.revenue.repository.RevenueTrendRepositoryDto;
import com.example.awintestbackend.transaction.TransactionMapper;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class TransactionAdapter {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionAdapter(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    public TransactionRepositoryDto save(TransactionRepositoryDto dto) {
        TransactionEntity entity = transactionMapper.toEntity(dto);
        TransactionEntity savedEntity = transactionRepository.save(entity);
        return transactionMapper.toRepoDto(savedEntity);
    }

    public Optional<TransactionRepositoryDto> findById(Long id) {
        return transactionRepository.findById(id).map(transactionMapper::toRepoDto);
    }

    public List<TransactionRepositoryDto> findAll() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toRepoDto)
                .toList();
    }

    public List<TransactionRepositoryDto> findByUserid(Long userid) {
        return transactionRepository.findByUserid(userid).stream()
                .map(transactionMapper::toRepoDto)
                .toList();
    }

    public List<TransactionRepositoryDto> findByUseridAndDateGreaterThanEqual(Long userid, OffsetDateTime date) {
        return transactionRepository.findByUseridAndDateGreaterThanEqual(userid, date).stream()
                .map(transactionMapper::toRepoDto)
                .toList();
    }

    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    public void deleteByUserid(Long userid) {
        transactionRepository.deleteByUserid(userid);
    }

    public List<RevenueTrendRepositoryDto> findDailyRevenueTrend(Long userid, OffsetDateTime startDate) {
        return transactionRepository.findDailyRevenueTrend(userid, startDate);
    }
}
