package com.dev.indianadapi.rating;

import com.dev.indianadapi.authentication.entity.UserAccount;
import com.dev.indianadapi.balance.Balance;
import com.dev.indianadapi.balance.BalanceService;
import com.dev.indianadapi.film_ad.FilmAd;
import com.dev.indianadapi.film_ad.FilmAdMapper;
import com.dev.indianadapi.film_ad.FilmAdService;
import com.dev.indianadapi.film_ad.dto.FilmAdResponse;
import com.dev.indianadapi.investment.Investment;
import com.dev.indianadapi.investment.InvestmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final FilmAdService filmAdService;
    private final BalanceService balanceService;

    public List<FilmAdResponse> calculateRatings() {

        List<FilmAd> top10Ads = filmAdService.findTop10OrderByTotalInvestment();

        returnInvestmentsBasedOnRank(top10Ads);

        return top10Ads.stream()
                .map(ad -> FilmAdResponse.builder()
                        .id(ad.getId())
                        .userAccountId(ad.getUserAccount().getId())
                        .tags(ad.getTags())
                        .views(ad.getViews())
                        .title(ad.getTitle())
                        .description(ad.getDescription())
                        .imageUrl(ad.getImageUrl())
                        .build())
                .collect(Collectors.toList());

    }

    private void returnInvestmentsBasedOnRank(List<FilmAd> top10Ads) {

        for (int i = 0; i < top10Ads.size(); i++) {

            FilmAd ad = top10Ads.get(i);
            int multiplier = calculateMultiplierForRank(i + 1);
            int investmentsSum = 0;

            for (Investment investment : ad.getInvestments()) {

                investmentsSum += investment.getAmount();

                int returnedAmount = investment.getAmount() * multiplier / 100;
                UserAccount investor = investment.getUserAccount();

                balanceService.add(investor.getId(), returnedAmount);
            }

            UserAccount creator = ad.getUserAccount();
            balanceService.add(creator.getId(), investmentsSum * 70 / 100);
        }

        List<FilmAd> remainingAds = filmAdService.findAllOutsideTop();

        for (FilmAd ad : remainingAds) {

            for (Investment investment : ad.getInvestments()) {

                int returnedAmount = investment.getAmount() * 80 / 100;
                UserAccount investor = investment.getUserAccount();

                balanceService.add(investor.getId(), returnedAmount);
            }

        }
    }

    private int calculateMultiplierForRank(int rank) {
        return switch (rank) {
            case 1 -> 200;
            case 2 -> 150;
            case 3 -> 125;
            default -> 110;
        };
    }

}
