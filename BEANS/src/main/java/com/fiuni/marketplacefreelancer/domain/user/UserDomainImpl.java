package com.fiuni.marketplacefreelancer.domain.user;

import com.fiuni.marketplacefreelancer.domain.base.IBaseDomain;
import com.fiuni.marketplacefreelancer.domain.role.RoleDomainImpl;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class UserDomainImpl implements IBaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleDomainImpl role;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password", length = 60)
    private String password;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
}
