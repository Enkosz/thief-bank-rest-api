package com.thief.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private List<Transaction> transactionsSent = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "to")
    private List<Transaction> transactionsReceived = new ArrayList<>();

    public List<Transaction> getTransactions() {
        List<Transaction> transactions = Stream.concat(transactionsSent.stream(), transactionsReceived.stream()).collect(Collectors.toList());
        List<Transaction> result = new ArrayList<>();
        Set<Transaction> set = new HashSet<>();

        for(Transaction transaction : transactions) {
            if(set.contains(transaction) && transaction.getType() == Transaction.Type.INTERNAL) continue;

            result.add(transaction);
            set.add(transaction);
        }

        return result;
    }

    public void disable() {
        this.active = false;
    }
}
