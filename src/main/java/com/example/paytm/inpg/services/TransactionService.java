package com.example.paytm.inpg.services;

import com.example.paytm.inpg.entities.Transaction;
import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public void save(Transaction transaction) { transactionRepository.save(transaction); }

    public List<Transaction> getTransactionByUserId(Integer id) {
        return transactionRepository.findByUser(id);
    }

    public Transaction get(Integer id) { return transactionRepository.findById(id).get(); }
}
