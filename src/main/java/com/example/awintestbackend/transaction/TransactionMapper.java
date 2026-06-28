package com.example.awintestbackend.transaction;

import com.example.awintestbackend.transaction.controller.TransactionControllerDto;
import com.example.awintestbackend.transaction.repository.TransactionEntity;
import com.example.awintestbackend.transaction.repository.TransactionRepositoryDto;
import com.example.awintestbackend.transaction.service.TransactionData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TransactionMapper {

    // Controller <-> Service
    TransactionData toData(TransactionControllerDto dto);
    TransactionControllerDto toControllerDto(TransactionData data);

    // Service <-> Repository
    TransactionRepositoryDto toRepoDto(TransactionData data);
    TransactionData toData(TransactionRepositoryDto dto);

    @Mapping(target = "id", source = "id")
    TransactionRepositoryDto toRepoDto(Long id, TransactionData data);

    // Repository <-> Entity
    TransactionEntity toEntity(TransactionRepositoryDto dto);
    TransactionRepositoryDto toRepoDto(TransactionEntity entity);
}
