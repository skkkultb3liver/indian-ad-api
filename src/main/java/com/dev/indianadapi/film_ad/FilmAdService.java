package com.dev.indianadapi.film_ad;

import com.dev.indianadapi.authentication.entity.UserAccount;
import com.dev.indianadapi.authentication.service.CustomUserDetailsService;
import com.dev.indianadapi.authentication.service.UserAccountService;
import com.dev.indianadapi.film_ad.dto.FilmAdCatalogResponse;
import com.dev.indianadapi.film_ad.dto.FilmAdRequest;
import com.dev.indianadapi.film_ad.dto.FilmAdResponse;
import com.dev.indianadapi.film_ad.tag.Tag;
import com.dev.indianadapi.film_ad.tag.TagService;
import com.dev.indianadapi.s3.S3Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
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
            MultipartFile image,
            String accessToken
    ) {

        FilmAd filmAd = FilmAd.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .imageUrl(s3Service.uploadImage(image, "film_ad_image"))
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

    public FilmAdCatalogResponse findAllFilmAds(
            int pageNum,
            int pageSize,
            Set<String> tagsSlugs,
            String sortByViews
    ) {

        Sort sort = Sort.unsorted();

        if (sortByViews.equalsIgnoreCase("asc")) {
            sort = Sort.by("views").ascending();
        } else if (sortByViews.equalsIgnoreCase("desc")) {
            sort = Sort.by("views").descending();
        }

        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<FilmAd> filmAds;

        if (tagsSlugs != null && !tagsSlugs.isEmpty()) {
            filmAds = repository.findByTagsSlugsIn(tagsSlugs, pageable);
        }
        else {
            filmAds = repository.findAll(pageable);
        }

        List<FilmAd> listOfFilmAds = filmAds.getContent();


        return FilmAdCatalogResponse.builder()
                .content(listOfFilmAds.stream().map(
                        filmAdMapper::filmAdToResponse
                ).collect(Collectors.toList()))
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalElements(filmAds.getTotalElements())
                .totalPages(filmAds.getTotalPages())
                .isLast(filmAds.isLast())
                .build();

    }

    public FilmAdResponse getFilmAdById(
            Long filmAdId,
            String viewerJwt
    ) {

        UserAccount viewer = userDetailsService.findUserByJwt(viewerJwt);
        FilmAd filmAd = repository.findById(filmAdId).orElseThrow(
                () -> new EntityNotFoundException("Не удалось найти рекламу")
        );

        if (!filmAd.getViewedByUserAccounts().contains(viewer) && !filmAd.getUserAccount().equals(viewer)) {
            filmAd.getViewedByUserAccounts().add(viewer);

            repository.save(filmAd);
        }

        return filmAdMapper.filmAdToResponse(filmAd);

    }

    public List<FilmAdResponse> findFilmAdsByUserAccountId(Long userAccountId) {

        return repository.findAllByUserAccountId(userAccountId).stream().map(
                filmAdMapper::filmAdToResponse
        ).collect(Collectors.toList());
    }

}
