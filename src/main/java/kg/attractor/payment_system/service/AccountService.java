package kg.attractor.payment_system.service;

import kg.attractor.payment_system.dto.AccountResponseDto;
import kg.attractor.payment_system.dto.BalanceUpdateRequestDto;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountService {
    List<AccountResponseDto> getAccountsForUser(User principal);

    String createAccount(User s, String currencyName);

    String getAccountBalance(String accountNumber);

    @Transactional
    void updateBalance(User principal, BalanceUpdateRequestDto balanceUpdateRequestDto);
}
