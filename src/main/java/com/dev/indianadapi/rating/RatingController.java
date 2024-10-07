package com.dev.indianadapi.rating;

import com.dev.indianadapi.film_ad.dto.FilmAdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/")
    public ResponseEntity<List<FilmAdResponse>> findTop10FilmAds() {

        return ResponseEntity.ok(ratingService.calculateRatings());
    }
}
