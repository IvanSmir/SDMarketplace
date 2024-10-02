package com.fiuni.marketplacefreelancer.dao.tag;

import com.fiuni.marketplacefreelancer.domain.tag.TagDomainImpl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITagDao extends JpaRepository<TagDomainImpl, String> {
    Optional<TagDomainImpl> findByName(String name);
}
