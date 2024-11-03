package com.fiuni.apitransaction.dao;

import com.fiuni.marketplacefreelancer.domain.project.ProjectDomainImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IProjectDAO extends JpaRepository<ProjectDomainImpl, String> {

}