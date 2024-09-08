package com.fiuni.marketplacefreelancer.dao;

import com.fiuni.marketplacefreelancer.domain.message.MessageImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMessageDao extends JpaRepository<MessageImpl, Integer> {
}
