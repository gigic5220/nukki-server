package com.done.nukki.dto.req;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class ListReqDto {
    private int page = 0;
    private int pageSize = 10;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalDateTime endDate;

    public String searchKeyword;
    public String searchKeywordCategory;

    public String sort;
}