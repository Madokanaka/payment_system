package kg.attractor.payment_system.service.impl;

import kg.attractor.payment_system.dao.AccountDao;
import kg.attractor.payment_system.dao.CurrencyDao;
import kg.attractor.payment_system.dao.TransactionDao;
import kg.attractor.payment_system.dao.UserDao;
import kg.attractor.payment_system.dto.TransactionDto;
import kg.attractor.payment_system.dto.TransactionRequestDto;
import kg.attractor.payment_system.exception.BadRequestException;
import kg.attractor.payment_system.exception.TransactionNotFoundException;
import kg.attractor.payment_system.model.Account;
import kg.attractor.payment_system.model.Currency;
import kg.attractor.payment_system.model.Transaction;
import kg.attractor.payment_system.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import kg.attractor.payment_system.exception.AccountNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final AccountDao accountDao;
    private final CurrencyDao currencyDao;
    private final TransactionDao transactionDao;
    private final UserDao userDao;


    @Override
    public List<TransactionDto> getTransactionHistory(Long accountId) {
        if (!accountDao.existsByAccountId(accountId)) {
            log.error("Account with id {} not found", accountId);
            throw new AccountNotFoundException("Account with id " + accountId + " was not found");
        }
        List<Transaction> transactions = transactionDao.findTransactionsByAccountId(accountId);

        if (transactions.isEmpty()) {
            log.warn("No transactions found for account id: {}", accountId);
            throw new TransactionNotFoundException(
                    "No transactions found for account with id " + accountId);
        }

        return transactions.stream().map(transaction -> {
            String senderAccountNumber = accountDao.findAccountByAccountId(transaction.getSenderAccountId()).getAccountNumber();
            String receiverAccountNumber = accountDao.findAccountByAccountId(transaction.getReceiverAccountId()).getAccountNumber();

            return new TransactionDto(
                    transaction.getId(),
                    senderAccountNumber,
                    receiverAccountNumber,
                    transaction.getAmount(),
                    transaction.getStatus(),
                    transaction.getTransactionType(),
                    transaction.getCreatedAt(),
                    transaction.getApprovedAt()
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Transaction createTransaction(User principal, TransactionRequestDto requestDto) {
        if (principal == null) {
            log.error("User is not authenticated.");
            throw new BadRequestException("User is not authenticated");
        }

        if (requestDto.getReceiverAccountNumber().equals(requestDto.getSenderAccountNumber())) {
            log.error("Invalid request: sender and receiver account numbers are the same.");
            throw new BadRequestException("Invalid request, account numbers can't be the same.");
        }
        Long userId = userDao.getIdByPhoneNumber(principal.getUsername());
        if (!accountDao.existsByAccountNumber(requestDto.getReceiverAccountNumber())){
            log.error("Receiver account with number {} not found", requestDto.getReceiverAccountNumber());
            throw new AccountNotFoundException("Account with the account number " + requestDto.getReceiverAccountNumber() + " was not found");
        }
        if (!accountDao.existsByAccountNumber(requestDto.getSenderAccountNumber())){
            log.error("Sender account with number {} not found", requestDto.getSenderAccountNumber());
            throw new AccountNotFoundException("Account with the account number " + requestDto.getReceiverAccountNumber() + " was not found");
        }

        Account senderAccount = accountDao.findAccountByAccountNumber(requestDto.getSenderAccountNumber());

        if (!senderAccount.getUserId().equals(userId)) {
            log.error("User {} is not authorized to make transactions from account {}", userId, senderAccount.getAccountNumber());
            throw new BadRequestException("You are not authorized to make transactions from this account.");
        }

        Account receiverAccount = accountDao.findAccountByAccountNumber(requestDto.getReceiverAccountNumber());

        if (senderAccount.getBalance().compareTo(requestDto.getAmount()) < 0) {
            log.error("Insufficient balance for transaction from account {} to account {}. Requested: {}, Available: {}",
                    senderAccount.getAccountNumber(), receiverAccount.getAccountNumber(), requestDto.getAmount(), senderAccount.getBalance());
            throw new BadRequestException("Insufficient balance for transaction.");
        }

        Currency senderCurrency = currencyDao.findCurrencyById(senderAccount.getCurrencyId());
        Currency receiverCurrency = currencyDao.findCurrencyById(receiverAccount.getCurrencyId());
        if (!senderCurrency.getCurrencyName().equals(receiverCurrency.getCurrencyName())) {
            log.error("Currency mismatch: sender account {} and receiver account {} have different currencies.",
                    senderAccount.getAccountNumber(), receiverAccount.getAccountNumber());
            throw new BadRequestException("Transaction can only be made to accounts with the same currency.");
        }

        if (requestDto.getAmount().compareTo(new BigDecimal(10)) > 0) {
            return initiatePendingTransaction(senderAccount, receiverAccount, requestDto.getAmount());
        } else {
            return processCompletedTransaction(senderAccount, receiverAccount, requestDto.getAmount());
        }
    }

    private Transaction initiatePendingTransaction(Account senderAccount, Account receiverAccount, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setSenderAccountId(senderAccount.getId());
        transaction.setReceiverAccountId(receiverAccount.getId());
        transaction.setAmount(amount);
        transaction.setStatus("PENDING");
        transaction.setTransactionType("TRANSFER");
        transaction.setCreatedAt(new java.sql.Timestamp(new Date().getTime()));
        transaction.setApprovedAt(null);

        transactionDao.createTransaction(transaction);

        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        receiverAccount.setBalance(receiverAccount.getBalance().add(amount));
        accountDao.updateBalance(senderAccount);
        accountDao.updateBalance(receiverAccount);

        return transaction;
    }

    private Transaction processCompletedTransaction(Account senderAccount, Account receiverAccount, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setSenderAccountId(senderAccount.getId());
        transaction.setReceiverAccountId(receiverAccount.getId());
        transaction.setAmount(amount);
        transaction.setStatus("COMPLETED");
        transaction.setTransactionType("TRANSFER");
        transaction.setCreatedAt(new java.sql.Timestamp(new Date().getTime()));
        transaction.setApprovedAt(new java.sql.Timestamp(new Date().getTime()));

        transactionDao.createTransaction(transaction);

        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        receiverAccount.setBalance(receiverAccount.getBalance().add(amount));
        accountDao.updateBalance(senderAccount);
        accountDao.updateBalance(receiverAccount);

        return transaction;
    }
}
