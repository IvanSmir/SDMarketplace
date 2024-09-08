package com.fiuni.marketplacefreelancer.domain.skill;

import com.fiuni.marketplacefreelancer.domain.base.IBaseDomain;
import com.fiuni.marketplacefreelancer.domain.profile.ProfileDomainImpl;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;

@Data
@Entity
@Table(name = "profile_skill")
public class ProfileSkillImpl implements IBaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private ProfileDomainImpl profile;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private SkillDomainImpl skill;
}
