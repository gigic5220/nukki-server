package com.done.nukki.controller;

import com.done.nukki.dto.req.CreateNukkiImageReqDto;
import com.done.nukki.dto.res.NukkiImageResDto;
import com.done.nukki.service.NukkiImageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/nukki-image")
public class NukkiImageController {

    private final NukkiImageService nukkiImageService;

    @Autowired
    public NukkiImageController(NukkiImageService nukkiImageService) {
        this.nukkiImageService = nukkiImageService;
    }

    @PostMapping("/")
    public NukkiImageResDto create(@Valid @RequestBody CreateNukkiImageReqDto dto) {
        return nukkiImageService.create(dto);
    }
}