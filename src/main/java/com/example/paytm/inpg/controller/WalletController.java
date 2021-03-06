package com.example.paytm.inpg.controller;

import com.example.paytm.inpg.entities.ResponseBody;
import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.entities.Wallet;
import com.example.paytm.inpg.helpers.Constants;
import com.example.paytm.inpg.helpers.PostValidator;
import com.example.paytm.inpg.helpers.PutValidator;
import com.example.paytm.inpg.helpers.UtilityMethods;
import com.example.paytm.inpg.services.dataservice.UserService;
import com.example.paytm.inpg.services.dataservice.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
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
        logger.log(Level.INFO, "list of all wallets returned at "+ UtilityMethods.get_current_time());
        return walletService.listAll();
    }

    @GetMapping(value = "/wallet", params = "walletId")
    public ResponseEntity<?> get(@RequestParam("walletId") Integer id) {
        try {
            Wallet wallet = walletService.get(id);
            ResponseEntity<Wallet> r = new ResponseEntity<>(wallet, OK);
            logger.log(Level.INFO, wallet.toString());
            return r;
        }
        catch (NoSuchElementException e) {
            ResponseBody responseBody = new ResponseBody("Cannot read nonexistent wallet",
                    "Not found");
            logger.log(Level.INFO, responseBody.toString());
            return new ResponseEntity<>(responseBody, NOT_FOUND);
        }
    }

    // using User object as a request body where it will have only one field, i.e, mobile number
    @PostMapping("/wallet")
    public ResponseEntity<ResponseBody> add(@RequestBody User userBody) {
        long mobileNumber = userBody.getMobilenumber();
        ResponseBody responseBody;
        List<User> walletUser = PostValidator.walletPostValidate(mobileNumber, userService);
        if(walletUser.isEmpty()) {
            responseBody = new ResponseBody(Constants.getWalletPostMessage(), "OK");
            logger.log(Level.INFO, responseBody.toString());
            return new ResponseEntity<>(responseBody, OK);
        }
        PostValidator.createSuccessfulWalletAccount(walletUser, userService, walletService);
        responseBody = new ResponseBody(Constants.getWalletPostMessage(), "OK");;
        return new ResponseEntity<>(responseBody, OK);
    }

    @PutMapping(value = "/wallet", params = "userId")
    public ResponseEntity<ResponseBody> addBalanceByUserID(@RequestParam("userId") Integer id,
                                                @RequestBody Wallet balance) {
        List<Wallet> wallets = PutValidator.canBalanceBeAdded(walletService, id, balance);
        ResponseBody responseBody;
        if(wallets.isEmpty()) {
            responseBody = new ResponseBody(Constants.getWalletPutMessage(), "OK");
            logger.log(Level.INFO, responseBody.toString());
            return new ResponseEntity<>(responseBody, OK);
        }
        Wallet wallet = wallets.get(0);

        // setting wallet balance and then saving it
        wallet.setBalance(wallet.getBalance() + balance.getBalance());
        responseBody = new ResponseBody(
                "Balance of "+balance.getBalance()+" added to wallet with id = "+wallet.getId(),
                "OK");
        logger.log(Level.INFO, wallet.toString());
        walletService.save(wallet);
        return new ResponseEntity<>(responseBody, OK);
    }

    @DeleteMapping(value = "/wallet", params = "walletId")
    public ResponseEntity<ResponseBody> deleteWalletByID(@RequestParam("walletId") Integer id) {
        ResponseBody responseBody;
        try {
            Wallet existingWallet = walletService.get(id);
            responseBody = new ResponseBody("Deleted wallet successfully with id = "+id, "OK");
            logger.log(Level.INFO, existingWallet.toString());
            walletService.delete(id);
            return new ResponseEntity<>(responseBody, OK);
        }
        catch (NoSuchElementException e) {
            responseBody = new ResponseBody("Cannot delete nonexistent wallet", "Not found");
            logger.log(Level.INFO, responseBody.toString());
            return new ResponseEntity<>(responseBody, NOT_FOUND);
        }
    }
}
