package com.dev.indianadapi.film_ad.tag;

import com.dev.indianadapi.film_ad.tag.dto.TagRequest;
import com.dev.indianadapi.film_ad.tag.dto.TagResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagRepository repository;
    private final TagMapper mapper;

    public TagResponse createTag(TagRequest request) {

        String slug = generateSlug(request.name());

        Tag tag = Tag.builder()
                .name(request.name())
                .slug(slug)
                .build();

        return mapper.tagToResponse(repository.save(tag));
    }

    private String generateSlug(String name) {
        return name.trim().toLowerCase().replaceAll("\\s+", "-");
    }

    public List<TagResponse> findAllTags() {

        return repository.findAll().stream().map(
                mapper::tagToResponse
        ).collect(Collectors.toList());
    }

    public Tag findTagBySlug(String slug) {

        return repository.findBySlug(slug).orElseThrow(
                        () -> new IllegalArgumentException("Не удалось найти тег " + slug)
                );
    }
}
