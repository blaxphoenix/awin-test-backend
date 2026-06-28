package com.example.awintestbackend.transaction.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userid;

    @Column(name = "\"value\"", nullable = false)
    private Double value;

    @Column(nullable = false)
    private String details;

    @Column(nullable = false)
    private LocalDate date;

    public TransactionEntity() {
    }

    public TransactionEntity(Long id, Long userid, Double value, String details, LocalDate date) {
        this.id = id;
        this.userid = userid;
        this.value = value;
        this.details = details;
        this.date = date;
    }
}
