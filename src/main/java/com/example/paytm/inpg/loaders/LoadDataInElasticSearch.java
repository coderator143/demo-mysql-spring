package com.example.paytm.inpg.loaders;

import com.example.paytm.inpg.entities.Transaction;
import com.example.paytm.inpg.helpers.Constants;
import com.example.paytm.inpg.repositories.ElasticTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Component
public class LoadDataInElasticSearch {

    @Autowired
    ElasticsearchOperations elasticsearchOperations;

    @Autowired
    ElasticTransactionRepository elasticTransactionRepository;

    @PostConstruct
    @Transactional
    public void loadAll() {
        elasticsearchOperations.putMapping(Transaction.class);
        System.out.println("Loading data");
        elasticTransactionRepository.save(Constants.getPAYER());
        elasticTransactionRepository.save(Constants.getPAYEE());
        System.out.println("Loading completed");
    }
}
