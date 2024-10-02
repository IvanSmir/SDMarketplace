package com.fiuni.marketplacefreelancer.dao.price;

import com.fiuni.marketplacefreelancer.domain.price.PriceDomainImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPriceDao extends JpaRepository<PriceDomainImpl, String> {
    Optional<PriceDomainImpl> findByProjectIdAndActive(String projectId, Boolean active);
}
