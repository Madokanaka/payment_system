package kg.attractor.payment_system.controller;

import kg.attractor.payment_system.dto.TransactionDto;
import kg.attractor.payment_system.service.AdminService;
import lombok.RequiredArgsConstructor;

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

}
