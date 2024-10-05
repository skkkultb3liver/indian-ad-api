package com.dev.indianadapi.film_ad.tag;

import com.dev.indianadapi.film_ad.tag.dto.TagRequest;
import com.dev.indianadapi.film_ad.tag.dto.TagResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<TagResponse> createTagHandler(@RequestBody TagRequest tagRequest) {
        TagResponse tagResponse = tagService.createTag(tagRequest);

        return ResponseEntity.ok(tagResponse);
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> findAllTagsHandler() {
        return ResponseEntity.ok(tagService.findAllTags());
    }

}
