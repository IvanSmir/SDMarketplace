package com.fiuni.apiusuarios.controller.profile;

import com.fiuni.apiusuarios.service.profile.ProfileService;
import com.fiuni.marketplacefreelancer.dto.Profile.ProfileDTO;
import com.fiuni.marketplacefreelancer.dto.Profile.ProfileResult;
import com.fiuni.marketplacefreelancer.dto.Rate.RateDTO;
import com.fiuni.marketplacefreelancer.dto.Rate.RateResult;
import com.fiuni.marketplacefreelancer.dto.Skill.SkillResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController("userprofileController")
@RequestMapping("/api/v1/profile")
@Slf4j
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<ProfileResult> getAll(Pageable pageable, @RequestParam(required = false) String skillId) {
        if(skillId != null && !skillId.trim().isEmpty()) {
            return new ResponseEntity<>(profileService.getProfilesBySkill(skillId, pageable), HttpStatus.OK);
        }
       ProfileResult profileResult = profileService.getAll(pageable);
       return new ResponseEntity<>(profileResult, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return new ResponseEntity<>(profileService.getById(id), HttpStatus.OK);
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

    @GetMapping("/{id}/rate")
    public ResponseEntity<RateResult> getRates(@PathVariable String id, Pageable pageable) {
        return new ResponseEntity<>(profileService.getRates(id, pageable), HttpStatus.OK);
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<?> addRate(@PathVariable String id, @RequestBody RateDTO rate) {
        log.info(rate.toString());
        return new ResponseEntity<>(profileService.addRate(id, rate), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/rate/{rateId}")
    public ResponseEntity<?> removeRate(@PathVariable String id, @PathVariable String rateId) {
        return new ResponseEntity<>(profileService.removeRate(id, rateId), HttpStatus.OK);
    }

    @PostMapping("/{id}/skill")
    public ResponseEntity<?> addSkillToProfile(@PathVariable String id, @RequestBody String skillId) {
        return new ResponseEntity<>(profileService.addSkillToProfile(id, skillId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/skill/{skillId}")
    public ResponseEntity<?> removeSkillFromProfile(@PathVariable String id, @PathVariable String skillId) {
        return new ResponseEntity<>(profileService.removeSkillFromProfile(id, skillId), HttpStatus.OK);
    }

    @GetMapping("/{id}/skill")
    public ResponseEntity<SkillResult> getSkills(@PathVariable String id, Pageable pageable) {
        return new ResponseEntity<>(profileService.getSkills(id, pageable), HttpStatus.OK);
    }



}
