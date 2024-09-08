package com.fiuni.marketplacefreelancer.dao;

import com.fiuni.marketplacefreelancer.domain.skill.SkillDomainImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISkillDao extends JpaRepository<SkillDomainImpl, Integer> {
}
