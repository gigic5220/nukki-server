package com.done.nukki.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "nukki_image", schema = "nukki")
public class NukkiImage {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "name", length = 12)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false;

    @ColumnDefault("0")
    @Column(name = "`like`")
    private Integer like;

    @ColumnDefault("0")
    @Column(name = "hit")
    private Integer hit;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created")
    private Instant created;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated")
    private Instant updated;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "deleted")
    private Instant deleted;

}