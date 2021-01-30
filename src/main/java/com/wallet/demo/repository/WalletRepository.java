package com.wallet.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wallet.demo.entity.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {
    List<Wallet> findAllByOrderByPriority();
}
