package com.example.paytm.inpg.repositories;


import com.example.paytm.inpg.entities.Transaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticTransactionRepository extends ElasticsearchRepository<Transaction, Integer> {
}
