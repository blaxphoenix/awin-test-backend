package com.example.awintestbackend.transaction.repository;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TransactionAdapter {

    private final TransactionRepository transactionRepository;

    public TransactionAdapter(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionRepositoryDto save(TransactionRepositoryDto dto) {
        TransactionEntity entity = toEntity(dto);
        TransactionEntity savedEntity = transactionRepository.save(entity);
        return toDto(savedEntity);
    }

    public Optional<TransactionRepositoryDto> findById(Long id) {
        return transactionRepository.findById(id).map(this::toDto);
    }

    public List<TransactionRepositoryDto> findAll() {
        return transactionRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<TransactionRepositoryDto> findByUserid(Long userid) {
        return transactionRepository.findByUserid(userid).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    public void deleteByUserid(Long userid) {
        transactionRepository.deleteByUserid(userid);
    }

    private TransactionEntity toEntity(TransactionRepositoryDto dto) {
        return new TransactionEntity(dto.id(), dto.userid(), dto.value(), dto.details(), dto.date());
    }

    private TransactionRepositoryDto toDto(TransactionEntity entity) {
        return new TransactionRepositoryDto(entity.getId(), entity.getUserid(), entity.getValue(), entity.getDetails(), entity.getDate());
    }
}
