package com.done.nukki.controller;

import com.done.nukki.dto.res.MemberResDto;
import com.done.nukki.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/{id}")
    public MemberResDto getById(@PathVariable("id") int id) {
        return new MemberResDto(memberService.getById(id));
    }
}