package com.fiuni.marketplacefreelancer.domain.role;

import com.fiuni.marketplacefreelancer.domain.base.IBaseDomain;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@Table(name = "roles")
public class RoleDomainImpl implements IBaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

}
