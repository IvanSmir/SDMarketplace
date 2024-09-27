package com.fiuni.apiusuarios.dao.profile;

import com.fiuni.marketplacefreelancer.domain.profile.ProfileDomainImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IProfileDao extends JpaRepository<ProfileDomainImpl, String> {
    Optional<ProfileDomainImpl> findByUserId(String userId);

    Page<ProfileDomainImpl> findBySkills_NameContaining(String skillName, Pageable pageable);
}
