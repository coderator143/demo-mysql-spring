package com.example.paytm.inpg.repositories;

import com.example.paytm.inpg.entities.ElasticTransaction;
import com.example.paytm.inpg.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.JpaRepository;

// by extending JpaRepository this interface contains all pre-created methods for logic of HTTP requests
public interface ElasticTransactionRepository extends ElasticsearchRepository<ElasticTransaction, String> {

    // these methods are custom finder methods, i.e for custom queries for mysql
    // returning a page instead of a list
    public Page<ElasticTransaction> findByUser(Integer id, Pageable pageable);
}