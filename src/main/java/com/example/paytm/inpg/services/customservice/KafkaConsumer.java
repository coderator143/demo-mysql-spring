package com.example.paytm.inpg.services.customservice;

import com.example.paytm.inpg.entities.ElasticTransaction;
import com.example.paytm.inpg.entities.Transaction;
import com.example.paytm.inpg.helpers.Constants;
import com.example.paytm.inpg.repositories.ElasticTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @Autowired
    ElasticTransactionRepository elasticTransactionRepository;

    @KafkaListener(topics = "Transaction-payer-PUSH", groupId = "group_json",
    containerFactory = "concurrentKafkaListenerContainerFactory")
    public void consumePayerTransaction(ElasticTransaction payerTransaction) {
        Constants.setPAYER(payerTransaction);
        System.out.println(payerTransaction.getId());
        elasticTransactionRepository.save(payerTransaction);
    }

    @KafkaListener(topics = "Transaction-payee-PUSH", groupId = "group_json",
            containerFactory = "concurrentKafkaListenerContainerFactory")
    public void consumePayeeTransaction(ElasticTransaction payeeTransaction) {
        Constants.setPAYEE(payeeTransaction);
        System.out.println(payeeTransaction.getId());
        elasticTransactionRepository.save(payeeTransaction);
    }
}
