package com.example.paytm.inpg.repositories;

import com.example.paytm.inpg.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    public List<Transaction> findByUser(Integer id);
}
