package com.fiuni.apiusuarios.controller.profile;

import com.fiuni.apiusuarios.service.profile.ProfileService;
import com.fiuni.marketplacefreelancer.dto.Profile.ProfileDTO;
import com.fiuni.marketplacefreelancer.dto.Profile.ProfileResult;
import com.fiuni.marketplacefreelancer.dto.Rate.RateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController("userprofileController")
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<ProfileResult> getAll(Pageable pageable) {
       ProfileResult profileResult = profileService.getAll(pageable);
       return new ResponseEntity<>(profileResult, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return new ResponseEntity<>(profileService.getById(id), HttpStatus.OK);
    }

    @GetMapping("tag")
    public ResponseEntity<?> getByTag(@RequestParam String tag, Pageable pageable) {
        return new ResponseEntity<>(profileService.getByTag(tag, pageable), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ProfileDTO profile) {
        return new ResponseEntity<>(profileService.save(profile), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody ProfileDTO profile) {
        return new ResponseEntity<>(profileService.update(id, profile), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return new ResponseEntity<>(profileService.delete(id), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/rate")
    public ResponseEntity<?> addRate(@PathVariable String id, @RequestBody RateDTO rate) {
        return new ResponseEntity<>(profileService.addRate(id, rate), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/rate/{rateId}")
    public ResponseEntity<?> removeRate(@PathVariable String id, @PathVariable String rateId) {
        return new ResponseEntity<>(profileService.removeRate(id, rateId), HttpStatus.OK);
    }

    @PutMapping("/{id}/skill/{skillId}")
    public ResponseEntity<?> addSkillToProfile(@PathVariable String id, @PathVariable String skillId) {
        return new ResponseEntity<>(profileService.addSkillToProfile(id, skillId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/skill/{skillId}")
    public ResponseEntity<?> removeSkillFromProfile(@PathVariable String id, @PathVariable String skillId) {
        return new ResponseEntity<>(profileService.removeSkillFromProfile(id, skillId), HttpStatus.OK);
    }



}
