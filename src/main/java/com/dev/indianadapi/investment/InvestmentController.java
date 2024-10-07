package com.dev.indianadapi.investment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/investments")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;

    @GetMapping("/")
    public ResponseEntity<List<InvestmentResponse>> getUserAccountInvestmentsHandler(
            @RequestHeader("Authorization") String accessToken
    ) {

        return ResponseEntity.ok(investmentService.getUserAccountInvestments(accessToken));
    }

    @PostMapping("/invest")
    public ResponseEntity<String> investHandler(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam Long filmId,
            @RequestParam Integer amount
    ) {
        investmentService.invest(accessToken, filmId, amount);

        return ResponseEntity.ok("Успешно");
    }
}
