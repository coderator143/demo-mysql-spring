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

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;
    @Autowired
    private UserService userService;
    private Logger logger = Logger.getLogger(this.getClass().getName());

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

    @PostMapping("/wallet")
    public ResponseEntity<?> add(@RequestBody User userBody) {
        long mobileNumber = userBody.getMobilenumber();
        if(mobileNumber == 0) {
            logger.log(Level.INFO, "Mobile number field is empty");
            return new ResponseEntity<>(BAD_REQUEST);
        }
        User user = userService.findbyMobileNumber(mobileNumber).get(0);
        user.setHaswallet(true);
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
        List<Wallet> wallets = walletService.findByOwnerID(id);
        if(wallets.isEmpty()) {
            logger.log(Level.INFO, "Wrong user ID or user hasn't created a wallet");
            return new ResponseEntity<>(BAD_REQUEST);
        }
        Wallet wallet = wallets.get(0);
        wallet.setBalance(wallet.getBalance() + balanceWallet.getBalance());
        logger.log(Level.INFO, "Balance of "+balanceWallet.getBalance()+" added");
        walletService.save(wallet);
        return new ResponseEntity<>(OK);
    }
}
