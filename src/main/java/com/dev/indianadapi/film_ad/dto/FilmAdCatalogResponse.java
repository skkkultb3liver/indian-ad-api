package com.dev.indianadapi.film_ad.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class FilmAdCatalogResponse {

    private List<FilmAdResponse> content;
    private int pageNum;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean isLast;
}
