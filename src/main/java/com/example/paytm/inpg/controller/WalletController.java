package com.example.paytm.inpg.controller;

import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.entities.Wallet;
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
    public ResponseEntity<?> add(@RequestBody User userBody) {
        long mobileNumber = userBody.getMobilenumber();

        // if mobile number we get from request body is 0
        if(mobileNumber == 0) {
            logger.log(Level.INFO, "Mobile number field is empty");
            return new ResponseEntity<>(BAD_REQUEST);
        }

        // getting a list of user with the specified mobile number
        List<User> walletUser = userService.findbyMobileNumber(mobileNumber);

        // if list is empty then the user with phone number doesn't exist
        if(walletUser.isEmpty()) {
            logger.log(Level.INFO, "User with phone number "+ mobileNumber +" does not exist");
            return new ResponseEntity<>(NOT_FOUND);
        }

        // if list has a user but he is not already registered for a wallet
        else if(walletUser.size() > 0 && walletUser.get(0).getHaswallet()) {
            logger.log(Level.INFO, "User already has a wallet registered");
            return new ResponseEntity<>(BAD_REQUEST);
        }

        // getting and setting user object from userList
        User user = walletUser.get(0);
        user.setHaswallet(true);

        // creating new wallet object for the user
        Wallet wallet = new Wallet();
        wallet.setBalance(0);
        wallet.setCreation(UtilityMethods.get_current_time());
        wallet.setOwner(user.getId());
        logger.log(Level.INFO, "Wallet created with an id = "+wallet.getId());
        userService.save(user);
        walletService.save(wallet);
        return new ResponseEntity<>(OK);
    }

    @PutMapping(value = "/wallet", params = "userId")
    public ResponseEntity<?> addBalanceByUserID(@RequestParam("userId") Integer id,
                                                @RequestBody Wallet balanceWallet) {
        // find list of wallet by userID
        List<Wallet> wallets = walletService.findByOwnerID(id);

        // if wallet list is empty, user doesn't exist
        if(wallets.isEmpty()) {
            logger.log(Level.INFO, "User does not exist");
            return new ResponseEntity<>(NOT_FOUND);
        }

        // getting wallet object from list and then balanec
        Wallet wallet = wallets.get(0);
        int balance = balanceWallet.getBalance();

        // adding balance = 0 is insignificant, less than 0 is not possible
        if(balance < 1) {
            logger.log(Level.INFO, "Cannot add balance <= 0");
            return new ResponseEntity<>(BAD_REQUEST);
        }

        // setting wallet balance and then saving it
        wallet.setBalance(wallet.getBalance() + balanceWallet.getBalance());
        logger.log(Level.INFO,
                "Balance of "+balanceWallet.getBalance()+" added to wallet with id = "+wallet.getId());
        walletService.save(wallet);
        return new ResponseEntity<>(OK);
    }

    @DeleteMapping(value = "/wallet", params = "walletId")
    public ResponseEntity<?> deleteWalletByID(@RequestParam("walletId") Integer id) {
        try {
            Wallet existingWallet = walletService.get(id);
            logger.log(Level.INFO, "Deleted wallet successfully with id = "+id);
            walletService.delete(id);
            return new ResponseEntity<>(OK);
        }
        catch (NoSuchElementException e) {
            logger.log(Level.INFO, "Cannot delete nonexistent wallet");
            return new ResponseEntity<>(NOT_FOUND);
        }
    }
}
