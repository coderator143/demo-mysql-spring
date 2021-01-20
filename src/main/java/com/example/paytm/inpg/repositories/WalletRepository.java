package com.example.paytm.inpg.repositories;

import com.example.paytm.inpg.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {
}
