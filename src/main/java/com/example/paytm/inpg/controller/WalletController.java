package com.example.paytm.inpg.controller;

import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.entities.Wallet;
import com.example.paytm.inpg.helpers.Constants;
import com.example.paytm.inpg.helpers.PostValidator;
import com.example.paytm.inpg.helpers.UtilityMethods;
import com.example.paytm.inpg.services.UserService;
import com.example.paytm.inpg.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.http.HttpStatus.*;

// wallet controller class for accepting and managing HTTP Requests
@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;
    @Autowired
    private UserService userService;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    // basically calling CRUD methods of the service class and specifying the response to return
    @GetMapping("/wallet")
    public List<Wallet> list() {
        logger.log(Level.INFO, "list of all wallets returned");
        return walletService.listAll();
    }

    @GetMapping(value = "/wallet", params = "walletId")
    public ResponseEntity<Wallet> get(@RequestParam("walletId") Integer id) {
        try {
            Wallet wallet = walletService.get(id);
            ResponseEntity<Wallet> r = new ResponseEntity<>(wallet, OK);
            logger.log(Level.INFO, "Read wallet successfully with id = "+id);
            return r;
        }
        catch (NoSuchElementException e) {
            logger.log(Level.INFO, "Cannot read nonexistent wallet");
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    // using User object as a request body where it will have only one field, i.e, mobile number
    @PostMapping("/wallet")
    public ResponseEntity<String> add(@RequestBody User userBody) {
        long mobileNumber = userBody.getMobilenumber();
        List<User> walletUser = PostValidator.walletPostValidate(mobileNumber, userService);
        if(walletUser.isEmpty()) {
            logger.log(Level.INFO, Constants.WALLET_POST_MESSAGE);
            return new ResponseEntity<>(Constants.WALLET_POST_MESSAGE, OK);
        }
        PostValidator.createSuccessfulWalletAccount(walletUser, userService, walletService);
        logger.log(Level.INFO, Constants.WALLET_POST_MESSAGE);
        return new ResponseEntity<>(Constants.WALLET_POST_MESSAGE, OK);
    }

    @PutMapping(value = "/wallet", params = "userId")
    public ResponseEntity<String> addBalanceByUserID(@RequestParam("userId") Integer id,
                                                @RequestBody Wallet balanceWallet) {
        // find list of wallet by userID
        List<Wallet> wallets = walletService.findByOwnerID(id);

        // if wallet list is empty, user doesn't exist
        if(wallets.isEmpty()) {
            logger.log(Level.INFO, "User does not exist");
            return new ResponseEntity<>("User does not exist", NOT_FOUND);
        }

        // getting wallet object from list and then balance
        Wallet wallet = wallets.get(0);
        int balance = balanceWallet.getBalance();

        // adding balance = 0 is insignificant, less than 0 is not possible
        if(balance < 1) {
            logger.log(Level.INFO, "Cannot add balance <= 0");
            return new ResponseEntity<>("Cannot add balance <= 0", OK);
        }

        // setting wallet balance and then saving it
        wallet.setBalance(wallet.getBalance() + balanceWallet.getBalance());
        logger.log(Level.INFO,
                "Balance of "+balanceWallet.getBalance()+" added to wallet with id = "+wallet.getId());
        walletService.save(wallet);
        return new ResponseEntity<>(
                "Balance of "+balanceWallet.getBalance()+" added to wallet with id = "+wallet.getId(),
                OK);
    }

    @DeleteMapping(value = "/wallet", params = "walletId")
    public ResponseEntity<?> deleteWalletByID(@RequestParam("walletId") Integer id) {
        try {
            Wallet existingWallet = walletService.get(id);
            logger.log(Level.INFO, "Deleted wallet successfully with id = "+id);
            walletService.delete(id);
            return new ResponseEntity<>("Deleted wallet successfully with id = "+id, OK);
        }
        catch (NoSuchElementException e) {
            logger.log(Level.INFO, "Cannot delete nonexistent wallet");
            return new ResponseEntity<>("Cannot delete nonexistent wallet", NOT_FOUND);
        }
    }
}
