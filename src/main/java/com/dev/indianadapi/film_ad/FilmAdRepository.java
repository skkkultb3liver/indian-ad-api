package com.dev.indianadapi.film_ad;

import com.dev.indianadapi.film_ad.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FilmAdRepository extends JpaRepository<FilmAd, Long> {

    List<FilmAd> findAllByUserAccountId(Long userAccountId);
    List<FilmAd> findByTagsIn(Set<Tag> tags);
}
