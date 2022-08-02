package com.mulakatbank.dto;

import com.mulakatbank.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountDtoMapper {

    public AccountDto mapper(Account account){
        return AccountDto.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .customerId(account.getCustomerId())
                .build();
    }
}
