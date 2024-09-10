package com.fiuni.marketplacefreelancer.dao.Skill;

import com.fiuni.marketplacefreelancer.domain.skill.ProfileSkillImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProfileSkillDao extends JpaRepository<ProfileSkillImpl, Integer> {
}
