package com.fiuni.marketplacefreelancer.dao.Tag;

import com.fiuni.marketplacefreelancer.domain.tag.TagDomainImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITagDao extends JpaRepository<TagDomainImpl, Integer> {
}
