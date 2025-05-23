package kg.attractor.payment_system.controller;

import jakarta.validation.Valid;
import kg.attractor.payment_system.dto.TransactionDto;
import kg.attractor.payment_system.dto.TransactionRequestDto;
import kg.attractor.payment_system.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{accountId}/history")
    public List<TransactionDto> getTransactionHistory(
            @AuthenticationPrincipal User principal,
            @PathVariable Long accountId) {

        return transactionService.getTransactionHistory(accountId);
    }

    @PostMapping
    public ResponseEntity<String> makeTransaction(@RequestBody @Valid TransactionRequestDto request,
                                                  @AuthenticationPrincipal User principal) {
        transactionService.createTransaction(principal, request);
        return ResponseEntity.ok("Transaction created successfully.");
    }
}


