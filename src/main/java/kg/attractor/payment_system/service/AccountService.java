package kg.attractor.payment_system.service;

import kg.attractor.payment_system.model.Account;

import java.util.List;

public interface AccountService {
    List<Account> getAccountsForUser(Long userId);

    String createAccount(String s, String currencyName);
}
