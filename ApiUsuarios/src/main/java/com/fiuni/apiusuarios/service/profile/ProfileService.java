package com.fiuni.apiusuarios.service.profile;

import com.fiuni.apiusuarios.dao.profile.IProfileDao;
import com.fiuni.apiusuarios.dao.skill.ISkillDao;
import com.fiuni.apiusuarios.dao.user.IUserDao;
import com.fiuni.apiusuarios.service.base.BaseServiceImpl;
import com.fiuni.apiusuarios.utils.InvalidDataException;
import com.fiuni.apiusuarios.utils.NotFoundException;
import com.fiuni.marketplacefreelancer.domain.profile.ProfileDomainImpl;
import com.fiuni.marketplacefreelancer.domain.rate.RateDomainImpl;
import com.fiuni.marketplacefreelancer.domain.skill.SkillDomainImpl;
import com.fiuni.marketplacefreelancer.domain.user.UserDomainImpl;
import com.fiuni.marketplacefreelancer.dto.Profile.ProfileDTO;
import com.fiuni.marketplacefreelancer.dto.Profile.ProfileResult;
import com.fiuni.marketplacefreelancer.dto.Rate.RateDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class ProfileService extends BaseServiceImpl<ProfileDTO, ProfileDomainImpl, ProfileResult> {

    private final IProfileDao profileDao;
    private final IUserDao userDao;
    private final ISkillDao skillDao;
    private final ModelMapper modelMapper;

    @Autowired
    public ProfileService(IProfileDao profileDao, IUserDao userDao, ISkillDao skillDao, ModelMapper modelMapper) {
        this.profileDao = profileDao;
        this.userDao = userDao;
        this.skillDao = skillDao;
        this.modelMapper = modelMapper;
    }

    @Override
    protected ProfileDTO converDomainToDto(ProfileDomainImpl domain) {
        return modelMapper.map(domain, ProfileDTO.class);
    }

    @Override
    protected ProfileDomainImpl converDtoToDomain(ProfileDTO dto) {
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
            ProfileDomainImpl profile = converDtoToDomain(dto);
            ProfileDomainImpl savedProfile = profileDao.save(profile);
            log.info("Profile saved successfully with ID: {}", savedProfile.getId());
            return converDomainToDto(savedProfile);
        } catch (Exception e) {
            log.error("An unexpected error occurred while saving profile: {}", dto.getUserId(), e);
            throw new RuntimeException("Error saving the profile", e);
        }
    }

    @Override
    public ProfileDTO getById(String id) {
        log.info("Starting profile get by id service for profile ID: {}", id);
        return profileDao.findById(id)
                .map(this::converDomainToDto)
                .orElseThrow(() -> new NotFoundException("Profile", id));
    }

    @Override
    public ProfileResult getAll(Pageable pageable) {
        log.info("Starting profile get all service");
        Page<ProfileDomainImpl> profiles = profileDao.findAll(pageable);
        List<ProfileDTO> profilesDTO = profiles.getContent().stream()
                .map(this::converDomainToDto)
                .toList();
        ProfileResult result = new ProfileResult();
        result.setProfiles(profilesDTO);
        return result;
    }

    public Boolean delete(String id) {
        log.info("Starting profile delete service for profile ID: {}", id);
        Optional<ProfileDomainImpl> profile = profileDao.findById(id);
        if (profile.isPresent()) {
            profileDao.delete(profile.get());
            log.info("Profile deleted successfully with ID: {}", id);
            return true;
        } else {
            log.warn("Attempt to delete a profile that does not exist: {}", id);
            return false;
        }
    }

    public ProfileDTO update(String id, ProfileDTO dto) {
        log.info("Starting update for profile with id: {}", id);

        Optional<ProfileDomainImpl> existingProfileOpt = profileDao.findById(id);
        if (existingProfileOpt.isEmpty()) {
            log.warn("Profile with id {} not found", id);
            throw new InvalidDataException("Profile with id " + id + " not found");
        }

        Optional<ProfileDomainImpl> existingProfile = profileDao.findById(id);
        if (existingProfile.isEmpty() || existingProfile.get().getUser().getId().equals(dto.getUserId())) {
            try {
                ProfileDomainImpl updatedProfile = profileDao.save(converDtoToDomain(dto));
                log.info("Profile with id {} updated successfully", updatedProfile.getId());
                return converDomainToDto(updatedProfile);
            } catch (Exception e) {
                log.error("An unexpected error occurred while updating profile with id: {}", id, e);
                throw new RuntimeException("Error updating the profile", e);
            }
        }
        else {
            log.warn("Attempt to update a profile that does not belong to the user");
            throw new InvalidDataException("Profile with id " + id + " does not belong to the user");
        }
    }

    public ProfileResult getByTag(String tag, Pageable pageable) {
        log.info("Starting profile get by tag service for profile tag: {}", tag);
        Page<ProfileDomainImpl> profiles = profileDao.findBySkills_NameContaining(tag, pageable);
        List<ProfileDTO> profilesDTO = profiles.getContent().stream()
                .map(this::converDomainToDto)
                .toList();
        ProfileResult result = new ProfileResult();
        result.setProfiles(profilesDTO);
        return result;
    }

    public ProfileDTO addRate(String id, RateDTO dto) {
        log.info("Starting profile add rate service for profile id: {}", id);
        Optional<ProfileDomainImpl> existingProfileOpt = profileDao.findById(id);
        if (existingProfileOpt.isPresent()) {
            ProfileDomainImpl existingProfile = existingProfileOpt.get();
            existingProfile.addRate(modelMapper.map(dto, RateDomainImpl.class));
            ProfileDomainImpl savedProfile = profileDao.save(existingProfile);
            log.info("Profile with id {} updated successfully", savedProfile.getId());
            return converDomainToDto(savedProfile);
        } else {
            log.warn("Attempt to update a profile that does not exist: {}", id);
            throw new InvalidDataException("Profile with id " + id + " not found");
        }
    }

    public ProfileDTO removeRate(String id, String rateId) {
        log.info("Starting profile remove rate service for profile id: {}", id);
        Optional<ProfileDomainImpl> existingProfileOpt = profileDao.findById(id);
        if (existingProfileOpt.isPresent()) {
            RateDomainImpl rate = existingProfileOpt.get().getRates().stream()
                    .filter(r -> Objects.equals(r.getId(), rateId))
                    .findFirst()
                    .orElseThrow(() -> new InvalidDataException("Rate with id " + rateId + " not found"));
            existingProfileOpt.get().removeRate(rate);
            ProfileDomainImpl savedProfile = profileDao.save(existingProfileOpt.get());
            log.info("Profile with id {} updated successfully", savedProfile.getId());
            return converDomainToDto(savedProfile);
        } else {
            log.warn("Attempt to update a profile that does not exist: {}", id);
            throw new InvalidDataException("Profile with id " + id + " not found");
        }
    }



    public ProfileDTO addSkillToProfile(String profileId, String skillId) {
        Optional<ProfileDomainImpl> profileOpt = profileDao.findById(profileId);
        if (profileOpt.isPresent()) {
            ProfileDomainImpl profile = profileOpt.get();

            Optional<SkillDomainImpl> skillOpt = skillDao.findById(skillId);
            if (skillOpt.isPresent()) {
                SkillDomainImpl skill = skillOpt.get();
                profile.getSkills().add(skill);

                ProfileDomainImpl updatedProfile = profileDao.save(profile);

                return converDomainToDto(updatedProfile);
            } else {
                throw new InvalidDataException("Skill with id " + skillId + " not found");
            }
        } else {
            throw new InvalidDataException("Profile with id " + profileId + " not found");
        }
    }

    public ProfileDTO removeSkillFromProfile(String profileId, String skillId) {
        Optional<ProfileDomainImpl> profileOpt = profileDao.findById(profileId);
        if (profileOpt.isPresent()) {
            ProfileDomainImpl profile = profileOpt.get();
            profile.getSkills().removeIf(skill -> Objects.equals(skill.getId(), skillId));

            ProfileDomainImpl updatedProfile = profileDao.save(profile);
            return converDomainToDto(updatedProfile);
        } else {
            throw new InvalidDataException("Profile with id " + profileId + " not found");
        }
    }


    private void validateProfileData(ProfileDTO dto) {
        if (dto.getUserId() == null || dto.getUserId().trim().isEmpty()) {
            log.error("Invalid profile data: profile user_id is null or empty");
            throw new InvalidDataException("Profile user_id cannot be null or empty");
        }
        if (dto.getRate() < 0) {
            log.error("Invalid profile data: profile rate is null or invalid");
            throw new InvalidDataException("Profile rate cannot be null or invalid");
        }
    }
}
