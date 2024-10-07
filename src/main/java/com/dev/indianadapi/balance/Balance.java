package com.dev.indianadapi.balance;

import com.dev.indianadapi.authentication.entity.UserAccount;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer balance;

    @OneToOne
    @JoinColumn(name = "user_account_id", referencedColumnName = "id")
    @ToString.Exclude
    private UserAccount userAccount;
}
