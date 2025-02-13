package com.done.nukki.entity;

import com.done.nukki.dto.req.CreateNukkiImageReqDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "nukki_image", schema = "nukki")
@NoArgsConstructor
public class NukkiImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "name", length = 50)
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

    @Column(nullable = false, updatable = false)
    private LocalDateTime created;

    @Column(nullable = false)
    private LocalDateTime updated;

    @PrePersist
    protected void onCreate() {
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = LocalDateTime.now();
    }

    @Column()
    private LocalDateTime deleted;

    public NukkiImage(CreateNukkiImageReqDto dto, Member member) {
        this.member = member;
        this.url = dto.getUrl();
        this.name = dto.getName();
        this.hit = 0;
        this.like = 0;
        this.description = dto.getDescription();
        this.isPublic = dto.getIsPublic();
    }
}