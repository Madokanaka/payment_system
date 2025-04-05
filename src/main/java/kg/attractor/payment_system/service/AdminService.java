package kg.attractor.payment_system.service;

import kg.attractor.payment_system.dto.TransactionDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AdminService {
    List<TransactionDto> getAllTransactions();

    List<TransactionDto> getPendingApprovalTransactions();

    void approveTransaction(Long transactionId);

    @Transactional
    void rollbackTransaction(Long transactionId);

    @Transactional
    void deleteTransaction(Long transactionRollbackId);
}
