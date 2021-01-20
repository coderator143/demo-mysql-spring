package com.example.paytm.inpg.repositories;

import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    public List<Wallet> findByOwner(int owner);
}
