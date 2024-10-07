package com.dev.indianadapi.investment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvestmentResponse {

    private Long id;
    private Integer amount;
    private Long filmAdId;
    private Long userAccountId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
