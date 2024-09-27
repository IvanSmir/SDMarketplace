package com.fiuni.apiusuarios.dao.profile;

import com.fiuni.marketplacefreelancer.domain.profile.ProfileDomainImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProfileDao extends JpaRepository<ProfileDomainImpl, String> {
    Optional<ProfileDomainImpl> findByUserId(String userId);

    Page<ProfileDomainImpl> findAllBySkills_Id(String skillId, Pageable pageable);

    @Query("SELECT DISTINCT p.id FROM ProfileDomainImpl p")
    Page<String> findAllIds(Pageable pageable);
}
