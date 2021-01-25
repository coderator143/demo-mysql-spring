package com.example.paytm.inpg.services;

import com.example.paytm.inpg.entities.Transaction;
import com.example.paytm.inpg.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Using all pre-created methods of TransactionRepository
    public void save(Transaction transaction) { transactionRepository.save(transaction); }

    public Page<Transaction> getTransactionByUserId(Integer id, Pageable pageable) {
        return transactionRepository.findByUser(id, pageable);
    }

    public Transaction get(Integer id) { return transactionRepository.findById(id).get(); }
}
