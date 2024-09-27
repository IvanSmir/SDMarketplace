package com.fiuni.apiusuarios.dao.rate;

import com.fiuni.marketplacefreelancer.domain.rate.RateDomainImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRateDao extends JpaRepository<RateDomainImpl, String> {

    Page<RateDomainImpl> findAllByProfileId(String profileId, Pageable pageable);
}
