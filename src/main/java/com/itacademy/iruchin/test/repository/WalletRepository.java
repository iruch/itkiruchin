package com.itacademy.iruchin.test.repository;

import com.itacademy.iruchin.test.domain.Wallet;
import com.itacademy.iruchin.test.dto.WalletResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    @Query("""
            select new com.itacademy.iruchin.test.dto.WalletResponse(w.uuid, coalesce(b.amount, 0))
              from Wallet w
              left join w.balance b
             order by w.createdAt
            """)
    List<WalletResponse> findAllWithBalances();
}
