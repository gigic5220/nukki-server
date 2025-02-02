package com.done.nukki.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "report", schema = "nukki")
public class Report {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_member_id", nullable = false)
    private Member targetMember;

    @Column(name = "type", nullable = false)
    private Boolean type = false;

    @ColumnDefault("0")
    @Column(name = "status")
    private Boolean status;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created")
    private Instant created;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "upated")
    private Instant upated;

}