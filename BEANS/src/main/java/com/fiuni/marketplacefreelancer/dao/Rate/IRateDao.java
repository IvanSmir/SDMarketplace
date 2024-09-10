package com.fiuni.marketplacefreelancer.dao.Rate;

import com.fiuni.marketplacefreelancer.domain.rate.RateDomainImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRateDao extends JpaRepository<RateDomainImpl, Integer> {
}
