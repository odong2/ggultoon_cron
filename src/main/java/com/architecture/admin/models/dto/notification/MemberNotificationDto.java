package com.architecture.admin.models.dto.notification;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MemberNotificationDto {

    private Long idx;
    private Long memberIdx;     // member.idx
    private String type;        // 알림 보낼 테이블명
    private Long typeIdx;       // 알림 보낼 테이블의 idx
    private String title;       // 알림 내용
    private Integer state;      // 상태값
    private String regdate;     // 전송일
    private String regdateTz;   // 전송일 타임존
    private String checkdate;   // 확인일
    private String checkdateTz; // 확인일 타임존
    private String deldate;     // 삭제일
    private String deldateTz;   // 삭제일 타임존
}
