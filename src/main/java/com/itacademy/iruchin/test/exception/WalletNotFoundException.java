package com.itacademy.iruchin.test.exception;

import java.util.UUID;

public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException(UUID walletId) {
        super("Кошелек не найден: " + walletId);
    }

}
