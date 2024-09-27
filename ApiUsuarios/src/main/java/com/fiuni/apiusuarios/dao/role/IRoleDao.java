package com.fiuni.apiusuarios.dao.role;

import com.fiuni.marketplacefreelancer.domain.role.RoleDomainImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleDao extends JpaRepository<RoleDomainImpl, String> {
    Optional<RoleDomainImpl> findByName(String name);

    @Query("SELECT DISTINCT r.id FROM RoleDomainImpl r")
    Page<String> findAllIds(Pageable pageable);
}
