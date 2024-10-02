package com.fiuni.marketplacefreelancer.controller;

import com.fiuni.marketplacefreelancer.dto.Project.ProjectResult;
import com.fiuni.marketplacefreelancer.dto.Tag.TagDTO;
import com.fiuni.marketplacefreelancer.dto.Tag.TagResult;
import com.fiuni.marketplacefreelancer.service.tag.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping
    public ResponseEntity<TagResult> getAll(Pageable pageable) {
        TagResult tagResult = tagService.getAll(pageable);
        return new ResponseEntity<>(tagResult, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody TagDTO tag) {
        TagDTO savedTag = tagService.save(tag);
        return new ResponseEntity<>(savedTag, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        TagDTO tag = tagService.getById(id);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getByName(@PathVariable String name) {
        TagDTO tag = tagService.getByName(name);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody TagDTO tag) {
        TagDTO updatedTag = tagService.update(id, tag);
        return new ResponseEntity<>(updatedTag, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        Boolean deleted = tagService.delete(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
