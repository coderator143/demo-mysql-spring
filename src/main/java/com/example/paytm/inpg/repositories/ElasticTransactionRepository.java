package com.example.paytm.inpg.repositories;

import com.example.paytm.inpg.entities.Transaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ElasticTransactionRepository extends ElasticsearchRepository<Transaction, Integer> {
}
