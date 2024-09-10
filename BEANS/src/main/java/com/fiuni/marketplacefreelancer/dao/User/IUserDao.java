package com.fiuni.marketplacefreelancer.dao.User;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fiuni.marketplacefreelancer.domain.user.UserDomainImpl;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserDao extends JpaRepository<UserDomainImpl, Integer> {
}