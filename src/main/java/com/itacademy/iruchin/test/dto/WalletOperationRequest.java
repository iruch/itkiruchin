package com.itacademy.iruchin.test.dto;

import com.itacademy.iruchin.test.common.OperationType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletOperationRequest(
        @NotNull UUID walletId,
        @NotNull OperationType operationType,
        @NotNull @Positive @Digits(integer = 17, fraction = 2) BigDecimal amount) {
}
