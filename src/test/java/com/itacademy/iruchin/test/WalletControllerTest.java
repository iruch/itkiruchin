package com.itacademy.iruchin.test;

import com.itacademy.iruchin.test.controller.WalletController;
import com.itacademy.iruchin.test.dto.WalletResponse;
import com.itacademy.iruchin.test.exception.InsufficientFundsException;
import com.itacademy.iruchin.test.exception.WalletNotFoundException;
import com.itacademy.iruchin.test.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WalletService walletService;

    private final UUID walletId = UUID.randomUUID();

    @Test
    void createWallet() throws Exception {
        when(walletService.create()).thenReturn(new WalletResponse(walletId, BigDecimal.ZERO));

        mockMvc.perform(post("/api/v1/wallets"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.walletId").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value(0));
    }

    @Test
    void getAllWallets() throws Exception {
        when(walletService.getAll()).thenReturn(List.of(
                new WalletResponse(walletId, new BigDecimal("100"))
        ));

        mockMvc.perform(get("/api/v1/wallets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].walletId").value(walletId.toString()))
                .andExpect(jsonPath("$[0].balance").value(100));
    }

    @Test
    void getWallet() throws Exception {
        when(walletService.get(walletId)).thenReturn(new WalletResponse(walletId, new BigDecimal("500")));

        mockMvc.perform(get("/api/v1/wallets/" + walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(500));
    }

    @Test
    void getWalletNotFound() throws Exception {
        when(walletService.get(walletId)).thenThrow(new WalletNotFoundException(walletId));

        mockMvc.perform(get("/api/v1/wallets/" + walletId))
                .andExpect(status().isNotFound());
    }

    @Test
    void deposit() throws Exception {
        when(walletService.operate(any())).thenReturn(new WalletResponse(walletId, new BigDecimal("1000")));

        String json = "{\"walletId\":\"" + walletId + "\",\"operationType\":\"DEPOSIT\",\"amount\":1000}";

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    void withdraw() throws Exception {
        when(walletService.operate(any())).thenReturn(new WalletResponse(walletId, new BigDecimal("700")));

        String json = "{\"walletId\":\"" + walletId + "\",\"operationType\":\"WITHDRAW\",\"amount\":300}";

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(700));
    }

    @Test
    void withdrawNotEnoughMoney() throws Exception {
        when(walletService.operate(any())).thenThrow(new InsufficientFundsException(walletId));

        String json = "{\"walletId\":\"" + walletId + "\",\"operationType\":\"WITHDRAW\",\"amount\":5000}";

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());
    }

    @Test
    void invalidAmount() throws Exception {
        String json = "{\"walletId\":\"" + walletId + "\",\"operationType\":\"DEPOSIT\",\"amount\":-100}";

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidJson() throws Exception {
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{bad json"))
                .andExpect(status().isBadRequest());
    }
}