package com.example.paytm.inpg.repositories;

import com.example.paytm.inpg.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// by extending JpaRepository this interface contains all pre-created methods for logic of HTTP requests
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    // these methods are custom finder methods, i.e for custom queries for mysql
    public List<Wallet> findByOwner(int owner);
}
