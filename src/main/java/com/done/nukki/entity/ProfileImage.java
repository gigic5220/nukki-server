package com.done.nukki.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "profile_image", schema = "nukki")
public class ProfileImage {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "url", nullable = false)
    private String url;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created")
    private Instant created;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated")
    private Instant updated;

}