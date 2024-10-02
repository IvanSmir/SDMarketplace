package com.fiuni.marketplacefreelancer.service.project;

import com.fiuni.marketplacefreelancer.dao.project.IProjectDao;
import com.fiuni.marketplacefreelancer.dao.tag.IProjectTagDao;
import com.fiuni.marketplacefreelancer.dao.tag.ITagDao;
import com.fiuni.marketplacefreelancer.domain.enums.ProjectStatus;
import com.fiuni.marketplacefreelancer.domain.project.ProjectDomainImpl;
import com.fiuni.marketplacefreelancer.domain.tag.ProjectTagDomainImpl;
import com.fiuni.marketplacefreelancer.domain.tag.TagDomainImpl;
import com.fiuni.marketplacefreelancer.dto.Project.ProjectDTO;
import com.fiuni.marketplacefreelancer.dto.Project.ProjectResult;
import com.fiuni.marketplacefreelancer.dto.Tag.TagDTO;
import com.fiuni.marketplacefreelancer.service.base.BaseServiceImpl;
import com.fiuni.marketplacefreelancer.utils.AlreadyExistsException;
import com.fiuni.marketplacefreelancer.utils.InvalidDataException;
import com.fiuni.marketplacefreelancer.utils.NotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService extends BaseServiceImpl<ProjectDTO, ProjectDomainImpl, ProjectResult> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IProjectDao projectDao;

    @Autowired
    private ITagDao tagDao;

    @Autowired
    private IProjectTagDao projectTagDao;

    @Autowired
    private CacheManager cacheManager;

    @Override
    protected ProjectDTO converDomainToDto(ProjectDomainImpl domain) {
        return modelMapper.map(domain, ProjectDTO.class);
    }

    @Override
    protected ProjectDomainImpl converDtoToDomain(ProjectDTO dto) {
        return modelMapper.map(dto, ProjectDomainImpl.class);
    }

    @Override
    public ProjectDTO save(ProjectDTO dto) {
        logger.info("Saving project with ID: {}", dto.getId());

        validateProjectData(dto);
        ProjectDomainImpl project = converDtoToDomain(dto);
        ProjectDomainImpl savedProject = projectDao.save(project);

        // Guardamos el proyecto en el cache
        ProjectDTO savedProjectDTO = converDomainToDto(savedProject);
        logger.info("Caching project with ID: {}", savedProjectDTO.getId());
        cacheManager.getCache("project").put(savedProjectDTO.getId(), savedProjectDTO);

        return savedProjectDTO;
    }


    @Cacheable(value = "project", key = "#id")
    public ProjectDTO getById(String id) {
        logger.info("Fetching project with ID: {} from cache or DB", id);

        return projectDao.findById(id)
                .map(this::converDomainToDto)
                .orElseThrow(() -> new NotFoundException("Project", id));
    }

    public ProjectResult getAll(Pageable pageable) {
        logger.info("Fetching all projects");

        Page<ProjectDomainImpl> projects = projectDao.findAll(pageable);

        List<ProjectDTO> projectDTOs = projects.getContent().stream()
                .map(this::converDomainToDto)
                .toList();

        // Cacheamos cada proyecto individualmente solo si no está ya en el cache
        projectDTOs.forEach(project -> {
            if (cacheManager.getCache("project").get(project.getId()) == null) {
                logger.info("Caching project with ID: {}", project.getId());
                cacheManager.getCache("project").put(project.getId(), project);
            } else {
                logger.info("Project with ID: {} is already in cache", project.getId());
            }
        });

        ProjectResult projectResult = new ProjectResult();
        projectResult.setProjects(projectDTOs);
        return projectResult;
    }


    public ProjectDTO update(String id, ProjectDTO dto) {
        logger.info("Updating project with ID: {}", id);

        ProjectDomainImpl existingProject = projectDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Project", id));

        modelMapper.map(dto, existingProject);
        ProjectDomainImpl updatedProject = projectDao.save(existingProject);

        logger.info("Updating cache for project with ID: {}", id);
        cacheManager.getCache("project").put(id, updatedProject);

        return converDomainToDto(updatedProject);
    }

    public Boolean delete(String id) {
        logger.info("Deleting project with ID: {}", id);

        Optional<ProjectDomainImpl> project = projectDao.findById(id);
        if (project.isPresent()) {
            projectDao.delete(project.get());
            logger.info("Removing project with ID: {} from cache", id);
            cacheManager.getCache("project").evict(id);

            return true;
        }
        return false;
    }

    public ProjectDTO getByTitle(String title) {
        logger.info("Fetching project by title: {}", title);

        ProjectDTO cachedProject = cacheManager.getCache("project").get(title, ProjectDTO.class);
        if (cachedProject != null) {
            return cachedProject;
        }

        // Si no está en cache, lo obtenemos de la base de datos
        return projectDao.findByTitle(title)
                .map(project -> {
                    ProjectDTO projectDTO = converDomainToDto(project);
                    logger.info("Caching project with title: {}", title);
                    cacheManager.getCache("project").put(title, projectDTO);
                    return projectDTO;
                })
                .orElseThrow(() -> new NotFoundException("Project", title));
    }

    public ProjectDTO getByStatus(ProjectStatus status) {
        logger.info("Fetching project by status: {}", status);

        // Intentamos obtener el proyecto del cache
        ProjectDTO cachedProject = cacheManager.getCache("project").get(status, ProjectDTO.class);
        if (cachedProject != null) {
            return cachedProject;
        }

        // Si no está en cache, lo obtenemos de la base de datos
        return projectDao.findByStatus(status)
                .map(project -> {
                    ProjectDTO projectDTO = converDomainToDto(project);
                    cacheManager.getCache("project").put(status, projectDTO);
                    return projectDTO;
                })
                .orElseThrow(() -> new NotFoundException("Project", status.getDisplayName()));
    }

    public Boolean addTag(String projectId, String tagId) {
        logger.info("Starting project add tag service for project id: {}", projectId);

        Optional<ProjectDomainImpl> project = projectDao.findById(projectId);
        if (project.isEmpty()) {
            logger.error("Project not found");
            throw new NotFoundException("Project", projectId);
        }

        Optional<TagDomainImpl> tag = tagDao.findById(tagId);
        if (tag.isEmpty()) {
            logger.error("Tag not found");
            throw new NotFoundException("Tag", tagId);
        }

        Optional<ProjectTagDomainImpl> existingProjectTag = projectTagDao.findByProjectIdAndTagId(projectId, tagId);
        if (existingProjectTag.isPresent()) {
            logger.error("ProjectTag already exists");
            throw new AlreadyExistsException("ProjectTag with projectId " + projectId + " and tagId " + tagId + " already exists");
        }

        ProjectTagDomainImpl projectTag = new ProjectTagDomainImpl();
        projectTag.setProject(project.get());
        projectTag.setTag(tag.get());
        projectTagDao.save(projectTag);

        return true;
    }

    public Boolean removeTag(String projectId, String tagId) {
        logger.info("Starting project remove tag service for project id: {}", projectId);

        Optional<ProjectDomainImpl> project = projectDao.findById(projectId);
        if (project.isEmpty()) {
            logger.error("Project not found");
            throw new NotFoundException("Project", projectId);
        }

        Optional<TagDomainImpl> tag = tagDao.findById(tagId);
        if (tag.isEmpty()) {
            logger.error("Tag not found");
            throw new NotFoundException("Tag", tagId);
        }

        Optional<ProjectTagDomainImpl> existingProjectTag = projectTagDao.findByProjectIdAndTagId(projectId, tagId);
        if (existingProjectTag.isEmpty()) {
            logger.error("ProjectTag not found");
            throw new AlreadyExistsException("ProjectTag with projectId " + projectId + " and tagId " + tagId + " not found");
        }

        projectTagDao.delete(existingProjectTag.get());
        logger.info("ProjectTag deleted successfully with ID: {}", projectId);

        return true;
    }

    public List<TagDTO> getAllTagsByProjectId(String projectId, Pageable pageable) {
        logger.info("Starting project get all tags by project id service for project id: {}", projectId);

        Optional<ProjectDomainImpl> project = projectDao.findById(projectId);
        if (project.isEmpty()) {
            throw new NotFoundException("Project", projectId);
        }

        Page<ProjectTagDomainImpl> projectTags = projectTagDao.findAllByProjectId(projectId, pageable);
        return projectTags.getContent().stream()
                .map(projectTag -> modelMapper.map(projectTag.getTag(), TagDTO.class))
                .toList();
    }

    private void validateProjectData(ProjectDTO dto) {
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            logger.error("Invalid project data: project title is null or empty");
            throw new InvalidDataException("Invalid project data: project title is null or empty");
        }
        if (dto.getUserId() == null || dto.getUserId().trim().isEmpty()) {
            logger.error("Invalid project data: project userId is null or empty");
            throw new InvalidDataException("Invalid project data: project userId is null or empty");
        }
    }
}
