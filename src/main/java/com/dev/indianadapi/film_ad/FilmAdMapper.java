package com.dev.indianadapi.film_ad;

import com.dev.indianadapi.film_ad.dto.FilmAdResponse;
import org.springframework.stereotype.Service;

@Service
public class FilmAdMapper {

    public FilmAdResponse filmAdToResponse(final FilmAd filmAd) {

        if (filmAd == null) return null;

        return FilmAdResponse.builder()
                .id(filmAd.getId())
                .title(filmAd.getTitle())
                .description(filmAd.getDescription())
                .views(filmAd.getViews())
                .tags(filmAd.getTags())
                .imageUrl(filmAd.getImageUrl())
                .userAccountId(filmAd.getUserAccount().getId())
                .creationCost(filmAd.getCreationCost())
                .build();
    }

}
