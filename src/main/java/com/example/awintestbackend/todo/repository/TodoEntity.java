package com.example.awintestbackend.todo.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "todos")
public class TodoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userid;

    @Column(nullable = false)
    private String description;

    @Column
    private String icon;

    @Column(nullable = false)
    private boolean state;

    public TodoEntity() {
    }

    public TodoEntity(Long id, Long userid, String description, String icon, boolean state) {
        this.id = id;
        this.userid = userid;
        this.description = description;
        this.icon = icon;
        this.state = state;
    }
}
