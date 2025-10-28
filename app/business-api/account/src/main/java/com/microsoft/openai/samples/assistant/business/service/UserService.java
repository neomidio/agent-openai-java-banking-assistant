package com.microsoft.openai.samples.assistant.business.service;

import com.microsoft.openai.samples.assistant.business.models.Account;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserService {


    private Map<String, Account> accounts = new HashMap<>();

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
                        null
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
                        null
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
                        null
                )
        );

     }
    public List<Account> getAccountsByUserName(String userName) {
        return Arrays.asList(accounts.get(userName));
    }


}
