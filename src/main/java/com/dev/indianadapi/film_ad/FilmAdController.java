package com.dev.indianadapi.film_ad;

import com.dev.indianadapi.film_ad.dto.FilmAdRequest;
import com.dev.indianadapi.film_ad.dto.FilmAdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/film_ads")
public class FilmAdController {

    private final FilmAdService filmAdService;

    @PostMapping("/")
    public ResponseEntity<FilmAdResponse> createFilmAdHandler(
            @RequestBody FilmAdRequest request,
            @RequestHeader("Authorization") String token
    ) {
        FilmAdResponse response = filmAdService.createFilmAd(request, token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<FilmAdResponse>> getFilmAdsHandler() {
        return ResponseEntity.ok(filmAdService.findAllFilmAds());
    }

}
