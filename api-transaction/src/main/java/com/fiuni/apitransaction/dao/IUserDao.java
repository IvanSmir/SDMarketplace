package com.fiuni.apitransaction.dao;

import com.fiuni.marketplacefreelancer.domain.user.UserDomainImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserDao extends JpaRepository<UserDomainImpl, String> {


}
