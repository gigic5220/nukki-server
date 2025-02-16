package com.done.nukki.controller;

import com.done.nukki.dto.res.NukkiImageResDto;
import com.done.nukki.service.NukkiImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/nukki-image")
public class NukkiImageController {

    private final NukkiImageService nukkiImageService;

    @Autowired
    public NukkiImageController(NukkiImageService nukkiImageService) {
        this.nukkiImageService = nukkiImageService;
    }

    @PostMapping(value = "", consumes = {"multipart/form-data"})
    public NukkiImageResDto create(@RequestPart(value = "dto") String dtoJsonString, @RequestPart(value = "file") MultipartFile file) {
        return nukkiImageService.create(dtoJsonString, file);
    }

}