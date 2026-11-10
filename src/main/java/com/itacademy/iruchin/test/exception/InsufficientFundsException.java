package com.itacademy.iruchin.test.exception;

import java.util.UUID;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(UUID walletId) {
        super("Недостаточно средств на кошельке: " + walletId);
    }

}
