package com.fiuni.marketplacefreelancer.domain.profile;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serial;

import com.fiuni.marketplacefreelancer.domain.base.IBaseDomain;
import com.fiuni.marketplacefreelancer.domain.user.UserDomainImpl;

@Entity
@Data
@Table(name = "profile")
public class ProfileDomainImpl implements IBaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDomainImpl user;

    @Column(name = "experience")
    private String experience;

    @Column(name = "portfolio_url")
    private String portfolioUrl;

    @Column(name = "rate", nullable = false)
    private float rate;

    @Column(name = "description")
    private String description;

}
