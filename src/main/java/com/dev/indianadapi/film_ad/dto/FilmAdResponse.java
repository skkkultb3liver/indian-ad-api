package com.dev.indianadapi.film_ad.dto;

import com.dev.indianadapi.film_ad.tag.Tag;
import lombok.*;

import java.util.Set;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilmAdResponse {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Integer views;
    private Long userAccountId;
    private Set<Tag> tags;

    private Integer creationCost;
}
