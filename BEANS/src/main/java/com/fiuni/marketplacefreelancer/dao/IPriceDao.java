package com.fiuni.marketplacefreelancer.dao;

import com.fiuni.marketplacefreelancer.domain.price.PriceDomainImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPriceDao extends JpaRepository<PriceDomainImpl, Integer> {
}
