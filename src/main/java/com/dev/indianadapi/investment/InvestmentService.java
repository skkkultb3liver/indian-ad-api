package com.dev.indianadapi.investment;

import com.dev.indianadapi.authentication.entity.UserAccount;
import com.dev.indianadapi.authentication.service.CustomUserDetailsService;
import com.dev.indianadapi.balance.Balance;
import com.dev.indianadapi.balance.BalanceService;
import com.dev.indianadapi.film_ad.FilmAd;
import com.dev.indianadapi.film_ad.FilmAdService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvestmentService {

    private final CustomUserDetailsService userDetailsService;
    private final FilmAdService filmAdService;
    private final InvestmentRepository investmentRepository;
    private final BalanceService balanceService;
    private final InvestmentMapper mapper;

    public InvestmentResponse invest(
            String accessToken,
            Long filmAdId,
            Integer amount
    ) {

        UserAccount userAccount = userDetailsService.findUserByJwt(accessToken);
        FilmAd filmAd = filmAdService.getById(filmAdId);

        if (filmAd.getUserAccount().equals(userAccount)) {
            throw new IllegalArgumentException("Вы не можете сделать инвестицию в свою же рекламу");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Вы должны инвестировать какую то сумму");
        }

        if (userAccount.getBalance().getBalance() < amount) {
            throw new IllegalArgumentException("Недостаточно средств на балансе");
        }

        InvestmentResponse response;

        Optional<Investment> existingInvestmentOpt = investmentRepository.findByUserAccountIdAndFilmAdId(
                userAccount.getId(), filmAd.getId());

        if (existingInvestmentOpt.isPresent()) {
            Investment existingInvestment = existingInvestmentOpt.get();
            Integer previousAmount = existingInvestment.getAmount();

            existingInvestment.setAmount(amount);

            if (amount > previousAmount) {
                int difference = amount - previousAmount;
                balanceService.add(userAccount.getId(), difference);
            } else {
                int difference = previousAmount - amount;
                balanceService.deduct(userAccount.getId(), difference);
            }

            existingInvestment.setUpdatedAt(LocalDateTime.now());
            investmentRepository.save(existingInvestment);

            response = mapper.investmentToResponse(existingInvestment);
        }

        else {
            Investment newInvestment = Investment.builder()
                    .amount(amount)
                    .filmAd(filmAd)
                    .userAccount(userAccount)
                    .build();

            balanceService.deduct(userAccount.getId(), amount);

            investmentRepository.save(newInvestment);
            response = mapper.investmentToResponse(newInvestment);
        }

        return response;
    }

    @Transactional
    public void deleteInvestment(
            String investorAccessToken,
            Long filmAdId
    ) {

        UserAccount investor = userDetailsService.findUserByJwt(investorAccessToken);
        Investment deletedInvestment = investmentRepository.findByUserAccountIdAndFilmAdId(investor.getId(), filmAdId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Не удалось найти инвестицию")
                );

        balanceService.add(investor.getId(), deletedInvestment.getAmount());
        deleteInvestment(deletedInvestment);
    }

    public List<InvestmentResponse> getUserAccountInvestments(
            String accessKey
    ) {
        UserAccount userAccount = userDetailsService.findUserByJwt(accessKey);
        List<Investment> investments = investmentRepository.findByUserAccountId(userAccount.getId());

        return investments.stream()
                .map(mapper::investmentToResponse)
                .collect(Collectors.toList());
    }

    private void deleteInvestment(Investment investment) {
        investmentRepository.delete(investment);
    }
}
