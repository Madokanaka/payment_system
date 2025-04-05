package kg.attractor.payment_system.service.impl;

import kg.attractor.payment_system.dao.AccountDao;
import kg.attractor.payment_system.dao.TransactionDao;
import kg.attractor.payment_system.dto.TransactionDto;
import kg.attractor.payment_system.exception.TransactionNotFoundException;
import kg.attractor.payment_system.model.Transaction;
import kg.attractor.payment_system.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import kg.attractor.payment_system.exception.AccountNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionDao;
    private final AccountDao accountDao;


    @Override
    public List<TransactionDto> getTransactionHistory(Long accountId) {
        if (!accountDao.existsByAccountId(accountId)) {
            throw new AccountNotFoundException("Account with id " + accountId + " was not found");
        }
        List<Transaction> transactions = transactionDao.findTransactionsByAccountId(accountId);

        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException(
                    "No transactions found for account with id " + accountId);
        }

        return transactions.stream().map(transaction -> {
            String senderAccountNumber = accountDao.findAccountByAccountId(transaction.getSenderAccountId()).getAccountNumber();
            String receiverAccountNumber = accountDao.findAccountByAccountId(transaction.getReceiverAccountId()).getAccountNumber();

            return new TransactionDto(
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
}
