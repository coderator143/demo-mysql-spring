package com.example.paytm.inpg.services;

import com.example.paytm.inpg.entities.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "Transaction-PUSH", groupId = "group_json",
    containerFactory = "concurrentKafkaListenerContainerFactory")
    public void consumeTransaction(Transaction transaction) {
        System.out.println("Consumer JSON message : "+transaction);
    }
}
