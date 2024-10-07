package com.dev.indianadapi.investment;

import org.springframework.stereotype.Service;

@Service
public class InvestmentMapper {

    public InvestmentResponse investmentToResponse(Investment investment) {

        if (investment == null) return null;

        return InvestmentResponse.builder()
                .id(investment.getId())
                .amount(investment.getAmount())
                .filmAdId(investment.getFilmAd().getId())
                .userAccountId(investment.getUserAccount().getId())
                .createdAt(investment.getCreatedAt())
                .updatedAt(investment.getUpdatedAt())
                .build();
    }

}
