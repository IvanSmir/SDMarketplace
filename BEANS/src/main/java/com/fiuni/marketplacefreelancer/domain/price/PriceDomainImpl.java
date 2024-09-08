package com.fiuni.marketplacefreelancer.domain.price;

import com.fiuni.marketplacefreelancer.domain.base.IBaseDomain;
import com.fiuni.marketplacefreelancer.domain.project.ProjectDomainImpl;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.time.LocalTime;


@Data
@Entity
@Table(name = "price")
public class PriceDomainImpl implements IBaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectDomainImpl project;

    @Column(name = "price", nullable = false)
    private float price;

    // TODO: Add currency enums
    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "date", nullable = false)
    private LocalTime date;

}
