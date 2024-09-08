package com.fiuni.marketplacefreelancer.domain.tag;

import com.fiuni.marketplacefreelancer.domain.base.IBaseDomain;
import com.fiuni.marketplacefreelancer.domain.project.ProjectDomainImpl;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;

@Data
@Entity
@Table(name = "project_tag")
public class ProjectTagDomainImpl implements IBaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectDomainImpl project;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private TagDomainImpl tag;

}
