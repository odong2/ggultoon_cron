package com.architecture.admin.models.dto.cron;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CronDto {
    private Long idx; // 일련번호
    private String name; // 이름
    private String detail; // 설명
    private Integer state; // 상태값
    private String regdate; // 등록일
    private String regdateTz; // 등록일 타임존
}
