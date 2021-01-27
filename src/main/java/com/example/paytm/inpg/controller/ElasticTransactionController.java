package com.example.paytm.inpg.controller;

import com.example.paytm.inpg.entities.*;
import com.example.paytm.inpg.entities.ResponseBody;
import com.example.paytm.inpg.helpers.Constants;
import com.example.paytm.inpg.helpers.PostValidator;
import com.example.paytm.inpg.repositories.ElasticTransactionRepository;
import com.example.paytm.inpg.services.dataservice.UserService;
import com.example.paytm.inpg.services.dataservice.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class ElasticTransactionController {

    @Autowired
    UserService userService;

    @Autowired
    WalletService walletService;

    @Autowired
    ElasticTransactionRepository elasticTransactionRepository;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    KafkaTemplate<String, ElasticTransaction> kafkaTemplate;
    private static final String PAYER_TOPIC = "Transaction-payer-PUSH";
    private static final String PAYEE_TOPIC = "Transaction-payee-PUSH";

    @PostMapping("/elasticTransaction")
    public ResponseEntity<ResponseBody> p2pTransfer(@RequestBody TransactionRequestBody requestBody) {
        Map<Integer, Wallet> m = PostValidator.p2pPost(requestBody, userService, walletService);
        ResponseBody responseBody;
        if(m.isEmpty()) {
            logger.log(Level.INFO, Constants.getP2pMessage());
            responseBody = new ResponseBody(Constants.getP2pMessage(), "OK");
            return new ResponseEntity<>(responseBody, OK);
        }
        Wallet payer = m.get(1), payee = m.get(2);
        int payerID = payer.getOwner(), payeeID = payee.getOwner(), amount = requestBody.getAmount();
        PostValidator.p2pElasticCreate(payer, payee, amount, walletService, kafkaTemplate,
                PAYER_TOPIC, PAYEE_TOPIC);
        responseBody = new ResponseBody(
                "Amount of Rs "+amount+" transferred from "+payerID+" to "+payeeID, "OK");
        return new ResponseEntity<>(responseBody, OK);
    }

    @GetMapping(value = "/elasticTransaction", params = "userId")
    public Page<ElasticTransaction> getTransactionByUserID(@RequestParam("userId") Integer id,
                                                    @RequestParam("page") Integer page) {
        logger.log(Level.INFO, "All transaction of user with id = "+id);

        // returning the list in a paginated and decreasing sorted way based on time
        return elasticTransactionRepository.findByUser(id, PageRequest.of(page, 3,
                Sort.by("time").descending()));
    }

    @GetMapping(value = "/elasticTransaction", params = "txnId")
    public ResponseEntity<?> get(@RequestParam("txnId") String id) {
        ResponseBody responseBody;
        try {
            Optional<ElasticTransaction> existingTransaction = elasticTransactionRepository.findById(id);
            responseBody = new ResponseBody("Completed", "OK");
            logger.log(Level.INFO, "Read transaction successfully with id = "+id);
            return new ResponseEntity<>(responseBody, OK);
        }
        catch (NoSuchElementException e) {
            responseBody = new ResponseBody("Cannot read nonexistent transaction", "Not found");
            logger.log(Level.INFO, "Cannot read nonexistent transaction");
            return new ResponseEntity<>(responseBody, NOT_FOUND);
        }
    }
}
