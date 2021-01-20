package com.example.paytm.inpg.services;

import com.example.paytm.inpg.entities.Wallet;
import com.example.paytm.inpg.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public void save(Wallet wallet) { walletRepository.save(wallet); }
}
