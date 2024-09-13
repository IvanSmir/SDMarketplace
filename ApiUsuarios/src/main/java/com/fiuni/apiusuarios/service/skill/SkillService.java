package com.fiuni.apiusuarios.service.skill;

import com.fiuni.apiusuarios.dao.skill.ISkillDao;
import com.fiuni.apiusuarios.service.base.BaseServiceImpl;
import com.fiuni.apiusuarios.utils.AlreadyExistsException;
import com.fiuni.apiusuarios.utils.InvalidDataException;
import com.fiuni.apiusuarios.utils.NotFoundException;
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



    @Autowired
    private ISkillDao skillDao;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    protected SkillDTO converDomainToDto(SkillDomainImpl domain) {
        return modelMapper.map(domain, SkillDTO.class);
    }

    @Override
    protected SkillDomainImpl converDtoToDomain(SkillDTO dto) {
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
            SkillDomainImpl skill = converDtoToDomain(dto);
            SkillDomainImpl savedSkill = skillDao.save(skill);
            log.info("Skill saved successfully with ID: {}", savedSkill.getId());
            return converDomainToDto(savedSkill);
        }catch (Exception e) {
            log.error("An unexpected error occurred while saving skill: {}", dto.getName(), e);
            throw new RuntimeException("Error saving the skill", e);
        }

    }

    @Override
    public SkillDTO getById(String id) {
        log.info("Starting skill get by id service for skill ID: {}", id);
        return skillDao.findById(id)
                .map(this::converDomainToDto)
                .orElseThrow(() -> new NotFoundException("Skill", id));
    }


    @Override
    public SkillResult getAll(Pageable pageable) {
        log.info("Starting skill get all service");
        Page<SkillDomainImpl> skills = skillDao.findAll(pageable);
        List<SkillDTO> skillDTOs = skills.getContent().stream()
                .map(this::converDomainToDto)
                .toList();
        SkillResult result = new SkillResult();
        result.setSkills(skillDTOs);
        return result;
    }

    private void validateSkillData(SkillDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            log.error("Invalid skill data: skill name is null or empty");
            throw new InvalidDataException("Skill name cannot be null or empty");
        }
    }
}
