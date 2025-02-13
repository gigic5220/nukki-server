package com.done.nukki.dto.res;
import com.done.nukki.entity.NukkiImage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class NukkiImageResDto {
    private final Integer id;
    private final MemberResDto member;
    private final String url;
    private final String name;
    private final String description;
    private final Boolean isPublic;
    private final Integer like;
    private final Integer hit;
    private final LocalDateTime created;
    private final LocalDateTime updated;
    private final LocalDateTime deleted;

    public NukkiImageResDto(NukkiImage nukkiImage) {
        this.id = nukkiImage.getId();
        this.member = new MemberResDto(nukkiImage.getMember());
        this.url = nukkiImage.getUrl();
        this.name = nukkiImage.getName();
        this.description = nukkiImage.getDescription();
        this.isPublic = nukkiImage.getIsPublic();
        this.like = nukkiImage.getLike();
        this.hit = nukkiImage.getHit();
        this.created = nukkiImage.getCreated();
        this.updated = nukkiImage.getUpdated();
        this.deleted = nukkiImage.getDeleted();
    }
}
