package com.example.paytm.inpg.controller;

import com.example.paytm.inpg.entities.Transaction;
import com.example.paytm.inpg.entities.TransactionRequestBody;
import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.entities.Wallet;
import com.example.paytm.inpg.helpers.Constants;
import com.example.paytm.inpg.helpers.PostValidator;
import com.example.paytm.inpg.services.TransactionService;
import com.example.paytm.inpg.services.UserService;
import com.example.paytm.inpg.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.http.HttpStatus.*;

// transaction controller class for accepting and managing HTTP Requests
@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;
    @Autowired
    private WalletService walletService;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    // basically calling CRUD methods of the service class and specifying the response to return
    // using transaction request body in entity class as a request for p2p transfer
    @PostMapping("/transaction")
    public ResponseEntity<?> p2pTransfer(@RequestBody TransactionRequestBody requestBody) {
        Map<Integer, Wallet> m = PostValidator.p2pPost(requestBody, userService, walletService);
        if(m.isEmpty()) {
            logger.log(Level.INFO, Constants.P2P_MESSAGE);
            return new ResponseEntity<>(Constants.P2P_MESSAGE, OK);
        }
        Wallet payer = m.get(1), payee = m.get(2);
        int payerID = payer.getOwner(), payeeID = payee.getOwner(), amount = requestBody.getAmount();
        PostValidator.p2pCreate(payer, payee, amount, walletService, transactionService);
        return new ResponseEntity<>("Amount of Rs "+amount+" transferred from "+payerID+" to "+payeeID,
                OK);
    }

    @GetMapping(value = "/transaction", params = "userId")
    public Page<Transaction> getTransactionByUserID(@RequestParam("userId") Integer id,
                                                    @RequestParam("page") Integer page) {
        logger.log(Level.INFO, "All transaction of user with id = "+id);

        // returning the list in a paginated and decreasing sorted way based on time
        return transactionService.getTransactionByUserId(id, PageRequest.of(page, 3,
                Sort.by("time").descending()));
    }

    @GetMapping(value = "/transaction", params = "txnId")
    public ResponseEntity<String> get(@RequestParam("txnId") Integer id) {
        try {
            Transaction existingTransaction = transactionService.get(id);
            ResponseEntity<String> r = new ResponseEntity<>(existingTransaction.getStatus(), OK);
            logger.log(Level.INFO, "Read transaction successfully with id = "+id);
            return r;
        }
        catch (NoSuchElementException e) {
            logger.log(Level.INFO, "Cannot read nonexistent transaction");
            return new ResponseEntity<>(NOT_FOUND);
        }
    }
}
