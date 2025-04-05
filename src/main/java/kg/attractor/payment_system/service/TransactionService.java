package kg.attractor.payment_system.service;

import kg.attractor.payment_system.dto.TransactionDto;

import java.util.List;

public interface TransactionService {
    List<TransactionDto> getTransactionHistory(Long accountId);
}
