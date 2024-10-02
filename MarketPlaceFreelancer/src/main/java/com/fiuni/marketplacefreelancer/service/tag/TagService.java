package com.fiuni.marketplacefreelancer.service.tag;

import com.fiuni.marketplacefreelancer.dao.tag.IProjectTagDao;
import com.fiuni.marketplacefreelancer.dao.tag.ITagDao;
import com.fiuni.marketplacefreelancer.domain.tag.ProjectTagDomainImpl;
import com.fiuni.marketplacefreelancer.domain.tag.TagDomainImpl;
import com.fiuni.marketplacefreelancer.dto.Tag.TagDTO;
import com.fiuni.marketplacefreelancer.dto.Tag.TagResult;
import com.fiuni.marketplacefreelancer.service.base.BaseServiceImpl;
import com.fiuni.marketplacefreelancer.utils.AlreadyExistsException;
import com.fiuni.marketplacefreelancer.utils.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TagService extends BaseServiceImpl<TagDTO, TagDomainImpl, TagResult> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ITagDao tagDao;

    @Autowired
    private IProjectTagDao projectTagDao;

    @Autowired
    private CacheManager cacheManager;

    @Override
    protected TagDTO converDomainToDto(TagDomainImpl domain) {
        log.info("Converting Tag domain to DTO for tag: {}", domain.getName());
        return modelMapper.map(domain, TagDTO.class);
    }

    @Override
    protected TagDomainImpl converDtoToDomain(TagDTO dto) {
        log.info("Converting Tag DTO to domain for tag: {}", dto.getName());
        return modelMapper.map(dto, TagDomainImpl.class);
    }

    @Override
    public TagDTO save(TagDTO dto) {
        log.info("Saving tag with name: {}", dto.getName());

        Optional<TagDomainImpl> existingTag = tagDao.findByName(dto.getName());
        if (existingTag.isPresent()) {
            log.warn("Tag already exists: {}", dto.getName());
            throw new AlreadyExistsException("The tag with name " + dto.getName() + " already exists");
        }

        try {
            TagDomainImpl tag = converDtoToDomain(dto);
            TagDomainImpl savedTag = tagDao.save(tag);
            log.info("Tag saved successfully with ID: {}", savedTag.getId());

            log.info("Caching saved tag with ID: {}", savedTag.getId());
            cacheManager.getCache("tag").put(savedTag.getId(), savedTag);

            return converDomainToDto(savedTag);
        } catch (Exception e) {
            log.error("Error saving tag: {}", dto.getName(), e);
            throw new RuntimeException("Error saving tag", e);
        }
    }

    public TagDTO getById(String id) {
        log.info("Fetching tag with ID: {}", id);

        TagDTO cachedTag = cacheManager.getCache("tag").get(id, TagDTO.class);
        if (cachedTag != null) {
            log.info("Tag found in cache with ID: {}", id);
            return cachedTag;
        }

        log.info("Tag not found in cache, querying database for ID: {}", id);
        return tagDao.findById(id)
                .map(tag -> {
                    TagDTO tagDTO = converDomainToDto(tag);
                    log.info("Caching fetched tag with ID: {}", id);
                    cacheManager.getCache("tag").put(id, tagDTO);
                    return tagDTO;
                })
                .orElseThrow(() -> new NotFoundException("Tag", id));
    }

    public TagResult getAll(Pageable pageable) {
        log.info("Fetching all tags");

        Page<TagDomainImpl> tags = tagDao.findAll(pageable);
        List<TagDTO> tagDTOs = tags.getContent().stream()
                .map(this::converDomainToDto)
                .toList();

        tagDTOs.forEach(tag -> {
            if (cacheManager.getCache("tag").get(tag.getId()) == null) {
                log.info("Caching tag with ID: {}", tag.getId());
                cacheManager.getCache("tag").put(tag.getId(), tag);
            } else {
                log.info("Tag with ID: {} is already cached", tag.getId());
            }
        });

        TagResult tagResult = new TagResult();
        tagResult.setTags(tagDTOs);
        return tagResult;
    }

    public TagDTO update(String id, TagDTO dto) {
        log.info("Updating tag with ID: {}", id);

        Optional<TagDomainImpl> existingTag = tagDao.findById(id);
        if (existingTag.isEmpty()) {
            log.warn("Tag not found with ID: {}", id);
            throw new NotFoundException("Tag", id);
        }

        try {
            TagDomainImpl updatedTag = converDtoToDomain(dto);
            TagDomainImpl savedTag = tagDao.save(updatedTag);
            log.info("Tag updated successfully with ID: {}", savedTag.getId());

            log.info("Updating cache for tag with ID: {}", id);
            cacheManager.getCache("tag").put(savedTag.getId(), savedTag);

            return converDomainToDto(savedTag);
        } catch (Exception e) {
            log.error("Error updating tag with ID: {}", id, e);
            throw new RuntimeException("Error updating tag", e);
        }
    }

    public Boolean delete(String id) {
        log.info("Deleting tag with ID: {}", id);

        List<ProjectTagDomainImpl> projectTags = projectTagDao.findAllByTagId(id, Pageable.unpaged());
        if (!projectTags.isEmpty()) {
            log.info("Deleting related project_tag entries for tag ID: {}", id);
            projectTagDao.deleteAll(projectTags);
        }

        Optional<TagDomainImpl> tag = tagDao.findById(id);
        if (tag.isPresent()) {
            log.info("Deleting tag from database with ID: {}", id);
            tagDao.delete(tag.get());

            log.info("Removing tag from cache with ID: {}", id);
            cacheManager.getCache("tag").evict(id);

            log.info("Tag deleted successfully with ID: {}", id);
            return true;
        } else {
            log.warn("Tag with ID: {} not found", id);
            return false;
        }
    }

    public TagDTO getByName(String name) {
        log.info("Fetching tag by name: {}", name);

        TagDTO cachedTag = cacheManager.getCache("tag").get(name, TagDTO.class);
        if (cachedTag != null) {
            log.info("Tag found in cache by name: {}", name);
            return cachedTag;
        }

        log.info("Tag not found in cache, querying database for name: {}", name);
        return tagDao.findByName(name)
                .map(tag -> {
                    TagDTO tagDTO = converDomainToDto(tag);
                    log.info("Caching fetched tag with name: {}", name);
                    cacheManager.getCache("tag").put(name, tagDTO);
                    return tagDTO;
                })
                .orElseThrow(() -> new NotFoundException("Tag", name));
    }
}
