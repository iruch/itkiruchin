package com.itacademy.iruchin.test.common;

import lombok.Getter;

@Getter
public enum OperationType {

    DEPOSIT("Пополнение"),
    WITHDRAW("Списание");

    private final String title;

    OperationType(String title) {
        this.title = title;
    }

}
