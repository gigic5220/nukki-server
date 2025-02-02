package com.done.nukki.entity;

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
@Table(name = "member", schema = "nukki")
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(length = 12)
    private String nickname;

    @Column(length = 10)
    private String provider;

    @Column(name = "social_account")
    private String socialAccount;

    @Column()
    private String status;

    @Column(updatable = false)
    private LocalDateTime created;

    @Column()
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

    public Member(String socialAccount, String provider, String status) {
        this.socialAccount = socialAccount;
        this.provider = provider;
        this.status = status;
    }
}