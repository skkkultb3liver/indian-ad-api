package com.dev.indianadapi.film_ad;

import com.dev.indianadapi.authentication.entity.UserAccount;
import com.dev.indianadapi.film_ad.tag.Tag;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "film_ad")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilmAd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String photoUrl;

    private Integer views;
    private Integer investedCoins;

    @ManyToOne
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @ManyToMany
    @JoinTable(
            name = "film_ad_tags",
            joinColumns = @JoinColumn(name = "film_ad_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();
}
