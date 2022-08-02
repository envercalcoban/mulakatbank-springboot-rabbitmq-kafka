package com.mulakatbank.repository;

import com.mulakatbank.model.Account;
import com.mulakatbank.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {

    List<Account> findAllByBalanceGreaterThan(Double balance);

    List<Account> findAllByCurrencyIsAndAndBalanceLessThan(Currency currency, Double balance);







}
