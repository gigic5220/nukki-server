package com.done.nukki.service;

import com.done.nukki.dto.req.CreateNukkiImageReqDto;
import com.done.nukki.dto.res.NukkiImageResDto;
import com.done.nukki.entity.Member;
import com.done.nukki.entity.NukkiImage;
import com.done.nukki.repository.NukkiImageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 눅끼 이미지 관련 비즈니스 로직을 처리하는 서비스 클래스.
 */
@Service
public class NukkiImageService {

    private final NukkiImageRepository nukkiImageRepository;
    private final S3Service s3Service;
    private final AuthService authService;

    /**
     * NukkiImageService 생성자.
     *
     * @param nukkiImageRepository Nukki 이미지 관련 데이터 저장소
     */
    @Autowired
    public NukkiImageService(NukkiImageRepository nukkiImageRepository, S3Service s3Service, AuthService authService) {
        this.nukkiImageRepository = nukkiImageRepository;
        this.s3Service = s3Service;
        this.authService = authService;
    }

    /**
     * 현재 로그인한 사용자의 정보를 기반으로 새로운 눅끼 이미지를 생성한다.
     *
     * @param dtoJsonString  누끼 이미지 생성 요청 데이터
     * @param file 이미지 파일
     * @return 생성된 이미지의 응답 DTO
     */
    public NukkiImageResDto create(String dtoJsonString, MultipartFile file) {

        ObjectMapper objectMapper = new ObjectMapper();
        CreateNukkiImageReqDto dto;

        try {
            dto = objectMapper.readValue(dtoJsonString, CreateNukkiImageReqDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 파싱 오류: " + e.getMessage(), e);
        }

        // S3에 이미지 파일 업로드
        String fileUrl = s3Service.uploadFile(file);

        // 현재 로그인한 사용자 정보 가져오기
        Member member = authService.getSecurityContextMember();

        // NukkiImage 엔티티 생성 및 저장
        NukkiImage savedImage = nukkiImageRepository.save(new NukkiImage(dto, member, fileUrl));

        // 저장된 데이터를 NukkiImageResDto로 변환하여 반환
        return new NukkiImageResDto(savedImage);
    }
}
