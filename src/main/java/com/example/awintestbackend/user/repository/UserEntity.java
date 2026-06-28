package com.example.awintestbackend.user.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String currency;

    public UserEntity() {
    }

    public UserEntity(Long userid, String name, String email, String currency) {
        this.userid = userid;
        this.name = name;
        this.email = email;
        this.currency = currency;
    }

}
