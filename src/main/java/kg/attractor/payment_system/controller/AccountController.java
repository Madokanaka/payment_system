package kg.attractor.payment_system.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kg.attractor.payment_system.dto.AccountRequestDto;
import kg.attractor.payment_system.dto.AccountResponseDto;
import kg.attractor.payment_system.dto.BalanceUpdateRequestDto;
import kg.attractor.payment_system.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public String createAccount(@AuthenticationPrincipal User principal, @RequestBody AccountRequestDto accountRequestDto) {
        return accountService.createAccount(principal,  accountRequestDto.getCurrency());
    }

    @GetMapping("/balance")
    public String getAccountBalance(
            @RequestParam
            @Size(min = 20, max = 20, message = "Account number must be exactly 20 digits long.")
            @Pattern(regexp = "^[0-9]+$", message = "Account number must contain only digits.")
            String accountNumber) {

        return accountService.getAccountBalance(accountNumber);
    }

    @PostMapping("/balance")
    public ResponseEntity<String> updateBalance(
            @AuthenticationPrincipal User principal,
            @Valid @RequestBody BalanceUpdateRequestDto requestDto) {

        accountService.updateBalance(principal, requestDto);

        return ResponseEntity.ok("Balance updated successfully!");
    }

    @GetMapping
    public List<AccountResponseDto> getUserAccounts(@AuthenticationPrincipal User principal) {
        return accountService.getAccountsForUser(principal);
    }
}
