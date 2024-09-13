package com.fiuni.apiusuarios.dao.user;

import com.fiuni.marketplacefreelancer.domain.user.UserDomainImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserDao extends JpaRepository<UserDomainImpl, String> {

    Optional<UserDomainImpl> findByEmail(String email);

    Page<UserDomainImpl> findAllByRoleId(String roleId, Pageable pageable);
}