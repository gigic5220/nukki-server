package com.done.nukki.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "nukki_image_tag_association", schema = "nukki")
public class NukkiImageTagAssociation {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nukki_image_id", nullable = false)
    private NukkiImage nukkiImage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created")
    private Instant created;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "udpated")
    private Instant udpated;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "deleted")
    private Instant deleted;

}