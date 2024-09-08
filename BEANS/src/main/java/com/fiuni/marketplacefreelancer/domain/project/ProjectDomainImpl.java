package com.fiuni.marketplacefreelancer.domain.project;

import com.fiuni.marketplacefreelancer.domain.base.IBaseDomain;
import com.fiuni.marketplacefreelancer.domain.enums.ProjectStatus;
import com.fiuni.marketplacefreelancer.domain.user.UserDomainImpl;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "project")
public class ProjectDomainImpl implements IBaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private String id;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDomainImpl user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProjectStatus status;

    @Column(name = "url")
    private String url;

    @Column(name = "startDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;

}
