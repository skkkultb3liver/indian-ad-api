package com.dev.indianadapi.film_ad;

import com.dev.indianadapi.authentication.entity.UserAccount;
import com.dev.indianadapi.authentication.service.CustomUserDetailsService;
import com.dev.indianadapi.film_ad.dto.FilmAdRequest;
import com.dev.indianadapi.film_ad.dto.FilmAdResponse;
import com.dev.indianadapi.film_ad.tag.TagService;
import com.dev.indianadapi.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmAdService {

    private final FilmAdRepository repository;
    private final TagService tagService;
    private final CustomUserDetailsService userDetailsService;
    private final S3Service s3Service;
    private final FilmAdMapper filmAdMapper;

    public FilmAdResponse createFilmAd(
            FilmAdRequest request,
            String accessToken
    ) {

        String FOLDER_NAME = "film_ad_image";

        FilmAd filmAd = FilmAd.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .userAccount(
                        userDetailsService.findUserByJwt(accessToken)
                )
                .tags(
                        request.getTagsSlugs().stream().map(
                                tagService::findTagBySlug
                        )
                        .collect(Collectors.toSet())
                )
                .build();

        return filmAdMapper.filmAdToResponse(
                repository.save(filmAd)
        );
    }

    public List<FilmAdResponse> findAllFilmAds() {

        return repository.findAll().stream().map(
                filmAdMapper::filmAdToResponse
        ).collect(Collectors.toList());
    }

}
