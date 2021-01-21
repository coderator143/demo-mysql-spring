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

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;
    @Autowired
    private WalletService walletService;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @PostMapping("/transaction")
    public ResponseEntity<?> p2pTransfer(@RequestBody TransactionRequestBody requestBody) {
        List<User> payerUser = userService.findbyMobileNumber(requestBody.getPayer_phone_number());
        List<User> payeeUser = userService.findbyMobileNumber(requestBody.getPayee_phone_number());
        if(payeeUser.isEmpty() || payerUser.isEmpty()) {
            logger.log(Level.INFO, "Either the payer or payee with this phone number doesn't exist");
            return new ResponseEntity<>(BAD_REQUEST);
        }
        int payerID = payerUser.get(0).getId(), payeeID = payeeUser.get(0).getId();
        int amount = requestBody.getAmount();
        List<Wallet> payerL= walletService.findByOwnerID(payerID);
        List<Wallet> payeeL = walletService.findByOwnerID(payeeID);
        if(payeeL.isEmpty() || payerL.isEmpty()) {
            logger.log(Level.INFO, "Either the payer or payee doesn't have a registered wallet");
            return new ResponseEntity<>(BAD_REQUEST);
        }
        Wallet payer = payerL.get(0);
        Wallet payee = payeeL.get(0);
        if(payer.getBalance() < amount) {
            logger.log(Level.INFO, "Insufficient balance");
            return new ResponseEntity<>(BAD_REQUEST);
        }
        payer.setBalance(payer.getBalance() - amount);
        payee.setBalance(payee.getBalance() + amount);
        walletService.save(payer); walletService.save(payee);
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
