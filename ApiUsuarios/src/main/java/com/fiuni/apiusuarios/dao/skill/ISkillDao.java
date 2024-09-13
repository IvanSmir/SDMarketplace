package com.fiuni.apiusuarios.dao.skill;

import com.fiuni.marketplacefreelancer.domain.skill.SkillDomainImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISkillDao extends JpaRepository<SkillDomainImpl, String> {

    Optional<SkillDomainImpl> findByName(String name);
}
