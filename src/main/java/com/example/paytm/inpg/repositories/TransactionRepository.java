package com.example.paytm.inpg.repositories;

import com.example.paytm.inpg.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

}
