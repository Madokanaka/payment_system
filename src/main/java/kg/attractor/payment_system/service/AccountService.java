package kg.attractor.payment_system.service;

import kg.attractor.payment_system.model.Account;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface AccountService {
    List<Account> getAccountsForUser(Long userId);

    String createAccount(User s, String currencyName);
}
