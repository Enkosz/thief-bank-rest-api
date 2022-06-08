package com.thief.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Account {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String name;

    private String surname;

    private Double amount = 0d;

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @OrderBy("date")
    private Set<Transaction> transactions = new HashSet<>();
}
