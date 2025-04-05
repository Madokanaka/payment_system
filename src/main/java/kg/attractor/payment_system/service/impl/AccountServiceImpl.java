package kg.attractor.payment_system.service.impl;

import kg.attractor.payment_system.dao.AccountDao;
import kg.attractor.payment_system.dao.CurrencyDao;
import kg.attractor.payment_system.dao.UserDao;
import kg.attractor.payment_system.exception.CurrencyNotFoundException;
import kg.attractor.payment_system.model.Account;
import kg.attractor.payment_system.model.Currency;
import kg.attractor.payment_system.service.AccountService;
import kg.attractor.payment_system.util.AccountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public String createAccount(String userNumber, String currencyName) {
        Currency currency = currencyDAO.findCurrencyByName(currencyName);
        if (currency == null) {
            throw new CurrencyNotFoundException("Currency not found");
        }


        String accountNumber = AccountUtil.generateAccountNumber();

        Long userId = userDao.getIdByPhoneNumber(userNumber);

        while (accountDAO.existsByAccountNumber(accountNumber)) {
            accountNumber = AccountUtil.generateAccountNumber();
        }

        accountDAO.createAccount(userId, currency.getId(), accountNumber);
        return accountNumber;
    }
}
