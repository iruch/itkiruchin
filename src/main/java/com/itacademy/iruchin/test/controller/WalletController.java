package com.itacademy.iruchin.test.controller;

import com.itacademy.iruchin.test.dto.WalletOperationRequest;
import com.itacademy.iruchin.test.dto.WalletResponse;
import com.itacademy.iruchin.test.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    /**
     * Получаем кошелек по UUID
     * @param walletId - UUID кошелька
     * @return - кошелек и его баланс
     */
    @GetMapping("/wallets/{walletId}")
    public WalletResponse get(@PathVariable UUID walletId) {
        return walletService.get(walletId);
    }

    /**
     * Списываем или зачисляем деньги на кошелек
     * @param request - сериализованный запрос
     * @return - кошелек и его баланс
     */
    @PostMapping("/wallet")
    public WalletResponse operate(@Valid @RequestBody WalletOperationRequest request) {
        return walletService.operate(request);
    }

    /**
     * Получить список всех кошельков (в ТЗ не было, но для тестов я сделал)
     * @return - список кошельков
     */
    @GetMapping("/wallets")
    public List<WalletResponse> getWallets() {
        return walletService.getAll();
    }

    /**
     * Создаем кошелек (в ТЗ не было, но для тестов я сделал)
     * @return - кошелек и его баланс
     */
    @PostMapping("/wallets")
    @ResponseStatus(HttpStatus.CREATED)
    public WalletResponse createWallet() {
        return walletService.create();
    }
}
