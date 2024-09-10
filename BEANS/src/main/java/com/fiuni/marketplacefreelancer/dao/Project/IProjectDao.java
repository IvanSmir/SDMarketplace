package com.fiuni.marketplacefreelancer.dao.Project;

import com.fiuni.marketplacefreelancer.domain.project.ProjectDomainImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProjectDao extends JpaRepository<ProjectDomainImpl, Integer> {
}
