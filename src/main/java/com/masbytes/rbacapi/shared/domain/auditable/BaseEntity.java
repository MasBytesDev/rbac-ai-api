package com.masbytes.rbacapi.shared.domain.auditable;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Abstract base class for all entities. Provides common fields such as primary
 * key, public UUID, creation timestamp, and update timestamp. Ensures that each
 * entity has a unique public identifier automatically initialized before
 * persistence.
 */
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(value = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuperBuilder
public class BaseEntity {

    /**
     * The internal primary key of the entity. Auto-generated using identity
     * strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The public UUID of the entity. Unique, immutable, and automatically
     * initialized before persistence.
     */
    @EqualsAndHashCode.Include
    @Column(name = "public_id", nullable = false, updatable = false, unique = true)
    private UUID publicId;

    /**
     * The timestamp when the entity was created. Automatically set on
     * persistence.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * The timestamp when the entity was last updated. Automatically refreshed
     * on update.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Initializes the public UUID before persistence if not already set.
     * Ensures that every entity has a unique external identifier.
     */
    @PrePersist
    protected void initializePublicId() {
        if (this.publicId == null) {
            this.publicId = UUID.randomUUID();
        }
    }

}
