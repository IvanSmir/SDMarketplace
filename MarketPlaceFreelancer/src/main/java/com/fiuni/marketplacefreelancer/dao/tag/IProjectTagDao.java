package com.fiuni.marketplacefreelancer.dao.tag;

import com.fiuni.marketplacefreelancer.domain.tag.ProjectTagDomainImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IProjectTagDao extends JpaRepository<ProjectTagDomainImpl, String> {
    Optional<ProjectTagDomainImpl> findByProjectIdAndTagId(String projectId, String tagId);
    List<ProjectTagDomainImpl> findAllByTagId(String tagId, Pageable pageable);

    Page<ProjectTagDomainImpl> findAllByProjectId(String projectId, Pageable pageable);
}
