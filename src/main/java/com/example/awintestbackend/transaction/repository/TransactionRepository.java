package com.example.awintestbackend.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByUserid(Long userid);

    void deleteByUserid(Long userid);

    List<TransactionEntity> findByUseridAndDateGreaterThanEqual(Long userid, OffsetDateTime date);
}
