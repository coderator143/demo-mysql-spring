package com.example.paytm.inpg.services;

import com.example.paytm.inpg.entities.Transaction;
import com.example.paytm.inpg.helpers.Constants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "Transaction-payer-PUSH", groupId = "group_json",
    containerFactory = "concurrentKafkaListenerContainerFactory")
    public void consumePayerTransaction(Transaction payerTransaction) {
        Constants.setPAYER(payerTransaction);
        System.out.println(payerTransaction);
    }

    @KafkaListener(topics = "Transaction-payee-PUSH", groupId = "group_json",
            containerFactory = "concurrentKafkaListenerContainerFactory")
    public void consumePayeeTransaction(Transaction payeeTransaction) {
        Constants.setPAYEE(payeeTransaction);
        System.out.println(payeeTransaction);
    }
}
