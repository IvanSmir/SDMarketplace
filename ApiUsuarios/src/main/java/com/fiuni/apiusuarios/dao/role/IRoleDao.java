package com.fiuni.apiusuarios.dao.role;

import com.fiuni.marketplacefreelancer.domain.role.RoleDomainImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleDao extends JpaRepository<RoleDomainImpl, String> {
    Optional<RoleDomainImpl> findByName(String name);
}
