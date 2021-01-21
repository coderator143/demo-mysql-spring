package com.example.paytm.inpg.services;

import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.entities.Wallet;
import com.example.paytm.inpg.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public List<Wallet> listAll() { return walletRepository.findAll(); }

    public Wallet get(Integer id) { return walletRepository.findById(id).get(); }

    public void save(Wallet wallet) { walletRepository.save(wallet); }

    public void delete(Integer id) { walletRepository.deleteById(id); }

    public List<Wallet> findByOwnerID(int owner) { return walletRepository.findByOwner(owner); }
}
