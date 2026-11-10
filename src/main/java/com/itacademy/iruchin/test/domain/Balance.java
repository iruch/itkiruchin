package com.itacademy.iruchin.test.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "balances")
@Getter
@NoArgsConstructor
public class Balance {

    @Id
    @Column(name = "wallet_uuid")
    private UUID walletUuid;

    @MapsId
    @OneToOne
    @JoinColumn(name = "wallet_uuid")
    private Wallet wallet;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public Balance(Wallet wallet) {
        this.wallet = wallet;
    }


}
