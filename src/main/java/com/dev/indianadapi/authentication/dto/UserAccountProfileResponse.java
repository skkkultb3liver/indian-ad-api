package com.dev.indianadapi.authentication.dto;

import com.dev.indianadapi.film_ad.dto.FilmAdResponse;
import com.dev.indianadapi.investment.InvestmentResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountProfileResponse {

    private String uid;
    private String avatarUrl;
    private List<FilmAdResponse> filmAds;
    private String email;
    private int balance;

    private List<InvestmentResponse> investments;

}
