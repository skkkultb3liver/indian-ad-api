package com.dev.indianadapi.film_ad;

import com.dev.indianadapi.film_ad.tag.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FilmAdRepository extends JpaRepository<FilmAd, Long> {

    List<FilmAd> findAllByUserAccountId(Long userAccountId);

    @Query("SELECT f FROM FilmAd f JOIN f.tags t WHERE t.slug IN :tagsSlugs")
    Page<FilmAd> findByTagsSlugsIn(@Param("tagsSlugs") Set<String> tagsSlugs, Pageable pageable);
}
