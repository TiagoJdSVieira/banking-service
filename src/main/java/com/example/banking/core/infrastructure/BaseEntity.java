package com.example.banking.core.infrastructure;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    @SequenceGenerator(name = "sequence_generator", sequenceName = "sequence_name", allocationSize = 1)
    private Long id;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private long createdDate;
}
