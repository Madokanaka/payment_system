package kg.attractor.payment_system.controller;

import jakarta.validation.constraints.Min;
import kg.attractor.payment_system.dto.TransactionDto;
import kg.attractor.payment_system.service.AdminService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/transactions")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminTransactionService;

    @GetMapping
    public List<TransactionDto> getAllTransactions() {
        return adminTransactionService.getAllTransactions();
    }

    @GetMapping("/approval")
    public List<TransactionDto> getPendingApprovalTransactions() {
        return adminTransactionService.getPendingApprovalTransactions();
    }

    @PostMapping("/approval")
    public ResponseEntity<?> approveTransaction(@Min(value = 1, message = "Minimal value is 1") @RequestParam Long transactionId) {
        adminTransactionService.approveTransaction(transactionId);
        return ResponseEntity.ok("Transaction approved successfully.");
    }

    @PostMapping("/rollback")
    public ResponseEntity<String> rollbackTransaction(@RequestParam Long transactionId) {
        adminTransactionService.rollbackTransaction(transactionId);
        return ResponseEntity.ok("Transaction rolled back successfully.");
    }
}

