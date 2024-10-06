package com.dev.indianadapi.film_ad.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class FilmAdRequest {

    private String title;
    private String description;
    private Set<String> tagsSlugs;


}
