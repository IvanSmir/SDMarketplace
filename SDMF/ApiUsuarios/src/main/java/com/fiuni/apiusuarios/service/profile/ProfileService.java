package com.fiuni.apiusuarios.service.profile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiuni.apiusuarios.dao.profile.IProfileDao;
import com.fiuni.apiusuarios.dao.rate.IRateDao;
import com.fiuni.apiusuarios.dao.skill.ISkillDao;
import com.fiuni.apiusuarios.dao.user.IUserDao;
import com.fiuni.apiusuarios.service.base.BaseServiceImpl;
import com.fiuni.marketplacefreelancer.utils.InvalidDataException;
import com.fiuni.marketplacefreelancer.utils.NotFoundException;
import com.fiuni.marketplacefreelancer.domain.profile.ProfileDomainImpl;
import com.fiuni.marketplacefreelancer.domain.rate.RateDomainImpl;
import com.fiuni.marketplacefreelancer.domain.skill.SkillDomainImpl;
import com.fiuni.marketplacefreelancer.domain.user.UserDomainImpl;
import com.fiuni.marketplacefreelancer.dto.Profile.ProfileDTO;
import com.fiuni.marketplacefreelancer.dto.Profile.ProfileResult;
import com.fiuni.marketplacefreelancer.dto.Rate.RateDTO;
import com.fiuni.marketplacefreelancer.dto.Rate.RateResult;
import com.fiuni.marketplacefreelancer.dto.Skill.SkillDTO;
import com.fiuni.marketplacefreelancer.dto.Skill.SkillResult;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class ProfileService extends BaseServiceImpl<ProfileDTO, ProfileDomainImpl, ProfileResult> {

    private final IProfileDao profileDao;
    private final IUserDao userDao;
    private final ISkillDao skillDao;
    private final IRateDao rateDao;
    private final ModelMapper modelMapper;

    @Autowired
    public ProfileService(IProfileDao profileDao, IUserDao userDao, ISkillDao skillDao, ModelMapper modelMapper, IRateDao rateDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
        this.skillDao = skillDao;
        this.rateDao = rateDao;
        this.modelMapper = modelMapper;
    }

    @Override
    protected ProfileDTO convertDomainToDto(ProfileDomainImpl domain) {
        return modelMapper.map(domain, ProfileDTO.class);
    }

    @Override
    protected ProfileDomainImpl convertDtoToDomain(ProfileDTO dto) {
        return modelMapper.map(dto, ProfileDomainImpl.class);
    }

    @Override
    public ProfileDTO save(ProfileDTO dto) {
        validateProfileData(dto);
        log.info("Starting profile save service for profile user_id: {}", dto.getUserId());
        Optional<UserDomainImpl> user = userDao.findById(dto.getUserId());
        if (user.isEmpty()) {
            log.warn("Profile user_id {} not found", dto.getUserId());
            throw new InvalidDataException("Profile user_id " + dto.getUserId() + " not found");
        }
        Optional<ProfileDomainImpl> existingProfile = profileDao.findByUserId(dto.getUserId());
        if (existingProfile.isPresent()) {
            log.warn("Attempt to create a profile that already exists: {}", dto.getUserId());
            throw new InvalidDataException("The profile with user_id " + dto.getUserId() + " already exists");
        }
        try {
            ProfileDomainImpl profile = convertDtoToDomain(dto);
            ProfileDomainImpl savedProfile = profileDao.save(profile);
            log.info("Profile saved successfully with ID: {}", savedProfile.getId());
            return convertDomainToDto(savedProfile);
        } catch (Exception e) {
            log.error("An unexpected error occurred while saving profile: {}", dto.getUserId(), e);
            throw new RuntimeException("Error saving the profile", e);
        }
    }

    @Override
    public ProfileDTO getById(String id) {
        log.info("Starting profile get by id service for profile ID: {}", id);
        return profileDao.findById(id)
                .map(this::convertDomainToDto)
                .orElseThrow(() -> new NotFoundException("Profile", id));
    }

    @Override
    public ProfileResult getAll(Pageable pageable) {
        log.info("Starting profile get all service");
        Page<ProfileDomainImpl> profiles = profileDao.findAll(pageable);
        List<ProfileDTO> profilesDTO = profiles.getContent().stream()
                .map(this::convertDomainToDto)
                .toList();
        ProfileResult result = new ProfileResult();
        result.setProfiles(profilesDTO);
        return result;
    }

    public Boolean delete(String id) {
        log.info("Starting profile delete service for profile ID: {}", id);
        Optional<ProfileDomainImpl> profile = profileDao.findById(id);
        if (profile.isEmpty()) {
            log.warn("Attempt to delete a profile that does not exist: {}", id);
            return false;
        }
        profileDao.delete(profile.get());
        log.info("Profile deleted successfully with ID: {}", id);
        return true;
    }

    public ProfileDTO update(String id, ProfileDTO dto) {
        log.info("Starting update for profile with id: {}", id);

        Optional<ProfileDomainImpl> existingProfileOpt = profileDao.findById(id);
        if (existingProfileOpt.isEmpty()) {
            log.warn("Profile with id {} not found", id);
            throw new InvalidDataException("Profile with id " + id + " not found");
        }

        Optional<ProfileDomainImpl> existingProfile = profileDao.findById(id);
        if (existingProfile.isPresent() && existingProfile.get().getUser().getId().equals(dto.getUserId())) {
            log.warn("Attempt to update a profile that does not belong to the user");
            throw new InvalidDataException("Profile with id " + id + " does not belong to the user");
        }
        try {
            ProfileDomainImpl updatedProfile = profileDao.save(convertDtoToDomain(dto));
            log.info("Profile with id {} updated successfully", updatedProfile.getId());
            return convertDomainToDto(updatedProfile);
        } catch (Exception e) {
            log.error("An unexpected error occurred while updating profile with id: {}", id, e);
            throw new RuntimeException("Error updating the profile", e);
        }
    }




    public RateDTO addRate(String id, RateDTO dto) {
        validateRateData(dto);
        log.info("Starting profile add rate service for profile id: {}", dto);
        Optional<ProfileDomainImpl> existingProfileOpt = profileDao.findById(id);
        if (existingProfileOpt.isEmpty()) {
            log.warn("Attempt to add a rate to a profile that does not exist: {}", id);
            throw new InvalidDataException("Profile with id " + id + " not found");
        }
        ProfileDomainImpl existingProfile = existingProfileOpt.get();
        if(existingProfile.getRates().stream().anyMatch(r -> Objects.equals(r.getRateType(), dto.getRateType()))) {
            log.warn("Attempt to add a rate that already exists: {}", id);
            throw new InvalidDataException("Rate with rateType " + dto.getRateType() + " already exists");
        }
        RateDomainImpl rate = modelMapper.map(dto, RateDomainImpl.class);
        rate.setProfile(existingProfile);
        existingProfile.addRate(rate);
        log.info("Rate before save: {}", rate);
        ProfileDomainImpl savedProfile = profileDao.save(existingProfile);
        log.info("Rate saved successfully with ID: {}", savedProfile.getId());
        return modelMapper.map(rate, RateDTO.class);
    }

    public boolean removeRate(String id, String rateId) {
        log.info("Starting profile remove rate service for profile id: {}", id);
        Optional<ProfileDomainImpl> existingProfileOpt = profileDao.findById(id);
        if (existingProfileOpt.isPresent()) {
            RateDomainImpl rate = existingProfileOpt.get().getRates().stream()
                    .filter(r -> Objects.equals(r.getId(), rateId))
                    .findFirst()
                    .orElseThrow(() -> new InvalidDataException("Rate with id " + rateId + " not found"));
            existingProfileOpt.get().removeRate(rate);
            ProfileDomainImpl savedProfile = profileDao.save(existingProfileOpt.get());
            log.info("Rate removed successfully with ID: {}", rateId);
            return true;
        } else {
            log.warn("Attempt to update a profile that does not exist: {}", id);
            throw new InvalidDataException("Profile with id " + id + " not found");
        }
    }

    public RateResult getRates(String profileId, Pageable pageable) {
        log.info("Starting profile get rates service for profile id: {}", profileId);
        Optional<ProfileDomainImpl> profileOpt = profileDao.findById(profileId);
        if (profileOpt.isPresent()) {
            ProfileDomainImpl profile = profileOpt.get();
            List<RateDomainImpl> rates = profile.getRates();
            RateResult result = new RateResult();
            result.setRates(rates.stream()
                    .map( r -> modelMapper.map(r, RateDTO.class))
                    .toList());
            return result;
        } else {
            log.warn("Attempt to get rates for a profile that does not exist: {}", profileId);
            throw new InvalidDataException("Profile with id " + profileId + " not found");
        }
    }

    public ProfileResult getBySkill(String tag, Pageable pageable) {
        log.info("Starting profile get by tag service for profile tag: {}", tag);
        Page<ProfileDomainImpl> profiles = profileDao.findAllBySkills_NameContaining(tag, pageable);
        ProfileResult result = new ProfileResult();
        result.setProfiles(profiles.getContent().stream()
                .map(this::convertDomainToDto)
                .toList());
        return result;
    }

    public String addSkillToProfile(String profileId, String skillJson) {
        Optional<ProfileDomainImpl> profileOpt = profileDao.findById(profileId);
        ObjectMapper objectMapper = new ObjectMapper();
        String skillId;
        try {
            JsonNode jsonNode = objectMapper.readTree(skillJson);
            skillId = jsonNode.get("skillId").asText();
        } catch (JsonProcessingException e) {
            throw new InvalidDataException("Invalid JSON format for skillId");
        }
        if (profileOpt.isEmpty()) {
            log.warn("Attempt to add a skill to a profile that does not exist: {}", profileId);
            throw new InvalidDataException("Profile with id " + profileId + " not found");
        }
        ProfileDomainImpl profile = profileOpt.get();
        Optional<SkillDomainImpl> skillOpt = skillDao.findById(skillId);
        if (skillOpt.isEmpty()) {
            log.warn("Attempt to add a skill that does not exist: {}", skillId);
            throw new InvalidDataException("Skill with id " + skillId + " not found");
        }
        if (profile.getSkills().stream().anyMatch(s -> Objects.equals(s.getId(), skillId))) {
            log.warn("Attempt to add a skill that already exists: {}", skillId);
            throw new InvalidDataException("Skill with id " + skillId + " already exists");
        }
        SkillDomainImpl skill = skillOpt.get();
        profile.getSkills().add(skill);
        ProfileDomainImpl updatedProfile = profileDao.save(profile);
        return "Skill added successfully";
    }

    public String removeSkillFromProfile(String profileId, String skillId) {
        Optional<ProfileDomainImpl> profileOpt = profileDao.findById(profileId);
        if (profileOpt.isPresent()) {
            ProfileDomainImpl profile = profileOpt.get();
            profile.getSkills().removeIf(skill -> Objects.equals(skill.getId(), skillId));

            ProfileDomainImpl updatedProfile = profileDao.save(profile);
            return "Skill removed successfully";
        } else {
            throw new InvalidDataException("Profile with id " + profileId + " not found");
        }
    }

    public SkillResult getSkills(String profileId, Pageable pageable) {
        log.info("Starting profile get skills service for profile id: {}", profileId);
        Optional<ProfileDomainImpl> profileOpt = profileDao.findById(profileId);
        if (profileOpt.isEmpty()) {
            log.warn("Attempt to get skills for a profile that does not exist: {}", profileId);
            throw new InvalidDataException("Profile with id " + profileId + " not found");
        }
        ProfileDomainImpl profile = profileOpt.get();
        Set<SkillDomainImpl> skills = profile.getSkills();
        SkillResult result = new SkillResult();
        result.setSkills(skills.stream()
                .map( s -> modelMapper.map(s, SkillDTO.class))
                .toList());
        return result;
    }


    private void validateProfileData(ProfileDTO dto) {
        if (dto.getUserId() == null || dto.getUserId().trim().isEmpty()) {
            log.error("Invalid profile data: profile user_id is null or empty");
            throw new InvalidDataException("Profile user_id cannot be null or empty");
        }

    }
    private void validateRateData(RateDTO dto) {
        log.info("Starting rate validation{}", dto);
        if (dto.getAmount() <=0) {
            log.error("Invalid rate data: rate rate is null     or invalid");
            throw new InvalidDataException("Rate amount cannot be below 0");
        }
    }
}
