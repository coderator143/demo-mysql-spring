package com.example.paytm.inpg.controller;

import com.example.paytm.inpg.entities.Transaction;
import com.example.paytm.inpg.entities.TransactionRequestBody;
import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.entities.Wallet;
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

        // payerUser and payeeUser are list of a user with payer and payee phone number
        List<User> payerUser = userService.findbyMobileNumber(requestBody.getPayer_phone_number());
        List<User> payeeUser = userService.findbyMobileNumber(requestBody.getPayee_phone_number());

        // if either of these lists are empty, that means user doesn't exist
        if(payeeUser.isEmpty() || payerUser.isEmpty()) {
            logger.log(Level.INFO, "Either the payer or payee with this phone number doesn't exist");
            return new ResponseEntity<>(BAD_REQUEST);
        }

        // getting ID of both the users
        int payerID = payerUser.get(0).getId(), payeeID = payeeUser.get(0).getId();
        int amount = requestBody.getAmount();

        // payerL and payeeL is a list of wallet of the payer and payee users
        List<Wallet> payerL= walletService.findByOwnerID(payerID);
        List<Wallet> payeeL = walletService.findByOwnerID(payeeID);

        // if either of the lists is empty, it means user doesn't have a registered account
        if(payeeL.isEmpty() || payerL.isEmpty()) {
            logger.log(Level.INFO, "Either the payer or payee doesn't have a registered wallet");
            return new ResponseEntity<>(BAD_REQUEST);
        }

        // getting wallet objects of the payer and payee
        Wallet payer = payerL.get(0), payee = payeeL.get(0);

        // if the payer has insufficient balance
        if(payer.getBalance() < amount) {
            logger.log(Level.INFO, "Insufficient balance");
            return new ResponseEntity<>(BAD_REQUEST);
        }

        // updating and saving payer and payee wallets after the transaction
        payer.setBalance(payer.getBalance() - amount);
        payee.setBalance(payee.getBalance() + amount);
        walletService.save(payer); walletService.save(payee);

        // creating and saving two transaction objects for payer and payee
        Transaction transactionPayer = new Transaction(), transactionPayee = new Transaction();
        transactionPayer.setUser(payerID); transactionPayer.setWithuser(payeeID);
        transactionPayer.setTime(System.currentTimeMillis()); transactionPayer.setMode("Payed");
        transactionPayer.setStatus("Completed"); transactionPayer.setAmount(amount);
        transactionPayee.setUser(payeeID); transactionPayee.setWithuser(payerID);
        transactionPayee.setTime(System.currentTimeMillis()); transactionPayee.setMode("Received");
        transactionPayee.setStatus("Completed"); transactionPayee.setAmount(amount);
        logger.log(Level.INFO, "Amount of Rs "+amount+" transferred from "+payerID+" to "+payeeID);
        transactionService.save(transactionPayer);
        transactionService.save(transactionPayee);
        return new ResponseEntity<>(OK);
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
