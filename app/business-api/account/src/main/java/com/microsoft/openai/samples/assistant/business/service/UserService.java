package com.microsoft.openai.samples.assistant.business.service;

import com.microsoft.openai.samples.assistant.business.models.Account;
import com.microsoft.openai.samples.assistant.business.models.PaymentMethodSummary;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserService {


    private final Map<String, Account> accounts = new HashMap<>();

    public UserService() {
        accounts.put(
                "carolina.lopez@coemlatam.com",
                new Account(
                        "1000",
                        "carolina.lopez@coemlatam.com",
                        "Carolina López",
                        "MXN",
                        "2021-03-15",
                        "125000",
                        List.of(
                                new PaymentMethodSummary("12345", "Tarjeta Visa", "2021-03-15", "2025-03-15"),
                                new PaymentMethodSummary("23456", "Transferencia bancaria", "2021-03-15", "9999-12-31")
                        )
                )
        );
        accounts.put(
                "diego.ramirez@coemlatam.com",
                new Account(
                        "1010",
                        "diego.ramirez@coemlatam.com",
                        "Diego Ramírez",
                        "COP",
                        "2020-07-01",
                        "8400000",
                        List.of(
                                new PaymentMethodSummary("345678", "Transferencia bancaria", "2020-07-01", "9999-12-31"),
                                new PaymentMethodSummary("55555", "Tarjeta Visa", "2020-07-01", "2026-07-01")
                        )
                )
        );
        accounts.put(
                "luisa.martinez@coemlatam.com",
                new Account(
                        "1020",
                        "luisa.martinez@coemlatam.com",
                        "Luisa Martínez",
                        "CLP",
                        "2022-05-10",
                        "2750000",
                        List.of(
                                new PaymentMethodSummary("46748576", "Débito automático", "2022-05-10", "9999-12-31")
                        )
                )
        );

     }
    public List<Account> getAccountsByUserName(String userName) {
        Account account = accounts.get(userName);
        if (account == null) {
            return List.of();
        }
        return List.of(account);
    }


}
