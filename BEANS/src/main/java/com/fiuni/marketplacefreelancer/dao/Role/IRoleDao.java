package com.fiuni.marketplacefreelancer.dao.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fiuni.marketplacefreelancer.domain.role.RoleDomainImpl;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleDao extends JpaRepository<RoleDomainImpl, Integer> {
}
