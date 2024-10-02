package com.dev.indianadapi.authentication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "refresh_token")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private Instant expiresAt;

    @OneToOne
    @JoinColumn(name = "user_account_id", referencedColumnName = "id")
    private UserAccount userAccount;
}
