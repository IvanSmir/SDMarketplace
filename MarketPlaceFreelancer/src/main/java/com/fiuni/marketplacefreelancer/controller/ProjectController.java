package com.fiuni.marketplacefreelancer.controller;

import com.fiuni.marketplacefreelancer.domain.enums.ProjectStatus;
import com.fiuni.marketplacefreelancer.dto.Project.ProjectDTO;
import com.fiuni.marketplacefreelancer.dto.Project.ProjectResult;
import com.fiuni.marketplacefreelancer.dto.Tag.TagDTO;
import com.fiuni.marketplacefreelancer.service.project.ProjectService;
import com.fiuni.marketplacefreelancer.service.tag.TagService;
import com.fiuni.marketplacefreelancer.utils.AlreadyExistsException;
import com.fiuni.marketplacefreelancer.utils.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TagService tagService;  // Añadimos el servicio de etiquetas (TagService)

    @GetMapping
    public ResponseEntity<ProjectResult> getAll(Pageable pageable) {
        return new ResponseEntity<>(projectService.getAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return new ResponseEntity<>(projectService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ProjectDTO project) {
        return new ResponseEntity<>(projectService.save(project), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody ProjectDTO project) {
        ProjectDTO updatedProject = projectService.update(id, project);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @PutMapping("/tags/{id}")  // Cambié la ruta para diferenciar entre proyectos y tags
    public ResponseEntity<?> updateTag(@PathVariable String id, @RequestBody TagDTO tag) {
        TagDTO updatedTag = tagService.update(id, tag);
        return new ResponseEntity<>(updatedTag, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return new ResponseEntity<>(projectService.delete(id), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/tag/{tagId}")
    public ResponseEntity<?> addTag(@PathVariable String id, @PathVariable String tagId) {
        try {
            return new ResponseEntity<>(projectService.addTag(id, tagId), HttpStatus.OK);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}/tag/{tagId}")
    public ResponseEntity<?> removeTag(@PathVariable String id, @PathVariable String tagId) {
        try {
            return new ResponseEntity<>(projectService.removeTag(id, tagId), HttpStatus.OK);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<?> getAllTagsByProjectId(@PathVariable String id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return new ResponseEntity<>(projectService.getAllTagsByProjectId(id, pageable), HttpStatus.OK);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<?> getByTitle(@PathVariable String title) {
        ProjectDTO project = projectService.getByTitle(title);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getByStatus(@PathVariable ProjectStatus status) {
        ProjectDTO project = projectService.getByStatus(status);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

}
