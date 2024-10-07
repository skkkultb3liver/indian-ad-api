package com.dev.indianadapi.balance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/balance")
@RequiredArgsConstructor
@Slf4j
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/{userAccountId}")
    public ResponseEntity<Balance> getBalance(@PathVariable Long userAccountId) {

        log.info("Getting balance for user {}", userAccountId);

        Balance response = balanceService.getBalanceByUserAccountId(userAccountId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCoins(@RequestParam Long userId, @RequestParam int amount) {
        balanceService.add(userId, amount);
        return ResponseEntity.ok("Успешно");
    }

    @PostMapping("/deduct")
    public ResponseEntity<?> deductCoins(@RequestParam Long userId, @RequestParam int amount) {
        balanceService.deduct(userId, amount);
        return ResponseEntity.ok("Успешно");
    }

}
