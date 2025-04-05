package kg.attractor.payment_system.service.impl;

import kg.attractor.payment_system.dao.AccountDao;
import kg.attractor.payment_system.dao.CurrencyDao;
import kg.attractor.payment_system.dao.UserDao;
import kg.attractor.payment_system.dto.BalanceUpdateRequestDto;
import kg.attractor.payment_system.exception.BadRequestException;
import kg.attractor.payment_system.exception.CurrencyNotFoundException;
import kg.attractor.payment_system.exception.AccountNotFoundException;
import kg.attractor.payment_system.model.Account;
import kg.attractor.payment_system.model.Currency;
import kg.attractor.payment_system.service.AccountService;
import kg.attractor.payment_system.util.AccountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;



import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDAO;

    @Autowired
    private CurrencyDao currencyDAO;

    @Autowired
    private UserDao userDao;

    @Override
    public List<Account> getAccountsForUser(Long userId) {
        return accountDAO.findAccountsByUserId(userId);
    }

    @Transactional
    @Override
    public String createAccount(User principal, String currencyName) {
        if (principal == null) {
            throw new BadRequestException("User is not authenticated");
        }
        Currency currency = currencyDAO.findCurrencyByName(currencyName);

        if (currency == null) {
            throw new CurrencyNotFoundException("Currency not found");
        }


        String accountNumber = AccountUtil.generateAccountNumber();

        Long userId = userDao.getIdByPhoneNumber(principal.getUsername());

        int userAccountCount = accountDAO.countAccountsByUserId(userId);
        if (userAccountCount >= 3) {
            throw new BadRequestException("A user can have no more than 3 accounts");
        }

        int userCurrencyAccountCount = accountDAO.countAccountsByUserIdAndCurrency(userId, currency.getId());
        if (userCurrencyAccountCount >= 1) {
            throw new BadRequestException("A user can have no more than one account per currency");
        }

        while (accountDAO.existsByAccountNumber(accountNumber)) {
            accountNumber = AccountUtil.generateAccountNumber();
        }

        accountDAO.createAccount(userId, currency.getId(), accountNumber);
        return accountNumber;
    }

    @Override
    public String getAccountBalance(String accountNumber) {
        if (!accountDAO.existsByAccountNumber(accountNumber)) {
            throw new AccountNotFoundException("Account with the account number " + accountNumber + " was not found");
        }

        Account account = accountDAO.findAccountByAccountNumber(accountNumber);

        BigDecimal balance = account.getBalance();
        DecimalFormat df = new DecimalFormat("0.00");
        String formattedBalance = df.format(balance);

        return "Account balance: " + formattedBalance;
    }

    @Transactional
    @Override
    public void updateBalance(User principal, BalanceUpdateRequestDto balanceUpdateRequestDto) {
        if (principal == null) {
            throw new BadRequestException("User is not authenticated");
        }
        String accountNumber = balanceUpdateRequestDto.getAccountNumber();
        BigDecimal amount = balanceUpdateRequestDto.getAmount();

        if (!accountDAO.existsByAccountNumber(accountNumber)) {
            throw new AccountNotFoundException("Account with the account number " + accountNumber + " was not found");
        }
        Account account = accountDAO.findAccountByAccountNumber(accountNumber);

        Long userId = userDao.getIdByPhoneNumber(principal.getUsername());
        if (!userId.equals(account.getUserId())) {
            throw new BadRequestException("Account belongs to another user");
        }

        account.setBalance(account.getBalance().add(amount));

        accountDAO.updateBalance(account);
    }

}
