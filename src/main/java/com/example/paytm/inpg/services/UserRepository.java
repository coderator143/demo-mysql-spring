package com.example.paytm.inpg.services;

import com.example.paytm.inpg.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
