package com.fiuni.marketplacefreelancer.domain.message;

import com.fiuni.marketplacefreelancer.domain.base.IBaseDomain;
import com.fiuni.marketplacefreelancer.domain.project.ProjectDomainImpl;
import com.fiuni.marketplacefreelancer.domain.user.UserDomainImpl;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "message")
public class MessageImpl implements IBaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectDomainImpl project;

    @ManyToOne
    @JoinColumn(name = "receptor_id", nullable = false)
    private UserDomainImpl receptor;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserDomainImpl sender;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

}
