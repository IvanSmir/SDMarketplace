package com.fiuni.authapi.dao;

import com.fiuni.marketplacefreelancer.domain.user.UserDomainImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserDao extends JpaRepository<UserDomainImpl, String> {

    Optional<UserDomainImpl> findByEmail(String email);

}