package com.fiuni.marketplacefreelancer.dao.Rate;

import com.fiuni.marketplacefreelancer.domain.rate.ProfileRateImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProfileRateDao  extends JpaRepository<ProfileRateImpl, Integer> {
}
