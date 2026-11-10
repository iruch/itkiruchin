package com.itacademy.iruchin.test.repository;

import com.itacademy.iruchin.test.domain.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface BalanceRepository extends JpaRepository<Balance, UUID> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update Balance b
           set b.amount = b.amount + :delta,
               b.version = b.version + 1
         where b.walletUuid = :walletId
           and b.amount + :delta >= 0
        """)
    int changeAmount(@Param("walletId") UUID walletId, @Param("delta") BigDecimal delta);
}
