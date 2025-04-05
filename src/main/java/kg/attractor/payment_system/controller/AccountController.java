package kg.attractor.payment_system.controller;

import kg.attractor.payment_system.dto.AccountRequestDto;
import kg.attractor.payment_system.service.AccountService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

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
}
