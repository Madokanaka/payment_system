package kg.attractor.payment_system.service;

import kg.attractor.payment_system.dto.TransactionDto;

import java.util.List;

public interface AdminService {
    List<TransactionDto> getAllTransactions();

    List<TransactionDto> getPendingApprovalTransactions();

    void approveTransaction(Long transactionId);
}
