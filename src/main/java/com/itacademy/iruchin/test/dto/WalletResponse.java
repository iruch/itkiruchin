package com.itacademy.iruchin.test.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletResponse(UUID walletId, BigDecimal balance) {
}
