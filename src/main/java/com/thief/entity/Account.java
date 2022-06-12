package com.thief.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(generator = "account_id_generator")
    @GenericGenerator(name = "account_id_generator", strategy = "com.thief.util.AccountIdGenerator")
    private String id;

    private String name;

    private String surname;

    private Double amount = 0d;

    private Boolean active = Boolean.TRUE;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "from")
    private Set<Transaction> transactionsSent = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "to")
    private Set<Transaction> transactionsReceived = new HashSet<>();

    public Set<Transaction> getTransactions() {
        Stream<Transaction> transactionStream = Stream.concat(transactionsReceived.stream(), transactionsSent.stream());

        return transactionStream
                .collect(Collectors.toSet());
    }

    public void disable() {
        this.active = false;
    }
}
