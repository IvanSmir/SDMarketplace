package com.fiuni.marketplacefreelancer.domain.tag;


import com.fiuni.marketplacefreelancer.domain.base.IBaseDomain;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;

@Data
@Entity
@Table(name = "tag")
public class TagDomainImpl implements IBaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

}
