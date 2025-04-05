package kg.attractor.payment_system.service.impl;

import kg.attractor.payment_system.dao.AccountDao;
import kg.attractor.payment_system.dao.TransactionDao;
import kg.attractor.payment_system.dto.TransactionDto;
import kg.attractor.payment_system.exception.TransactionNotFoundException;
import kg.attractor.payment_system.model.Account;
import kg.attractor.payment_system.model.Transaction;
import kg.attractor.payment_system.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final TransactionDao transactionDao;
    private final AccountDao accountDao;


    @Override
    public List<TransactionDto> getAllTransactions() {
        List<Transaction> transactions = transactionDao.findAll();
        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException("No transactions found.");
        }
        return toTransactionDtoList(transactions);
    }

    @Override
    public List<TransactionDto> getPendingApprovalTransactions() {
        List<Transaction> transactions = transactionDao.findPendingApprovalTransactions();
        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException(
                    "No pending approval transactions found.");
        }
        return toTransactionDtoList(transactions);
    }

    private List<TransactionDto> toTransactionDtoList(List<Transaction> transactions) {
        return transactions.stream()
                .map(tx -> {
                    Account sender = accountDao.findAccountByAccountId(tx.getSenderAccountId());
                    Account receiver = accountDao.findAccountByAccountId(tx.getReceiverAccountId());

                    return new TransactionDto(
                            tx.getId(),
                            sender.getAccountNumber(),
                            receiver.getAccountNumber(),
                            tx.getAmount(),
                            tx.getStatus(),
                            tx.getTransactionType(),
                            tx.getCreatedAt(),
                            tx.getApprovedAt()
                    );
                })
                .collect(Collectors.toList());
    }
}
