package kg.attractor.payment_system.service.impl;

import kg.attractor.payment_system.dao.AccountDao;
import kg.attractor.payment_system.dao.CurrencyDao;
import kg.attractor.payment_system.dao.TransactionDao;
import kg.attractor.payment_system.dao.UserDao;
import kg.attractor.payment_system.dto.AccountResponseDto;
import kg.attractor.payment_system.dto.BalanceUpdateRequestDto;
import kg.attractor.payment_system.exception.BadRequestException;
import kg.attractor.payment_system.exception.CurrencyNotFoundException;
import kg.attractor.payment_system.exception.AccountNotFoundException;
import kg.attractor.payment_system.model.Account;
import kg.attractor.payment_system.model.Currency;
import kg.attractor.payment_system.model.Transaction;
import kg.attractor.payment_system.service.AccountService;
import kg.attractor.payment_system.util.AccountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;



import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDAO;

    @Autowired
    private CurrencyDao currencyDAO;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TransactionDao transactionDao;

    @Override
    public List<AccountResponseDto> getAccountsForUser(User principal) {
        if (principal == null) {
            log.warn("User is not authenticated");
            throw new BadRequestException("User is not authenticated");
        }
        Long userId = userDao.getIdByPhoneNumber(principal.getUsername());
        List<Account> accounts = accountDAO.findAccountsByUserId(userId);
        if (accounts.isEmpty()) {
            log.warn("No accounts found for user with ID: {}", userId);
            throw new AccountNotFoundException("User has no accounts yet");
        }
        return accounts.stream()
                .map(account -> new AccountResponseDto(account.getAccountNumber(), account.getBalance()))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public String createAccount(User principal, String currencyName) {
        if (principal == null) {
            log.warn("User is not authenticated");
            throw new BadRequestException("User is not authenticated");
        }
        Currency currency = currencyDAO.findCurrencyByName(currencyName.toUpperCase());

        if (currency == null) {
            log.error("Currency '{}' not found", currencyName);
            throw new CurrencyNotFoundException("Currency not found");
        }


        String accountNumber = AccountUtil.generateAccountNumber();

        Long userId = userDao.getIdByPhoneNumber(principal.getUsername());

        int userAccountCount = accountDAO.countAccountsByUserId(userId);
        if (userAccountCount >= 3) {
            log.error("User with ID {} has reached the maximum limit of accounts (3)", userId);
            throw new BadRequestException("A user can have no more than 3 accounts");
        }

        int userCurrencyAccountCount = accountDAO.countAccountsByUserIdAndCurrency(userId, currency.getId());
        if (userCurrencyAccountCount >= 1) {
            log.error("User with ID {} already has an account in currency {}", userId, currencyName);
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
            log.error("Account with account number '{}' was not found", accountNumber);
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
            log.warn("User is not authenticated");
            throw new BadRequestException("User is not authenticated");
        }
        String accountNumber = balanceUpdateRequestDto.getAccountNumber();
        BigDecimal amount = balanceUpdateRequestDto.getAmount();

        if (!accountDAO.existsByAccountNumber(accountNumber)) {
            log.error("Account with account number '{}' was not found", accountNumber);
            throw new AccountNotFoundException("Account with the account number " + accountNumber + " was not found");
        }
        Account account = accountDAO.findAccountByAccountNumber(accountNumber);

        Long userId = userDao.getIdByPhoneNumber(principal.getUsername());
        if (!userId.equals(account.getUserId())) {
            log.error("User with ID {} tried to update balance for an account that does not belong to them", userId);
            throw new BadRequestException("Account belongs to another user");
        }

        account.setBalance(account.getBalance().add(amount));

        accountDAO.updateBalance(account);
        Long accountId = account.getId();
        Transaction transaction = new Transaction().builder().
                senderAccountId(accountId).
                receiverAccountId(accountId).
                amount(amount).
                status("COMPLETED").
                transactionType("BALANCE_UPDATE").
                createdAt(new java.sql.Timestamp(new java.util.Date().getTime())).
                approvedAt(new java.sql.Timestamp(new java.util.Date().getTime())).
                build();

        transactionDao.createTransaction(transaction);
    }

}
