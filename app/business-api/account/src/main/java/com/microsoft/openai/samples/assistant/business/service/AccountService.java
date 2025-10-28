package com.microsoft.openai.samples.assistant.business.service;

import com.microsoft.openai.samples.assistant.business.models.Account;
import com.microsoft.openai.samples.assistant.business.models.PaymentMethod;
import com.microsoft.openai.samples.assistant.business.models.PaymentMethodSummary;
import com.microsoft.openai.samples.assistant.business.models.Beneficiary;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {

    private final Map<String, Account> accounts;
    private final Map<String, PaymentMethod> paymentMethods;

    public AccountService() {
        this.accounts = new HashMap<>();
        this.paymentMethods = new HashMap<>();
        // Fill the map with dummy data
        this.accounts.put("1000", new Account(
                "1000",
                "carolina.lopez@coemlatam.com",
                "Carolina López",
                "MXN",
                "2021-03-15",
                "125000",
                Arrays.asList(new PaymentMethodSummary("12345", "Tarjeta Visa", "2021-03-15", "2025-03-15"),
                              new PaymentMethodSummary("23456", "Transferencia bancaria", "2021-03-15", "9999-12-31"))));
        this.accounts.put("1010", new Account(
                "1010",
                "diego.ramirez@coemlatam.com",
                "Diego Ramírez",
                "COP",
                "2020-07-01",
                "8400000",
                Arrays.asList(new PaymentMethodSummary("345678", "Transferencia bancaria", "2020-07-01", "9999-12-31"),
                              new PaymentMethodSummary("55555", "Tarjeta Visa", "2020-07-01", "2026-07-01"))));
        this.accounts.put("1020", new Account(
                "1020",
                "luisa.martinez@coemlatam.com",
                "Luisa Martínez",
                "CLP",
                "2022-05-10",
                "2750000",
                Arrays.asList(new PaymentMethodSummary("46748576", "Débito automático", "2022-05-10", "9999-12-31"))));

        this.paymentMethods.put("12345", new PaymentMethod("12345", "Tarjeta Visa", "2021-03-15", "2025-03-15", "15000.00", "5489123498761234"));
        this.paymentMethods.put("55555", new PaymentMethod("55555", "Tarjeta Visa", "2023-02-01", "2027-02-01", "9200.00", "4598120099134472"));
        this.paymentMethods.put("23456", new PaymentMethod("23456", "Transferencia bancaria", "2021-03-15", "9999-12-31", "120000.00", null));
        this.paymentMethods.put("345678", new PaymentMethod("345678", "Transferencia bancaria", "2020-07-01", "9999-12-31", "2100000.00", null));
    }

    public Account getAccountDetails(String accountId) {
        if (accountId == null || accountId.isEmpty())
            throw new IllegalArgumentException("El identificador de cuenta está vacío o es nulo");
        try {
            Integer.parseInt(accountId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El identificador de cuenta no es un número válido");
        }
        // Return account data from the map
        return this.accounts.get(accountId);
    }

    public PaymentMethod getPaymentMethodDetails(String paymentMethodId) {
        if (paymentMethodId == null || paymentMethodId.isEmpty())
            throw new IllegalArgumentException("El identificador del método de pago está vacío o es nulo");
        try {
            Integer.parseInt(paymentMethodId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El identificador del método de pago no es un número válido");
        }
        // Return account data from the map
        return this.paymentMethods.get(paymentMethodId);
    }

    public List<Beneficiary> getRegisteredBeneficiary(String accountId) {
        if (accountId == null || accountId.isEmpty())
            throw new IllegalArgumentException("El identificador de cuenta está vacío o es nulo");
        try {
            Integer.parseInt(accountId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El identificador de cuenta no es un número válido");
        }
        // Return dummy list of beneficiaries
        return Arrays.asList(
                new Beneficiary("1", "Servicios Hidráulicos Rivera", "123456789", "Banco Azteca"),
                new Beneficiary("2", "Electricidad Andina", "987654321", "Banco de Bogotá")
        );
    }
}
