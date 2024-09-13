package com.fiuni.marketplacefreelancer.domain.rate;

import com.fiuni.marketplacefreelancer.domain.base.IBaseDomain;
import com.fiuni.marketplacefreelancer.domain.enums.RateType;
import com.fiuni.marketplacefreelancer.domain.profile.ProfileDomainImpl;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;


@Data
@Entity
@Table(name = "rate")
public class RateDomainImpl implements IBaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "rate_type", nullable = false)
    private RateType rateType;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private ProfileDomainImpl profile;



}
