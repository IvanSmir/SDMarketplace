package com.fiuni.marketplacefreelancer.dao.user;

import com.fiuni.marketplacefreelancer.domain.price.PriceDomainImpl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserDao extends JpaRepository<PriceDomainImpl, String> {
}
