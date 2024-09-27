package com.fiuni.marketplacefreelancer.domain.profile;

import com.fiuni.marketplacefreelancer.domain.rate.RateDomainImpl;
import com.fiuni.marketplacefreelancer.domain.skill.SkillDomainImpl;
import jakarta.persistence.*;
import lombok.Data;
import java.io.Serial;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @OneToMany(mappedBy = "profile",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RateDomainImpl> rates = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "profile_skill",
            joinColumns = @JoinColumn(name = "profile_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id"))
    private Set<SkillDomainImpl> skills = new HashSet<>();

    @Column(name = "description")
    private String description;


    public void addRate(RateDomainImpl rate) {
        rates.add(rate);
        rate.setProfile(this);
    }

    public void removeRate(RateDomainImpl rate) {
        rates.remove(rate);
        rate.setProfile(this);
    }

}
