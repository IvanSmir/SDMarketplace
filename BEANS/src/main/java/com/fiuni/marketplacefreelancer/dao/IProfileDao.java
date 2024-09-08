package com.fiuni.marketplacefreelancer.dao;

import com.fiuni.marketplacefreelancer.domain.profile.ProfileDomainImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProfileDao extends JpaRepository<ProfileDomainImpl, Integer> {
}
