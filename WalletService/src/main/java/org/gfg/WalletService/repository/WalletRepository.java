package org.gfg.WalletService.repository;


import jakarta.transaction.Transactional;
import org.gfg.WalletService.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Wallet findByPhoneNo(String phoneNo);

    // We should use @modify if we want to modify the resource. By this we are telling JPA that we are doing modification here
    //Also It's recommended to put @Transactional if we modify the resource
    @Transactional
    @Modifying
    @Query("update Wallet w set w.balance = w.balance+:amount where w.phoneNo = :phoneNo")
    void updateWallet(String phoneNo, Double amount);

}
