package kg.attractor.payment_system.service;

import kg.attractor.payment_system.dto.TransactionDto;
import kg.attractor.payment_system.dto.TransactionRequestDto;
import kg.attractor.payment_system.model.Transaction;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TransactionService {
    List<TransactionDto> getTransactionHistory(Long accountId);

    @Transactional
    Transaction createTransaction(User principal, TransactionRequestDto requestDto);
}
