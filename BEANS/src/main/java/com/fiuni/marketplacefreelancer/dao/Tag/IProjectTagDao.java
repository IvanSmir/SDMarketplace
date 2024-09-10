package com.fiuni.marketplacefreelancer.dao.Tag;

import com.fiuni.marketplacefreelancer.domain.tag.ProjectTagDomainImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProjectTagDao extends JpaRepository<ProjectTagDomainImpl, Integer> {
}
