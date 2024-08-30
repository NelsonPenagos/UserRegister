package com.company.register.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "phones")
@Data
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String citycode;

    @Column(nullable = false)
    private String contrycode;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
