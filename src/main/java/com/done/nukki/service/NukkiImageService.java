package com.done.nukki.service;

import com.done.nukki.dto.req.CreateNukkiImageReqDto;
import com.done.nukki.dto.res.NukkiImageResDto;
import com.done.nukki.entity.Member;
import com.done.nukki.entity.NukkiImage;
import com.done.nukki.repository.NukkiImageRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NukkiImageService {

    private final EntityManager entityManager;
    private final NukkiImageRepository nukkiImageRepository;

    // 생성자 주입
    @Autowired
    public NukkiImageService(NukkiImageRepository nukkiImageRepository, EntityManager entityManager, S3Service s3Service) {
        this.entityManager = entityManager;
        this.nukkiImageRepository = nukkiImageRepository;
    }

    public NukkiImageResDto create(CreateNukkiImageReqDto dto) {
        Member member = entityManager.getReference(Member.class, dto.getMemberId());

        System.out.println("dto = " + dto.getName());

        return new NukkiImageResDto(nukkiImageRepository.save(new NukkiImage(dto, member)));
    }
}