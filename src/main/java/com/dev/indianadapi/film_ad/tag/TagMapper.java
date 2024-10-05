package com.dev.indianadapi.film_ad.tag;

import com.dev.indianadapi.film_ad.tag.dto.TagResponse;
import org.springframework.stereotype.Service;

@Service
public class TagMapper {

    public TagResponse tagToResponse(Tag tag) {

        if (tag == null) return null;

        return new TagResponse(
                tag.getId(),
                tag.getName(),
                tag.getSlug()
        );
    }

}
