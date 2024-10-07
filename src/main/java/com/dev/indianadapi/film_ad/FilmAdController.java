package com.dev.indianadapi.film_ad;

import com.dev.indianadapi.film_ad.dto.FilmAdCatalogResponse;
import com.dev.indianadapi.film_ad.dto.FilmAdRequest;
import com.dev.indianadapi.film_ad.dto.FilmAdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/film_ads")
public class FilmAdController {

    private final FilmAdService filmAdService;

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FilmAdResponse> createFilmAdHandler(
            @RequestPart("filmAdRequest") FilmAdRequest request,
            @RequestPart("file") MultipartFile image,
            @RequestHeader("Authorization") String token
    ) {

        FilmAdResponse response = filmAdService.createFilmAd(request, image, token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<FilmAdCatalogResponse> findAllFilmsAdsHandler(
            @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "tagsSlugs", required = false) Set<String> tagsSlugs,
            @RequestParam(value = "sortByViews", required = false, defaultValue = "null") String sortByViews
    ) {

        return ResponseEntity.ok(filmAdService.findAllFilmAds(pageNum, pageSize, tagsSlugs, sortByViews));
    }

    @GetMapping("/{filmAdId}")
    public ResponseEntity<FilmAdResponse> getFilmAdByIdHandler(
            @PathVariable Long filmAdId,
            @RequestHeader("Authorization") String jwt
    ) {

        return ResponseEntity.ok(filmAdService.getFilmAdByIdAndViewerJwt(filmAdId, jwt));
    }



}
