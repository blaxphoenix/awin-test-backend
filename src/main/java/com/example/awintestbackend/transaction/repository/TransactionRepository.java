package com.example.awintestbackend.transaction.repository;

import com.example.awintestbackend.revenue.repository.RevenueTrendRepositoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByUserid(Long userid);

    void deleteByUserid(Long userid);

    List<TransactionEntity> findByUseridAndDateGreaterThanEqual(Long userid, OffsetDateTime date);

    @Query("SELECT new com.example.awintestbackend.revenue.repository.RevenueTrendRepositoryDto(" +
            "CAST(t.date AS LocalDate), SUM(t.value)) " +
            "FROM TransactionEntity t " +
            "WHERE t.userid = :userid AND t.date >= :startDate " +
            "GROUP BY CAST(t.date AS LocalDate) " +
            "ORDER BY CAST(t.date AS LocalDate)")
    List<RevenueTrendRepositoryDto> findDailyRevenueTrend(@Param("userid") Long userid, @Param("startDate") OffsetDateTime startDate);
}
