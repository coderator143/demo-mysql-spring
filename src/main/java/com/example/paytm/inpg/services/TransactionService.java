package com.example.paytm.inpg.services;

import com.example.paytm.inpg.entities.Transaction;
import com.example.paytm.inpg.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public void save(Transaction transaction) { transactionRepository.save(transaction); }

}
