package com.fiuni.apiusuarios.dao;

import com.fiuni.marketplacefreelancer.domain.role.RoleDomainImpl;
import com.fiuni.marketplacefreelancer.dto.Role.RoleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleDao extends JpaRepository<RoleDomainImpl, String> {
    public RoleDomainImpl findByName(String name);

}
