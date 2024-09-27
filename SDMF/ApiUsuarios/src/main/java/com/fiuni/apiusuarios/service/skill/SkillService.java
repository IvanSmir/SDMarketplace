package com.fiuni.apiusuarios.service.skill;

import com.fiuni.apiusuarios.dao.skill.ISkillDao;
import com.fiuni.apiusuarios.service.base.BaseServiceImpl;
import com.fiuni.marketplacefreelancer.utils.AlreadyExistsException;
import com.fiuni.marketplacefreelancer.utils.InvalidDataException;
import com.fiuni.marketplacefreelancer.utils.NotFoundException;
import com.fiuni.marketplacefreelancer.domain.skill.SkillDomainImpl;
import com.fiuni.marketplacefreelancer.dto.Skill.SkillDTO;
import com.fiuni.marketplacefreelancer.dto.Skill.SkillResult;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SkillService extends BaseServiceImpl<SkillDTO, SkillDomainImpl, SkillResult> {

    private final ISkillDao skillDao;
    private final ModelMapper modelMapper;

    @Autowired
    public SkillService(ISkillDao skillDao, ModelMapper modelMapper) {
        this.skillDao = skillDao;
        this.modelMapper = modelMapper;
    }

    @Override
    protected SkillDTO convertDomainToDto(SkillDomainImpl domain) {
        return modelMapper.map(domain, SkillDTO.class);
    }

    @Override
    protected SkillDomainImpl convertDtoToDomain(SkillDTO dto) {
        return modelMapper.map(dto, SkillDomainImpl.class);
    }

    @Override
    public SkillDTO save(SkillDTO dto) {
        log.info("Starting skill save service for skill name: {}", dto.getName());
        validateSkillData(dto);

        Optional<SkillDomainImpl> existingSkill = skillDao.findByName(dto.getName());
        if (existingSkill.isPresent()) {
            log.warn("Attempt to create a skill that already exists: {}", dto.getName());
            throw new AlreadyExistsException("The skill with name " + dto.getName() + " already exists");
        }
        try {
            SkillDomainImpl skill = convertDtoToDomain(dto);
            SkillDomainImpl savedSkill = skillDao.save(skill);
            log.info("Skill saved successfully with ID: {}", savedSkill.getId());
            return convertDomainToDto(savedSkill);
        }catch (Exception e) {
            log.error("An unexpected error occurred while saving skill: {}", dto.getName(), e);
            throw new RuntimeException("Error saving the skill", e);
        }

    }

    @Override
    public SkillDTO getById(String id) {
        log.info("Starting skill get by id service for skill ID: {}", id);
        return skillDao.findById(id)
                .map(this::convertDomainToDto)
                .orElseThrow(() -> new NotFoundException("Skill", id));
    }


    @Override
    public SkillResult getAll(Pageable pageable) {
        log.info("Starting skill get all service");
        Page<SkillDomainImpl> skills = skillDao.findAll(pageable);
        List<SkillDTO> skillDTOs = skills.getContent().stream()
                .map(this::convertDomainToDto)
                .toList();
        SkillResult result = new SkillResult();
        result.setSkills(skillDTOs);
        return result;
    }

    public Boolean delete(String id) {
        log.info("Starting skill delete service for skill ID: {}", id);
        Optional<SkillDomainImpl> skill = skillDao.findById(id);
        if (skill.isPresent()) {
            skillDao.delete(skill.get());
            log.info("Skill deleted successfully with ID: {}", id);
            return true;
        } else {
            log.warn("Attempt to delete a skill that does not exist: {}", id);
            return false;
        }
    }

    public SkillDTO update(String id, SkillDTO dto) {
        log.info("Starting update for skill with id: {}", id);

        Optional<SkillDomainImpl> existingSkillOpt = skillDao.findById(id);
        if (existingSkillOpt.isEmpty()) {
            log.warn("Skill with id {} not found", id);
            throw new InvalidDataException("Skill with id " + id + " not found");
        }

        SkillDomainImpl existingSkill = existingSkillOpt.get();
        validateSkillData(dto);

        try {
            existingSkill.setName(dto.getName());
            SkillDomainImpl updatedSkill = skillDao.save(existingSkill);
            log.info("Skill with id {} updated successfully", updatedSkill.getId());

            return modelMapper.map(updatedSkill, SkillDTO.class);
        } catch (Exception e) {
            log.error("An unexpected error occurred while updating skill with id: {}", id, e);
            throw new RuntimeException("Error updating the skill", e);
        }


    }

    private void validateSkillData(SkillDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            log.error("Invalid skill data: skill name is null or empty");
            throw new InvalidDataException("Skill name cannot be null or empty");
        }
    }
}
