package com.fiuni.marketplacefreelancer.domain.transaction;

import com.fiuni.marketplacefreelancer.domain.base.IBaseDomain;
import com.fiuni.marketplacefreelancer.domain.enums.PaymentStatus;
import com.fiuni.marketplacefreelancer.domain.enums.TransactionType;
import com.fiuni.marketplacefreelancer.domain.project.ProjectDomainImpl;
import com.fiuni.marketplacefreelancer.domain.user.UserDomainImpl;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "transaction")
public class TransactionImpl implements IBaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @OneToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectDomainImpl project;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private UserDomainImpl client;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = false)
    private UserDomainImpl freelancer;


    @Column(name = "amount", nullable = false)
    private float amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Column(name = "date", nullable = false)
    private LocalTime date;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;

}
