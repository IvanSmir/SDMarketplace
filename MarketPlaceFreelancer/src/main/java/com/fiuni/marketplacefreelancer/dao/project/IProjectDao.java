package com.fiuni.marketplacefreelancer.dao.project;

import com.fiuni.marketplacefreelancer.domain.enums.ProjectStatus;
import com.fiuni.marketplacefreelancer.domain.project.ProjectDomainImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProjectDao extends JpaRepository<ProjectDomainImpl, String> {
    Optional<ProjectDomainImpl> findByTitle(String title);
    Optional<ProjectDomainImpl> findByStatus(ProjectStatus status);
}
