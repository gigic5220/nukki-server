package com.done.nukki.dto.req;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateNukkiImageReqDto {

    @NotNull(message = "Member ID는 필수입니다.")
    private Integer memberId;

    @NotBlank(message = "이미지 URL은 필수입니다.")
    @Pattern(
        regexp = "^(https?://)?[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(/[a-zA-Z0-9@:%._+~#=/?&-]*)?$",
        message = "유효한 URL 형식이 아닙니다."
    )
    private String url;

    @NotBlank(message = "이미지 이름은 필수입니다.")
    @Size(max = 50, message = "이미지 이름은 최대 50자까지 가능합니다.")
    private String name;

    @Size(max = 255, message = "이미지 설명은 최대 255자까지 가능합니다.")
    private String description;

    @NotNull(message = "공개 여부는 필수입니다.")
    private Boolean isPublic;
}
