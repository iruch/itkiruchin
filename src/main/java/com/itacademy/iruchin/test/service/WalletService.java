package com.itacademy.iruchin.test.service;

import com.itacademy.iruchin.test.domain.Balance;
import com.itacademy.iruchin.test.domain.Wallet;
import com.itacademy.iruchin.test.dto.WalletOperationRequest;
import com.itacademy.iruchin.test.dto.WalletResponse;
import com.itacademy.iruchin.test.exception.InsufficientFundsException;
import com.itacademy.iruchin.test.exception.WalletNotFoundException;
import com.itacademy.iruchin.test.repository.BalanceRepository;
import com.itacademy.iruchin.test.repository.WalletRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final BalanceRepository balanceRepository;

    public WalletService(WalletRepository walletRepository, BalanceRepository balanceRepository) {
        this.walletRepository = walletRepository;
        this.balanceRepository = balanceRepository;
    }


    /**
     * Создаем кошелек для тестов
     */
    @Transactional
    public WalletResponse create() {
        Wallet wallet = new Wallet();
        wallet.setBalance(new Balance(wallet));
        walletRepository.save(wallet);
        return new WalletResponse(wallet.getUuid(), BigDecimal.ZERO);
    }

    /**
     * Получаем кошелек по UUID с фронта
     * @param walletId - UUID кошелька
     * @return формализованный ответ
     */
    @Transactional(readOnly = true)
    public WalletResponse get(UUID walletId) {
        Balance balance = balanceRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
        return new WalletResponse(walletId, balance.getAmount());
    }

    /**
     * Либо списываем, либо пополняем в зависимости от типа.
     * Плюс если не смогли изменить баланс в запросе, то вываливаемся с определенной ошибкой.
     * @param request - формализованный запрос
     * @return кошелек
     */
    @Transactional
    public WalletResponse operate(@Valid WalletOperationRequest request) {
        UUID walletId = request.walletId();
        BigDecimal delta = switch (request.operationType()) {
            case DEPOSIT -> request.amount();
            case WITHDRAW -> request.amount().negate();
        };

        int updated = balanceRepository.changeAmount(walletId, delta);
        if (updated == 0) {
            if (!balanceRepository.existsById(walletId)) {
                throw new WalletNotFoundException(walletId);
            }
            throw new InsufficientFundsException(walletId);
        }

        return get(walletId);

    }

    /**
     * Для удобства получение всех кошельков
     * @return list формализованных ответов
     */
    @Transactional(readOnly = true)
    public List<WalletResponse> getAll() {
        return walletRepository.findAllWithBalances();
    }


}

